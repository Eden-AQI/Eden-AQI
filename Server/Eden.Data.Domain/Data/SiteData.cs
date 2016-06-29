using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Domain.Data
{
    [DataContract]
    public class SiteData
    {
        [DataMember]
        public string Name { get; set; }

        [DataMember]
        public string UpdateTime { get; set; }

        [DataMember]
        public int Aqi { get; set; }

        public AQIGrade Grade { get; set; }

        public PrimaryParameter Primary { get; set; }

        [DataMember]
        public string AqiLevel { get { return Grade.Grade.ToString(); } }

        [DataMember]
        public string PrimaryParameter { get { return Primary.ParameterName.Trim(); } }

        [DataMember]
        public string PrimaryValue { get { return Primary.Value.ToString(); } }

        [DataMember]
        public string Health { get { return Grade.HealthEffect; } }

        [DataMember]
        public string Suggest { get { return Grade.Method; } }

        private List<ForecastItem> _forecast = new List<ForecastItem>();

        [DataMember]
        public List<ForecastItem> Forecast { get { return _forecast; } }

        private List<ParameterData> _otherParameters = new List<ParameterData>();

        [DataMember]
        public List<ParameterData> OtherParameters { get { return _otherParameters; } }

        private List<DayAqi> _days = new List<DayAqi>();

        [DataMember]
        public List<DayAqi> Days { get { return _days; } set { _days = value; } }

        private List<ParameterHourData> _hours = new List<ParameterHourData>();

        [DataMember]
        public List<ParameterHourData> Hours { get { return _hours; } set { _hours = value; } }

        public string StationCode { get; set; }
    }

    [DataContract]
    public class ParameterData
    {
        [DataMember]
        public string Name { get; set; }

        [DataMember]
        public string Value { get; set; }
    }


    public class PrimaryParameter
    {
        public int ParameterId { get; set; }

        public string ParameterName { get; set; }

        public int Aqi { get; set; }

        public decimal Value { get; set; }
    }

    [DataContract]
    public class ParameterHourData
    {
        private List<HourData> _data = new List<HourData>();

        [DataMember]
        public string Parameter { get; set; }

        public int ParameterId { get; set; }

        [DataMember]
        public string Unit { get; set; }

        [DataMember]
        public List<HourData> Data { get { return _data; } }
    }

    [DataContract]
    public class HourData
    {
        public DateTime DateTime { get; set; }

        [DataMember]
        public string Time { get { return DateTime.ToString("yyyy-MM-dd HH:00"); } }

        [DataMember]
        public decimal Value { get; set; }
        [DataMember]
        public int Aqi { get; set; }
    }

    [DataContract]
    public class DayAqi
    {
        public DateTime DayTime { get; set; }
        [DataMember]
        public string Day { get { return DayTime.ToString("yyyy-MM-dd"); } }
        [DataMember]
        public int Aqi { get; set; }
        [DataMember]
        public int Level { get; set; }
    }

}
