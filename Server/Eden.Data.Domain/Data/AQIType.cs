namespace Eden.Domain.Data
{
    using Core;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("AQIType")]
    public partial class AQIType : BaseEntity
    {

        [NotMapped]
        public new int Id { get { return AQITypeId; } }

        public short AQITypeId { get; set; }

        [Column("AQIType")]
        [StringLength(50)]
        public string AQIType1 { get; set; }

        [StringLength(50)]
        public string AQITypeName { get; set; }
    }
}
