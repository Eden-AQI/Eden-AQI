using Eden.ServicesDefine.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Eden.Domain.Data;
using Eden.Domain.Metadata;
using Eden.Core.Caching;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using System.IO;
using Eden.ServicesDefine.Metadata;
using Microsoft.International.Converters.PinYinConverter;
using Eden.Services.Configuration;
using Eden.Domain.Configuration;

namespace Eden.Services.Data
{
    public class RestAqiManager : IAqiManager
    {
        private string dataUrl;
        private string cityCode;

        private readonly ICacheManager _cacheManager;
        private readonly IAqiGradeService _aqiGradeService;
        private readonly ISettingService _settingService;

        private readonly string CACHEKEY_LISTDATA_REALTIME = "REALTIME_LISTDATA";
        private readonly string CACHEKEY_LISTDATA_DAY = "DAY_LISTDATA";
        private readonly string CACHEKEY_LISTDATA_MONTH = "MONTH_LISTDATA";
        private readonly string CACHEKEY_LISTDATA_YEAR = "YEAR_LISTDATA";

        private readonly string CACHE_KEY_SITEAQI_JSON = "AQI.SITE.JSON";
        private readonly string CACHE_KEY_ALLSITE_CURRENT_AQI = "REAILTIME_DATA";

        public RestAqiManager(ICacheManager cacheManager, IAqiGradeService aqiGradeService, ISettingService settingService)
        {
            _cacheManager = cacheManager;
            _aqiGradeService = aqiGradeService;
            _settingService = settingService;
        }

        private string ServerAddress
        {
            get
            {
                if (string.IsNullOrEmpty(dataUrl))
                {
                    var systemSettings = _settingService.LoadSetting<SystemSetting>();
                    dataUrl = systemSettings.DataApiAddress;
                    if (dataUrl[dataUrl.Length - 1] != '/')
                        dataUrl += "/";
                }
                return dataUrl;
            }
        }

        private string CityCode
        {
            get
            {
                if (string.IsNullOrEmpty(cityCode))
                {
                    var systemSettings = _settingService.LoadSetting<SystemSetting>();
                    cityCode = systemSettings.CityCode;
                }
                return cityCode;
            }
        }

        /// <summary>
        /// 所有站点
        /// </summary>
        /// <returns></returns>
        public List<Station> GetAllStation()
        {
            string cacheKey = "ALL_STATION";
            List<Station> result = new List<Station>();
            var cache = _cacheManager.Get<List<Station>>(cacheKey, () =>
             {
                 return requestAllStationFromServer();
             });
            result.AddRange(cache);
            return result;
        }



        /// <summary>
        /// 站点AQI
        /// </summary>
        /// <param name="stationCode"></param>
        /// <returns></returns>
        public string GetSiteAqiJson(string stationCode)
        {
            string cacheKey = CACHE_KEY_SITEAQI_JSON + stationCode;
            return _cacheManager.Get<string>(cacheKey, () => { return GenSiteRealtimeAqiJson(stationCode); });
        }

        private string GenSiteRealtimeAqiJson(string stationCode)
        {
            Dictionary<string, SiteData> allData = _cacheManager.Get<Dictionary<string, SiteData>>(CACHE_KEY_ALLSITE_CURRENT_AQI, () => { return BuildAllSiteRealTimeAqi(); });
            if (null == allData || !allData.ContainsKey(stationCode))
                return null;
            SiteData theData = allData[stationCode];
            string json = JsonConvert.SerializeObject(theData);
            return json;
        }

        private List<Station> requestAllStationFromServer()
        {
            string json = AqiWebRequest.ReqUrl(ServerAddress + "api-fims-model/station/getStationData", paramData: "cityCode=" + CityCode);
            if (string.IsNullOrEmpty(json))
                return null;
            JsonSerializer serializer = new JsonSerializer();
            StringReader sr = new StringReader(json);
            object o = serializer.Deserialize(new JsonTextReader(sr), typeof(List<Station>));
            var result = o as List<Station>;
            foreach (var s in result)
            {
                s.FirstPinyin = getFirstPinyin(s.StationName);
            }

            return result;
        }

