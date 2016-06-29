namespace Eden.Domain.Customers
{
    using Core;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("UserActivity")]
    public partial class UserActivity:BaseEntity
    {

        public int EventType { get; set; }

        public string DeviceNumber { get; set; }

        public DateTime EventTime { get; set; }

        public string Message { get; set; }
    }
}
