namespace Eden.Domain.Metadata
{
    using Core;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("UpdateTime")]
    public partial class UpdateTime : BaseEntity
    {

        [NotMapped]
        public new int Id { get { return UpdateTimeID; } }

        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public int UpdateTimeID { get; set; }

        [Required]
        [StringLength(10)]
        public string Name { get; set; }

        [Column("UpdateTime", TypeName = "smalldatetime")]
        public DateTime Time { get; set; }
    }
}