        /// <summary> 
        /// 汉字转化为拼音首字母
        /// </summary> 
        /// <param name="str">汉字</param> 
        /// <returns>首字母</returns> 
        private string getFirstPinyin(string str)
        {
            if (string.IsNullOrEmpty(str))
                return string.Empty;

            foreach (char obj in str)
            {
                try
                {
                    ChineseChar chineseChar = new ChineseChar(obj);
                    return chineseChar.Pinyins[0][0].ToString();
                }
                catch
                {
                }
            }
            return string.Empty;
        }

        public Dictionary<string, SiteData> BuildAllSiteRealTimeAqi()
        {
            Dictionary<string, SiteData> result = new Dictionary<string, SiteData>();

            List<StationOrCityMeasuredData> stationHourData = requestAllStationMeasureHourDataFromServer();
            List<StationOrCityMeasuredData> stationDayData = requestAllStationMeasureDayDataFromServer();

            stationHourData.AddRange(requestCityMeasureHourDataFromServer());
            stationDayData.AddRange(requestCityMeasureDayDataFromServer());

            Dictionary<DateTime, List<StationOrCityMeasuredData>> tpdHour = new Dictionary<DateTime, List<StationOrCityMeasuredData>>();
            Dictionary<DateTime, List<StationOrCityMeasuredData>> tpdDay = new Dictionary<DateTime, List<StationOrCityMeasuredData>>();
            Dictionary<string, List<StationOrCityMeasuredData>> shd = margeStationData(stationHourData, tpdHour);
            Dictionary<string, List<StationOrCityMeasuredData>> sdd = margeStationData(stationDayData, tpdDay);

            List<CityForecastData> cityForecast = requestCityForecastDataFromServer().OrderBy(f => f.DateTime).ToList();

            //列表
            RankingData hourRankingData = initRandkingData("0");
            RankingData dayRankingData = initRandkingData("1");

            var allStation = GetAllStation();
            allStation.AddRange(GetAllStation());
            allStation.Add(new Station() { StationCode = "0", StationName = "郑州市" });
            foreach (Station s in allStation)
            {
                SiteData sd = new SiteData() { Name = s.StationName, StationCode = s.StationCode };
                result[s.StationCode] = sd;

                if (shd.ContainsKey(s.StationCode))
                {
                    var hourData = shd[s.StationCode];
                    fillHourToSiteData(sd, hourData, hourRankingData);
                }
                if (sdd.ContainsKey(s.StationCode))
                    fillDayToSiteData(sd, sdd[s.StationCode], dayRankingData);
                if (s.StationCode == "0")
                {
                    foreach (CityForecastData f in cityForecast)
                    {
                        sd.Forecast.Add(new ForecastItem() { Aqi = f.Aqi_min + "-" + f.Aqi_max, AqiLevel = f.AqiLevel, PrimaryParameter = f.PrimaryPol, Time = f.DateTime.Day + "日" });
                    }
                }
            }
            _cacheManager.Set(CACHEKEY_LISTDATA_REALTIME, hourRankingData, 60);
            _cacheManager.Set(CACHEKEY_LISTDATA_DAY, dayRankingData, 1440);

            return result;
        }

        private RankingData initRandkingData(string duration)
        {
            RankingData rd = new RankingData() { Duration = duration };
            rd.Items.Add(new RankingParameterData() { Parameter = "AQI" });
            rd.Items.Add(new RankingParameterData() { Parameter = "SO2" });
            rd.Items.Add(new RankingParameterData() { Parameter = "NO2" });
            rd.Items.Add(new RankingParameterData() { Parameter = "PM10" });
            rd.Items.Add(new RankingParameterData() { Parameter = "PM2.5" });
            rd.Items.Add(new RankingParameterData() { Parameter = "CO" });
            rd.Items.Add(new RankingParameterData() { Parameter = "O3" });
            return rd;
        }

