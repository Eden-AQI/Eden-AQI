namespace Eden.Domain.Logging
{
    using Core;
    using Customers;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("Log")]
    public partial class Log : BaseEntity
    {

        public int? LogLevelId { get; set; }

        [Column(TypeName = "text")]
        [FullIndexKey]
        public string ShortMessage { get; set; }

        [Column(TypeName = "text")]
        [FullIndexKey]
        public string FullMessage { get; set; }

        [StringLength(200)]
        [FullIndexKey]
        public string IpAddress { get; set; }

        [StringLength(128)]
        public string CustomerId { get; set; }

        [StringLength(500)]
        public string PageUrl { get; set; }

        [StringLength(500)]
        public string ReferrerUrl { get; set; }

        [OrderBy(Desc = true)]
        public DateTime? CreatedOnUtc { get; set; }

        public int EventSource { get; set; }

        [NotMapped]
        public LogLevel LogLevel
        {
            get
            {
                return (LogLevel)this.LogLevelId;
            }
            set
            {
                this.LogLevelId = (int)value;
            }
        }

        [NotMapped]
        public Customer Customer { get; set; }
        
    }
}
