namespace Eden.Domain.Data
{
    using Core;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("AQIItem")]
    public partial class AQIItem : BaseEntity
    {
        [NotMapped]
        public new int Id { get { return AQIItemID; } }

        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public short AQIItemID { get; set; }

        [StringLength(10)]
        public string TimeType { get; set; }

        [StringLength(10)]
        public string AQIItemType { get; set; }

        [StringLength(10)]
        public string AQIItemType_24hr { get; set; }

        [StringLength(10)]
        public string AQIType_bak { get; set; }

        [StringLength(20)]
        public string AQIItemName { get; set; }

        public short? ParameterID { get; set; }

        public short? DurationID { get; set; }

        public short? AggregateID { get; set; }

        [StringLength(50)]
        public string Parameter { get; set; }

        [StringLength(50)]
        public string Description { get; set; }

        public int? OrderID { get; set; }
    }
}