        private void fillHourToSiteData(SiteData siteData, List<StationOrCityMeasuredData> hourData, RankingData rd)
        {
            var newestData = hourData[hourData.Count - 1];

            PrimaryParameter pp = new PrimaryParameter();
            pp.Aqi = int.Parse(newestData.AQI);
            pp.ParameterName = newestData.PrimaryPol;
            pp.Value = switchPpValue(newestData);

            siteData.Primary = pp;
            siteData.Grade = _aqiGradeService.CalcGrade(pp.Aqi);
            siteData.Aqi = pp.Aqi;

            siteData.UpdateTime = newestData.DateTime.ToString("yyyy-MM-dd HH:00");

            fillRankingData(rd, newestData, siteData.Name, pp.Aqi);

            siteData.OtherParameters.Add(new ParameterData() { Name = "SO2", Value = parseStringToDecimal(newestData.SO2) + "ug/m3" });
            siteData.OtherParameters.Add(new ParameterData() { Name = "NO2", Value = parseStringToDecimal(newestData.NO2) + "ug/m3" });
            siteData.OtherParameters.Add(new ParameterData() { Name = "PM10", Value = parseStringToDecimal(newestData.PM10) + "ug/m3" });
            siteData.OtherParameters.Add(new ParameterData() { Name = "PM2.5", Value = parseStringToDecimal(newestData.PM25) + "ug/m3" });
            siteData.OtherParameters.Add(new ParameterData() { Name = "CO", Value = parseStringToDecimal(newestData.CO) + "mg/m3" });
            siteData.OtherParameters.Add(new ParameterData() { Name = "O3", Value = parseStringToDecimal(newestData.O3) + "ug/m3" });

            ParameterHourData so2 = new ParameterHourData() { Parameter = "SO2", Unit = "ug/m3" };
            ParameterHourData no2 = new ParameterHourData() { Parameter = "NO2", Unit = "ug/m3" };
            ParameterHourData pm10 = new ParameterHourData() { Parameter = "PM10", Unit = "ug/m3" };
            ParameterHourData pm25 = new ParameterHourData() { Parameter = "PM2.5", Unit = "ug/m3" };
            ParameterHourData co = new ParameterHourData() { Parameter = "CO", Unit = "mg/m3" };
            ParameterHourData o3 = new ParameterHourData() { Parameter = "O3", Unit = "ug/m3" };

            foreach (var h in hourData)
            {
                so2.Data.Add(new HourData() { Aqi = parseStringToInt(h.SO2IAQI), Value = parseStringToDecimal(h.SO2), DateTime = h.DateTime });
                no2.Data.Add(new HourData() { Aqi = parseStringToInt(h.NO2IAQI), Value = parseStringToDecimal(h.NO2), DateTime = h.DateTime });
                pm10.Data.Add(new HourData() { Aqi = parseStringToInt(h.PM10IAQI), Value = parseStringToDecimal(h.PM10), DateTime = h.DateTime });
                pm25.Data.Add(new HourData() { Aqi = parseStringToInt(h.PM25IAQI), Value = parseStringToDecimal(h.PM25), DateTime = h.DateTime });
                co.Data.Add(new HourData() { Aqi = parseStringToInt(h.COIAQI), Value = parseStringToDecimal(h.CO), DateTime = h.DateTime });
                o3.Data.Add(new HourData() { Aqi = parseStringToInt(h.O3IAQI), Value = parseStringToDecimal(h.O3), DateTime = h.DateTime });
            }
            siteData.Hours.AddRange(new ParameterHourData[] { so2, no2, pm10, pm25, co, o3 });

        }

        private void fillDayToSiteData(SiteData siteData, List<StationOrCityMeasuredData> dayData, RankingData rd)
        {
            foreach (var d in dayData)
            {
                int aqi = parseStringToInt(d.AQI);
                var g = _aqiGradeService.CalcGrade(aqi);
                siteData.Days.Add(new DayAqi() { Aqi = aqi, Level = g.Grade, DayTime = d.DateTime });
            }
            var newestData = dayData[dayData.Count - 1];
            fillRankingData(rd, newestData, siteData.Name, parseStringToInt(newestData.AQI));
        }

        private void fillRankingData(RankingData rd, StationOrCityMeasuredData newestData, string siteName, int aqi)
        {
            rd.Items.Where(p => p.Parameter == "AQI").FirstOrDefault().Data.Add(new RankingSiteData() { SiteName = siteName, Value = aqi });
            rd.Items.Where(p => p.Parameter == "SO2").FirstOrDefault().Data.Add(new RankingSiteData() { SiteName = siteName, Value = parseStringToDecimal(newestData.SO2) });
            rd.Items.Where(p => p.Parameter == "NO2").FirstOrDefault().Data.Add(new RankingSiteData() { SiteName = siteName, Value = parseStringToDecimal(newestData.NO2) });
            rd.Items.Where(p => p.Parameter == "PM10").FirstOrDefault().Data.Add(new RankingSiteData() { SiteName = siteName, Value = parseStringToDecimal(newestData.PM10) });
            rd.Items.Where(p => p.Parameter == "PM2.5").FirstOrDefault().Data.Add(new RankingSiteData() { SiteName = siteName, Value = parseStringToDecimal(newestData.PM25) });
            rd.Items.Where(p => p.Parameter == "CO").FirstOrDefault().Data.Add(new RankingSiteData() { SiteName = siteName, Value = parseStringToDecimal(newestData.CO) });
            rd.Items.Where(p => p.Parameter == "O3").FirstOrDefault().Data.Add(new RankingSiteData() { SiteName = siteName, Value = parseStringToDecimal(newestData.O3) });
        }

