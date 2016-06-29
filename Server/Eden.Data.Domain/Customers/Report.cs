namespace Eden.Domain.Customers
{
    using Core;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("Report")]
    public partial class Report : BaseEntity
    {

        public int Value { get; set; }

        public int Duration { get; set; }

        public DateTime LST { get; set; }

        public int Type { get; set; }
    }
}
