namespace Eden.Domain.Media
{
    using Core;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("PictureSpecification")]
    public partial class PictureSpecification : BaseEntity
    {

        [Required]
        [StringLength(500)]
        [FullIndexKey]
        public string Name { get; set; }

        [Required]
        [StringLength(100)]
        public string Suffix { get; set; }

        public int Width { get; set; }

        public int Height { get; set; }

        public bool IsSystem { get; set; }

        public bool Enable { get; set; }
    }
}