        private decimal switchPpValue(StationOrCityMeasuredData d)
        {
            string val = "";
            switch (d.PrimaryPol.ToUpper())
            {
                case "SO2": val = d.SO2; break;
                case "NO2": val = d.NO2; break;
                case "PM10": val = d.PM10; break;
                case "CO": val = d.CO; break;
                case "O3": val = d.O3; break;
                case "PM2.5": val = d.PM25; break;
            }
            decimal dVal = 0;
            decimal.TryParse(val, out dVal);
            return dVal;
        }

        private int parseStringToInt(string str)
        {
            int val = 0;
            int.TryParse(str, out val);
            return val;
        }

        private decimal parseStringToDecimal(string str)
        {
            decimal d = 0;
            decimal.TryParse(str, out d);
            return Math.Round(d, 2);
        }

        /// <summary>
        /// 合并站点数据
        /// </summary>
        /// <param name="dataSource"></param>
        /// <returns></returns>
        private Dictionary<string, List<StationOrCityMeasuredData>> margeStationData(List<StationOrCityMeasuredData> dataSource, Dictionary<DateTime, List<StationOrCityMeasuredData>> tpd)
        {
            Dictionary<string, List<StationOrCityMeasuredData>> stDatas = new Dictionary<string, List<StationOrCityMeasuredData>>();


            foreach (StationOrCityMeasuredData item in dataSource)
            {
                List<StationOrCityMeasuredData> times = null;
                if (stDatas.ContainsKey(item.StationCode))
                    times = stDatas[item.StationCode];
                else
                {
                    times = new List<StationOrCityMeasuredData>();
                    stDatas[item.StationCode] = times;
                }
                times.Add(item);

                List<StationOrCityMeasuredData> ss = null;
                if (tpd.ContainsKey(item.DateTime))
                    ss = tpd[item.DateTime];
                else
                {
                    ss = new List<StationOrCityMeasuredData>();
                    tpd[item.DateTime] = ss;
                }
                ss.Add(item);
            }

            for (int i = 0; i < stDatas.Keys.Count; i++)
            {
                string key = stDatas.Keys.ElementAt(i);
                stDatas[key] = stDatas[key].OrderBy(d => d.DateTime).ToList();
            }

            return stDatas;
        }

        private string getAllStationCode()
        {
            List<Station> stations = GetAllStation();
            StringBuilder sbStationIds = new StringBuilder();
            string split = "";
            foreach (Station s in stations)
            {
                sbStationIds.Append(split);
                sbStationIds.Append(s.StationCode);
                split = ",";
            }
            return sbStationIds.ToString();
        }

        /// <summary>
        /// 站点小时实测数据
        /// </summary>
        /// <returns></returns>
        private List<StationOrCityMeasuredData> requestAllStationMeasureHourDataFromServer()
        {
            string url = ServerAddress + "api-fims-model/measured/getStationHourlyData";
            string json = AqiWebRequest.ReqUrl(url, paramData: "auditState=0&stationCode=" + getAllStationCode() + "&date=24");
            return readMeasuredDataFromJson(json);
        }

        /// <summary>
        /// 城市小时实测数据
        /// </summary>
        /// <returns></returns>
        private List<StationOrCityMeasuredData> requestCityMeasureHourDataFromServer()
        {
            string url = ServerAddress + "api-fims-model/measured/getCityHourlyData";
            string json = AqiWebRequest.ReqUrl(url, paramData: "auditState=0&cityCode=410100&date=24");
            return readMeasuredDataFromJson(json);
        }

