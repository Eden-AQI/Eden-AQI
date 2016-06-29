using Eden.Core.Caching;
using Eden.Core.Data;
using Eden.Core.Infrastructure;
using Eden.Domain.Data;
using Eden.Domain.Metadata;
using Eden.ServicesDefine;
using Eden.ServicesDefine.Data;
using Eden.ServicesDefine.Metadata;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Services.Data
{
    public class AqiManager : IAqiManager
    {
        private readonly ICacheManager _cacheManager;
        private readonly string CACHE_KEY_SITEAQI_JSON = "AQI.SITE.JSON";
        private readonly string CACHE_KEY_ALLSITE_CURRENT_AQI = "REAILTIME_DATA";

        private readonly IRepository<UpdateTime> _updateTimeRepository;
        private readonly IRepository<DataRTSite> _siteDataRepository;
        private readonly IEntityService<Parameter> _parameterRepository;
        //private readonly IRepository<Site> _siteRepository;
        private readonly IEntityService<Site> _siteService;
        private readonly IAqiGradeService _aqiGradeService;
        private readonly IRepository<DataRTGroup> _groupDataRepository;

        public AqiManager(ICacheManager cacheManager,
            IRepository<UpdateTime> updateTimeRepository,
            IRepository<DataRTSite> siteDataRepository,
            IEntityService<Parameter> parameterRepository,
            IEntityService<Site> siteService,
            IAqiGradeService aqiGradeService,
            IRepository<DataRTGroup> groupDataRepository)
        {
            _updateTimeRepository = updateTimeRepository;
            _siteDataRepository = siteDataRepository;
            _parameterRepository = parameterRepository;
            _siteService = siteService;
            _cacheManager = cacheManager;
            _aqiGradeService = aqiGradeService;
            _groupDataRepository = groupDataRepository;
        }

        public string GetSiteAqiJson(int siteId)
        {
            string cacheKey = CACHE_KEY_SITEAQI_JSON + siteId;
            return _cacheManager.Get<string>(cacheKey, () => { return GenSiteRealtimeAqiJson(siteId); });
        }

        private string GenSiteRealtimeAqiJson(int siteId)
        {
            Dictionary<int, SiteData> allData = _cacheManager.Get<Dictionary<int, SiteData>>(CACHE_KEY_ALLSITE_CURRENT_AQI, () => { return BuildAllSiteRealTimeAqi(); });
            if (null == allData || !allData.ContainsKey(siteId))
                return null;
            SiteData theData = allData[siteId];
            string json = JsonConvert.SerializeObject(theData);
            return json;
        }

        public Dictionary<int, SiteData> BuildAllSiteRealTimeAqi()
        {
            var time = _updateTimeRepository.Table.Where(t => t.Name == "AQINow").FirstOrDefault();
            if (time == null)
                return null;
            //var dataSource = _siteDataRepository.Table.Where(d => d.DurationID == 10 && d.LST_AQI == time.Time).Where(d => d.ParameterID == 101 || d.ParameterID == 102 || d.ParameterID == 104 || d.ParameterID == 106 || d.ParameterID == 107 || d.ParameterID == 108).ToList();

            //var dataSource = _siteDataRepository.Table.Where(d => d.DurationID == 10 && d.LST_AQI == time.Time).Where(d => d.ParameterID == 1 || d.ParameterID == 2 || d.ParameterID == 6 || d.ParameterID == 7 || d.ParameterID == 8 || d.ParameterID == 21).ToList();

            var dataSource = _siteDataRepository.Table.Where(d => d.DurationID == 10 && d.LST_AQI == time.Time).Where(d => d.AQIItemID == 100 || d.AQIItemID == 101 || d.AQIItemID == 102 || d.AQIItemID == 107 || d.AQIItemID == 104 || d.AQIItemID == 106 || d.ParameterID == 108).ToList();
            Dictionary<int, List<DataRTSite>> siteData = new Dictionary<int, List<DataRTSite>>();

            Dictionary<int, SiteData> result = new Dictionary<int, SiteData>();

            Dictionary<int, List<ParameterHourData>> hourData = Get24HoursData(time.Time);
            Dictionary<int, List<DayAqi>> dayData = Get30DayAqi(time.Time);

            var siteService = EngineContext.Current.Resolve<ISiteService>();

            foreach (var dataItem in dataSource)
            {
                List<DataRTSite> sList = null;
                if (siteData.ContainsKey(dataItem.SiteID))
                    sList = siteData[dataItem.SiteID];
                else
                {
                    sList = new List<DataRTSite>();
                    siteData[dataItem.SiteID] = sList;
                }
                sList.Add(dataItem);
            }

            foreach (int siteId in siteData.Keys)
            {
                var pData = siteData[siteId];
                //var primaryData = pData.OrderByDescending(s => s.AQI).FirstOrDefault();
                //PrimaryParameter pp = new PrimaryParameter()
                //{
                //    Aqi = primaryData.AQI.Value,
                //    Value = primaryData.Value.Value,
                //    ParameterId = primaryData.ParameterID.Value,
                //    ParameterName = primaryData.Parameter
                //};

                var theSite = siteService.GetById(siteId);

                SiteData sd = new SiteData();
                if (null != theSite)
                    sd.Name = theSite.Name;
                sd.UpdateTime = time.Time.ToString("HH:mm");
                foreach (var pd in pData)
                {
                    if (pd.AQIItemID == 100)
                    {
                        var primaryData = pd;
                        PrimaryParameter pp = new PrimaryParameter()
                        {
                            Aqi = primaryData.AQI.Value,
                            Value = Math.Round(primaryData.Value.Value, 2),
                            ParameterId = primaryData.ParameterID.Value,
                            ParameterName = primaryData.Parameter
                        };
                        sd.Primary = pp;
                        sd.Aqi = pp.Aqi;
                        sd.Grade = _aqiGradeService.CalcGrade(pp.Aqi);
                    }
                    else {
                        ParameterData parameterData = new ParameterData();
                        parameterData.Value = pd.Value != null ? (Math.Round(pd.Value.Value, 2) + "ug/m3") : "";
                        parameterData.Name = pd.Parameter;
                        sd.OtherParameters.Add(parameterData);
                    }
                }

                if (hourData.ContainsKey(siteId))
                    sd.Hours = hourData[siteId];

                if (dayData.ContainsKey(siteId))
                    sd.Days = dayData[siteId];
                if (sd.Days == null || sd.Days.Count == 0)
                    sd.Days = createExampleDayAqi(time.Time);
                sd.Forecast.AddRange(createExampleForecast(DateTime.Now));
                result[siteId] = sd;
            }
            result[0] = GetCityRealtimeAqi(time.Time);
            return result;
        }

        private Dictionary<int, List<ParameterHourData>> Get24HoursData(DateTime time)
        {
            DateTime d2 = time.AddHours(-24);
            //var dataSource = _siteDataRepository.Table.Where(d => d.DurationID == 10 && d.LST_AQI <= time && d.LST_AQI >= d2).Where(d => d.ParameterID == 101 || d.ParameterID == 102 || d.ParameterID == 104 || d.DurationID == 106 || d.ParameterID == 107 || d.ParameterID == 108).OrderBy(d => d.LST_AQI).ToList();

            //var dataSource = _siteDataRepository.Table.Where(d => d.DurationID == 10 && d.LST_AQI <= time && d.LST_AQI >= d2).Where(d => d.ParameterID == 1 || d.ParameterID == 2 || d.ParameterID == 6 || d.ParameterID == 7 || d.ParameterID == 8 || d.ParameterID == 108).OrderBy(d => d.LST_AQI).ToList();

            var dataSource = _siteDataRepository.Table.Where(d => d.DurationID == 10 && d.LST_AQI <= time && d.LST_AQI >= d2).Where(d => d.AQIItemID == 100 || d.AQIItemID == 101 || d.AQIItemID == 102 || d.AQIItemID == 107 || d.AQIItemID == 104 || d.AQIItemID == 106 || d.ParameterID == 108).OrderBy(d => d.LST_AQI).ToList();

            Dictionary<int, List<ParameterHourData>> result = new Dictionary<int, List<ParameterHourData>>();
            foreach (var dataItem in dataSource)
            {
                List<ParameterHourData> phdList = null;
                if (result.ContainsKey(dataItem.SiteID))
                    phdList = result[dataItem.SiteID];
                else
                {
                    phdList = new List<ParameterHourData>();
                    result[dataItem.SiteID] = phdList;
                }
                ParameterHourData theData = phdList.Where(p => p.ParameterId == dataItem.ParameterID).FirstOrDefault();
                if (null == theData)
                {
                    theData = new ParameterHourData() { Parameter = dataItem.Parameter, ParameterId = dataItem.ParameterID.Value, Unit = "ug/m3" };
                    phdList.Add(theData);
                }
                HourData hd = new HourData() { Aqi = dataItem.AQI.Value, Value = Math.Round(dataItem.Value.Value, 2), DateTime = dataItem.LST_AQI };
                theData.Data.Add(hd);
            }
            return result;
        }

        private Dictionary<int, List<DayAqi>> Get30DayAqi(DateTime time)
        {
            DateTime d2 = time.AddDays(-30);
            var dataSource = _siteDataRepository.Table.Where(d => d.DurationID == 11 && d.LST_AQI <= time && d.LST_AQI >= d2 && d.ParameterID == 0)
                .OrderBy(d => d.LST_AQI).ToList();

            Dictionary<int, List<DayAqi>> result = new Dictionary<int, List<DayAqi>>();
            foreach (var dataItem in dataSource)
            {
                List<DayAqi> dayAqi = null;
                if (result.ContainsKey(dataItem.SiteID))
                    dayAqi = result[dataItem.SiteID];
                else
                {
                    dayAqi = new List<DayAqi>();
                    result[dataItem.SiteID] = dayAqi;
                }
                DayAqi aqi = new DayAqi() { Aqi = dataItem.AQI.Value, DayTime = dataItem.LST_AQI, Level = dataItem.Grade.Value };
                dayAqi.Add(aqi);

            }

            return result;
        }

        public int GetSiteCurrentAqi(int siteId)
        {
            Dictionary<int, SiteData> allData = _cacheManager.Get<Dictionary<int, SiteData>>(CACHE_KEY_ALLSITE_CURRENT_AQI, () => { return BuildAllSiteRealTimeAqi(); });
            if (allData.ContainsKey(siteId))
                return allData[siteId].Aqi;
            return 0;
        }

        private SiteData GetCityRealtimeAqi(DateTime time)
        {
            var dataSource = _groupDataRepository.Table.Where(d => d.DurationID == 10 && d.GroupID == 102 && d.LST_AQI == time).Where(d => d.AQIItemID == 100 || d.AQIItemID == 101 || d.AQIItemID == 102 || d.AQIItemID == 107 || d.AQIItemID == 104 || d.AQIItemID == 106 || d.ParameterID == 108).ToList();

            SiteData result = new SiteData()
            {
                Name = "郑州市",
                UpdateTime = time.ToString("HH:mm")
            };
            foreach (var dataItem in dataSource)
            {
                if (dataItem.AQIItemID == 100)
                {
                    var primaryData = dataItem;
                    PrimaryParameter pp = new PrimaryParameter()
                    {
                        Aqi = primaryData.AQI.Value,
                        Value = Math.Round(primaryData.Value.Value, 2),
                        ParameterId = primaryData.ParameterID.Value,
                        ParameterName = primaryData.Parameter
                    };
                    result.Primary = pp;
                    result.Aqi = pp.Aqi;
                    result.Grade = _aqiGradeService.CalcGrade(pp.Aqi);
                }
                else {
                    ParameterData pd = new ParameterData();
                    pd.Name = dataItem.Parameter;
                    pd.Value = null != dataItem.Value ? (Math.Round(dataItem.Value.Value, 2) + "ug/m3") : "";
                    result.OtherParameters.Add(pd);
                }
            }

            DateTime d2 = time.AddHours(-24);
            var hourData = _groupDataRepository.Table.Where(d => d.DurationID == 10 && d.GroupID == 102 && d.LST_AQI <= time && d.LST_AQI >= d2).Where(d => d.AQIItemID == 100 || d.AQIItemID == 101 || d.AQIItemID == 102 || d.AQIItemID == 107 || d.AQIItemID == 104 || d.AQIItemID == 106 || d.ParameterID == 108).OrderBy(d => d.LST_AQI).ToList();
            List<ParameterHourData> hp = new List<ParameterHourData>();

            foreach (var dataItem in hourData)
            {
                ParameterHourData theData = hp.Where(p => p.ParameterId == dataItem.ParameterID).FirstOrDefault();
                if (null == theData)
                {
                    theData = new ParameterHourData() { Parameter = dataItem.Parameter, ParameterId = dataItem.ParameterID.Value, Unit = "ug/m3" };
                    hp.Add(theData);
                }
                HourData hd = new HourData() { Aqi = dataItem.AQI.Value, Value = Math.Round(dataItem.Value.Value, 2), DateTime = dataItem.LST_AQI };
                theData.Data.Add(hd);
            }
            result.Hours = hp;
            d2 = time.AddDays(-30);

            var dayData = _groupDataRepository.Table.Where(d => d.DurationID == 11 && d.GroupID == 102 && d.LST_AQI <= time && d.LST_AQI >= d2 && d.ParameterID == 0).OrderBy(d => d.LST_AQI).ToList();
            foreach (var dataItem in dayData)
            {
                DayAqi dayAqi = new DayAqi();
                dayAqi.Aqi = dataItem.AQI.Value;
                dayAqi.Level = dataItem.Grade.Value;
                dayAqi.DayTime = dataItem.LST_AQI;
                result.Days.Add(dayAqi);
            }
            if (result.Days.Count == 0)
                result.Days.AddRange(createExampleDayAqi(time));
            result.Forecast.AddRange(createExampleForecast(DateTime.Now));
            return result;
        }


        private List<DayAqi> createExampleDayAqi(DateTime time)
        {
            List<DayAqi> result = new List<DayAqi>();
            time = new DateTime(time.Year, time.Month, time.Day);
            DateTime d1 = time.AddDays(-30);
            Random r = new Random();
            while (d1 <= time)
            {
                DayAqi dayAqi = new DayAqi();
                dayAqi.Aqi = r.Next(1, 300);
                var g = _aqiGradeService.CalcGrade(dayAqi.Aqi);
                dayAqi.Level = g.Grade;
                dayAqi.DayTime = d1;
                d1 = d1.AddDays(1);
                result.Add(dayAqi);
            }
            return result;
        }

        private List<ForecastItem> createExampleForecast(DateTime time)
        {
            string[] ps = new string[] { "PM2.5", "PM10", "SO2", "O3", "CO", "NO2" };
            List<ForecastItem> result = new List<ForecastItem>();
            DateTime endTime = time.AddDays(3);
            Random r = new Random();
            while (time <= endTime)
            {
                ForecastItem item = new ForecastItem();
                if (time.Hour >= 6 && time.Hour <= 18)
                    item.Time = time.Day + "日白天";
                else
                    item.Time = time.Day + "日夜间";
                item.Aqi = r.Next(1, 300);
                var g = _aqiGradeService.CalcGrade(item.Aqi);
                item.AqiLevel = g.Grade.ToString();
                item.PrimaryParameter = ps[r.Next(0, 6)];
                result.Add(item);
                time = time.AddHours(12);
            }
            return result;
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
            result.Add(createExampleRanking("0"));
            result.Add(createExampleRanking("1"));
            result.Add(createExampleRanking("2"));
            result.Add(createExampleRanking("3"));
            return result;
        }

        private RankingData createExampleRanking(string duration)
        {
            RankingData data = new RankingData() { Duration = duration, Items = new List<RankingParameterData>() };
            string[] ps = new string[] { "AQI", "PM2.5", "SO2", "NO2", "O3" };
            var siteService = EngineContext.Current.Resolve<ISiteService>();
            var allSites = siteService.GetAllSite();

            Random r = new Random();
            foreach (string parameter in ps)
            {
                RankingParameterData pData = new RankingParameterData() { Parameter = parameter, Data = new List<RankingSiteData>() };
                foreach (var s in allSites)
                {
                    RankingSiteData sd = new RankingSiteData()
                    {
                        SiteName = s.Name.Trim()
                    };
                    if (parameter == "AQI")
                        sd.Value = r.Next(1, 300);
                    else
                        sd.Value = new decimal(Math.Round(r.NextDouble(), 2));
                    pData.Data.Add(sd);
                }
                pData.Data = pData.Data.OrderByDescending(d => d.Value).ToList();
                data.Items.Add(pData);
            }
            return data;
        }

        public void Update()
        {
            var data = BuildAllSiteRealTimeAqi();
            _cacheManager.Set(CACHE_KEY_ALLSITE_CURRENT_AQI, data, 60);
        }

        public List<Station> GetAllStation()
        {
            throw new NotImplementedException();
        }
    }
}
