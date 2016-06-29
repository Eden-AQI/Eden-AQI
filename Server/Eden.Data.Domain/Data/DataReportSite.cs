namespace Eden.Domain.Data
{
    using Core;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;
    
    [Table("Data_Report_Site")]
    public partial class DataReportSite : BaseEntity
    {
        public new long Id { get; set; }

        [Key]
        [Column(Order = 0)]
        public DateTime LST { get; set; }

        [Key]
        [Column(Order = 1)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public short SiteID { get; set; }

        [Key]
        [Column(Order = 2)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public short AQIItemID { get; set; }

        public short? DurationID { get; set; }

        public short? AggregateID { get; set; }

        public short? ParameterID { get; set; }

        [StringLength(50)]
        public string Parameter { get; set; }

        public decimal? Value { get; set; }

        public int? AQI { get; set; }

        public int? Grade { get; set; }

        [StringLength(50)]
        public string Quality { get; set; }

        public int? ValidNum { get; set; }

        public decimal? Avg { get; set; }

        public decimal? Max { get; set; }

        public decimal? Min { get; set; }

        public decimal? Stdev { get; set; }

        public decimal? Percentile95 { get; set; }

        public DateTime? CreateTime { get; set; }

        public DateTime? UpdateTime { get; set; }
    }
}
