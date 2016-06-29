namespace Eden.Domain.Data
{
    using Core;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("AQIGrade")]
    public partial class AQIGrade : BaseEntity
    {
        [NotMapped]
        public new long Id { get { return GID; } }

        [Key]
        [Column(Order = 0)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public long GID { get; set; }

        [Key]
        [Column(Order = 1)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public int Grade { get; set; }

        [StringLength(10)]
        public string GradeName { get; set; }

        public int? AQIMin { get; set; }

        public int? AQIMax { get; set; }

        [StringLength(10)]
        public string AQIState { get; set; }

        public int? ColorR { get; set; }

        public int? ColorG { get; set; }

        public int? ColorB { get; set; }

        [StringLength(100)]
        public string HealthEffect { get; set; }

        [StringLength(100)]
        public string Method { get; set; }
    }
}
