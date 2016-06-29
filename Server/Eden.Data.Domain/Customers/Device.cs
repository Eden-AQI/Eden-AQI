namespace Eden.Domain.Customers
{
    using Core;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("Device")]
    public partial class Device : BaseEntity
    {
        [Key]
        public new int Id { get; set; }

        public int Platform { get; set; }

        public int DeviceType { get; set; }

        public string PushId { get; set; }

        [FullIndexKey]
        public string DeviceNumber { get; set; }

        public DateTime CreateTime { get; set; }

        public decimal? Latitude { get; set; }

        public decimal? Longitude { get; set; }
    }

}
