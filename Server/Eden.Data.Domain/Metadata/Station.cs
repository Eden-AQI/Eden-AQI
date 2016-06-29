using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Domain.Metadata
{
    public class Station
    {
        public string CityCode { get; set; }
        public string StationCode { get; set; }
        public string StationName { get; set; }
        public decimal Longitude { get; set; }
        public decimal Latitude { get; set; }
        public string StationType { get; set; }
        public string FirstPinyin { get; set; }
    }

    public class CityForecastData
    {
        public string DataDate { get; set; }

        public string CityCode { get; set; }

        public string CityName { get; set; }

        public string Aqi_min { get; set; }

        public string Aqi_max { get; set; }

        public string PrimaryPol { get; set; }

        public string AqiLevel { get; set; }

        public DateTime DateTime
        {
            get
            {
                return StationOrCityMeasuredData.DateTimeFromString(DataDate);
            }
        }
    }

    public class StationOrCityMeasuredData
    {
        private string stationCode;

        public string Time { get; set; }

        public string StationCode
        {
            get
            {
                if (string.IsNullOrEmpty(stationCode))
                    return "0";
                return stationCode;
            }
            set { stationCode = value; }
        }

        public string CityCode { get; set; }

        public string SO2 { get; set; }

        public string SO2IAQI { get; set; }

        public string NO2 { get; set; }

        public string NO2IAQI { get; set; }

        public string PM10 { get; set; }

        public string PM10IAQI { get; set; }

        public string PM10_24h { get; set; }

        public string PM10_24hIAQI { get; set; }

        public string CO { get; set; }

        public string COIAQI { get; set; }

        public string O3 { get; set; }

        public string O3IAQI { get; set; }

        public string O3_8h { get; set; }

        public string O3_8hIAQI { get; set; }

        public string PM25 { get; set; }

        public string PM25IAQI { get; set; }

        public string PM25_24h { get; set; }

        public string PM25_24hIAQI { get; set; }

        public string AQI { get; set; }

        public string PrimaryPol { get; set; }

        public string AQILevel { get; set; }

        public DateTime DateTime
        {
            get
            {
                return DateTimeFromString(Time);
            }
        }

        public static DateTime DateTimeFromString(string str)
        {
            if (string.IsNullOrEmpty(str))
                return DateTime.MinValue;
            int year = int.Parse(str.Substring(0, 4));
            int month = int.Parse(str.Substring(4, 2));
            int day = int.Parse(str.Substring(6, 2));
            int hour = 0;
            if (str.Length == 10)
                hour = int.Parse(str.Substring(8, 2));
            DateTime rs = new DateTime(year, month, day, hour, 0, 0);
            return rs;
        }
    }
}
