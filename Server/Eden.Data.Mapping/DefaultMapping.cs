using Eden.Domain;
using Eden.Domain.Configuration;
using Eden.Domain.Customers;
using Eden.Domain.Data;
using Eden.Domain.Logging;
using Eden.Domain.Media;
using Eden.Domain.Metadata;
using Eden.Domain.Tasks;
using System;
using System.Collections.Generic;
using System.Data.Entity.ModelConfiguration;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.DataMapping
{
    public class LogMapping : EntityTypeConfiguration<Log> { }
    public class MediaItemMapping : EntityTypeConfiguration<MediaItem> { }
    public class PictureSpecificationMapping : EntityTypeConfiguration<PictureSpecification> { }
    public class ScheduleTaskMapping : EntityTypeConfiguration<ScheduleTask> { }
    public class SmsValidateMapping : EntityTypeConfiguration<SmsValidate> { }

    public class AQIGradeMapping : EntityTypeConfiguration<AQIGrade> { }
    public class AQIItemMapping : EntityTypeConfiguration<AQIItem> { }
    public class AQITypeMapping : EntityTypeConfiguration<AQIType> { }
    public class DataReportSiteMapping : EntityTypeConfiguration<DataReportSite> { }
    public class DataRTSiteMapping : EntityTypeConfiguration<DataRTSite> { }
    public class DataRTGroupMapping : EntityTypeConfiguration<DataRTGroup> { }

    public class DeviceMapping : EntityTypeConfiguration<Device> { }

    public class CityMapping : EntityTypeConfiguration<City> { }
    public class CountyMapping : EntityTypeConfiguration<County> { }
    public class SiteMapping : EntityTypeConfiguration<Site> { }
    public class ParameterMapping : EntityTypeConfiguration<Parameter> { }
    public class UpdateTimeMapping : EntityTypeConfiguration<UpdateTime> { }

    public class UserActivityMapping : EntityTypeConfiguration<UserActivity> { }
    public class ReportMapping : EntityTypeConfiguration<Report> { }

    public class NotifyMapping : EntityTypeConfiguration<Notify> { }

    public class SettingMapping : EntityTypeConfiguration<Setting> { }
    public class RequestLogMapping : EntityTypeConfiguration<RequestLog> { }
}
