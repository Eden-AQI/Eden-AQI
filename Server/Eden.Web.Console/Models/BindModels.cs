using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Eden.Web.Console.Models
{

    public class DeviceModel : BaseViewModel
    {
        public string PlatformName { get; set; }

        public string DeviceTypeName { get; set; }

        public string PushId { get; set; }

        public string DeviceNumber { get; set; }

        public DateTime CreateTime { get; set; }

        public string CreateTimeString { get; set; }

        public decimal? Latitude { get; set; }

        public decimal? Longitude { get; set; }
    }

    public class DeviceListModel : BaseViewModel
    {
        public string PlatformName { get; set; }

        public string DeviceTypeName { get; set; }

        public string DeviceNumber { get; set; }

        public string CreateTime { get; set; }
    }

    public class PushViewModel : BaseViewModel
    {
        public int Level { get; set; }

        public string Message { get; set; }

        public DateTime CreateTime { get; set; }

        public int CityId { get; set; }

        public int Platform { get; set; }

        public string Type { get; set; }

        public string PlatformName { get; set; }

        public string CreateTimeString { get; set; }
    }

    public class PushListModel : BaseViewModel
    {
        public string Type { get; set; }

        public string PlatformName { get; set; }

        public string CreateTimeString { get; set; }

        public string Message { get; set; }
    }

    public class LogViewModel : BaseViewModel
    {
        public string LogLevelString { get; set; }

        public string ShortMessage { get; set; }

        public string FullMessage { get; set; }

        public string IpAddress { get; set; }

        public string CustomerId { get; set; }

        public string PageUrl { get; set; }

        public string ReferrerUrl { get; set; }

        public string EventTimeString { get; set; }

        public string EventSourceString { get; set; }

    }

    public class LogListModel : BaseViewModel
    {
        public string ShortMessage { get; set; }
        public string Level { get; set; }
        public string EventTimeString { get; set; }
        public string EventSourceString { get; set; }
    }

    public class ScheduleTaskViewModel : BaseViewModel
    {
        
        public string Name { get; set; }

        public int Seconds { get; set; }

        public bool Enabled { get; set; }

        public bool StopOnError { get; set; }

        public string EnabledString { get; set; }

        public string StopOnErrorString { get; set; }

        public string LastStartTime { get; set; }

        public string LastEndTime { get; set; }

        public string LastSuccessTime { get; set; }

    }

    public class SettingViewModel : BaseViewModel
    {
        public string Name { get; set; }

        public string Value { get; set; }
    }

    public class VersionSettingModel
    {
        public string VersionName { get; set; }

        public string VersionCode { get; set; }

        public string Description { get; set; }

        public string UpdateTime { get; set; }

        public string DownloadUrl { get; set; }

        public bool Mandatory { get; set; }

        public string MandatoryStr { get; set; }
    }

    public class ReadonlyModel : BaseViewModel
    {

    }
}