        /// <summary>
        /// 站点日实测数据
        /// </summary>
        /// <returns></returns>
        private List<StationOrCityMeasuredData> requestAllStationMeasureDayDataFromServer()
        {
            string url = ServerAddress + "api-fims-model/measured/getStationDaylyData";
            string json = AqiWebRequest.ReqUrl(url, paramData: "auditState=0&stationCode=" + getAllStationCode() + "&date=29");
            return readMeasuredDataFromJson(json);
        }

        /// <summary>
        /// 城市日实测数据
        /// </summary>
        /// <returns></returns>
        private List<StationOrCityMeasuredData> requestCityMeasureDayDataFromServer()
        {
            string url = ServerAddress + "api-fims-model/measured/getCityDaylyData";
            string json = AqiWebRequest.ReqUrl(url, paramData: "auditState=0&cityCode=410100&date=29");
            return readMeasuredDataFromJson(json);
        }

        /// <summary>
        /// 城市预报数据
        /// </summary>
        /// <returns></returns>
        private List<CityForecastData> requestCityForecastDataFromServer()
        {
            string url = ServerAddress + "api-fims-model/correct/getCorrectData";
            string json = AqiWebRequest.ReqUrl(url, paramData: "cityCodes=410100");
            if (string.IsNullOrEmpty(json))
                return new List<CityForecastData>();
            JsonSerializer serializer = new JsonSerializer();
            StringReader sr = new StringReader(json);
            object o = serializer.Deserialize(new JsonTextReader(sr), typeof(List<CityForecastData>));
            var rs = o as List<CityForecastData>;
            if (null == rs)
                rs = new List<CityForecastData>();
            return rs;
        }

        private List<StationOrCityMeasuredData> readMeasuredDataFromJson(string json)
        {
            if (string.IsNullOrEmpty(json))
                return new List<StationOrCityMeasuredData>();
            JsonSerializer serializer = new JsonSerializer();
            StringReader sr = new StringReader(json);
            object o = serializer.Deserialize(new JsonTextReader(sr), typeof(List<StationOrCityMeasuredData>));
            var rs = o as List<StationOrCityMeasuredData>;
            if (null == rs)
                rs = new List<StationOrCityMeasuredData>();
            return rs;
        }

        public string GetRankingData()
        {
            string cacheKey = "ranking";
            return _cacheManager.Get<string>(cacheKey, () =>
            {
                var data = calcRankingData();
                string json = JsonConvert.SerializeObject(data);
                return json;
            });
        }

        private List<RankingData> calcRankingData()
        {
            List<RankingData> result = new List<RankingData>();
            result.Add(getRankingDataFromCache(CACHEKEY_LISTDATA_REALTIME, "0"));
            result.Add(getRankingDataFromCache(CACHEKEY_LISTDATA_DAY, "1"));
            result.Add(getRankingDataFromCache(CACHEKEY_LISTDATA_MONTH, "2"));
            result.Add(getRankingDataFromCache(CACHEKEY_LISTDATA_YEAR, "3"));
            return result;
        }

        public int[] GetSiteCurrentAqi(string stationCodes)
        {
            List<int> aqis = new List<int>();
            Dictionary<string, SiteData> allData = _cacheManager.Get<Dictionary<string, SiteData>>(CACHE_KEY_ALLSITE_CURRENT_AQI, () => { return BuildAllSiteRealTimeAqi(); });
            string[] siteCodes = stationCodes.Split(',');
            foreach (string c in siteCodes)
            {
                if (allData.ContainsKey(c))
                    aqis.Add(allData[c].Aqi);
                else
                    aqis.Add(0);
            }
            return aqis.ToArray();
        }

        public void Update()
        {
            var data = BuildAllSiteRealTimeAqi();
            _cacheManager.Set(CACHE_KEY_ALLSITE_CURRENT_AQI, data, 60);
        }

        private RankingData getRankingDataFromCache(string key, string duration)
        {
            RankingData data = _cacheManager.Get<RankingData>(key);
            if (null == data)
                return new RankingData() { Duration = duration };
            return data;
        }

        public SiteData GetSiteCurrentData(string stationCode)
        {
            Dictionary<string, SiteData> allData = _cacheManager.Get<Dictionary<string, SiteData>>(CACHE_KEY_ALLSITE_CURRENT_AQI, () => { return BuildAllSiteRealTimeAqi(); });
            if (allData.ContainsKey(stationCode))
                return allData[stationCode];
            return null;
        }
    }
}
