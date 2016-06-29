namespace Eden.Domain.Metadata
{
    using Core;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("Site")]
    public partial class Site : BaseEntity
    {
        [NotMapped]
        public new int Id { get { return SiteID; } }


        [Column(Order = 0)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public short SiteID { get; set; }


        [Column(Order = 1)]
        [StringLength(50)]
        public string StationID { get; set; }


        [Column(Order = 2)]
        [StringLength(50)]
        public string Name { get; set; }


        [Column(Order = 3)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public short StateID { get; set; }
        

        [Column(Order = 4)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public short CountyID { get; set; }


        [Column(Order = 5)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public int Elevation { get; set; }


        [Column(Order = 6, TypeName = "numeric")]
        public decimal Latitude { get; set; }


        [Column(Order = 7, TypeName = "numeric")]
        public decimal Longitude { get; set; }


        [Column(Order = 8, TypeName = "numeric")]
        public decimal UTCOffset { get; set; }


        [Column(Order = 9)]
        [StringLength(4)]
        public string AQSCode { get; set; }


        [Column(Order = 10)]
        [StringLength(9)]
        public string FullAQSCode { get; set; }


        [Column(Order = 11)]
        public bool IsActive { get; set; }

        [Column(Order = 12)]
        public bool IsApproved { get; set; }

        [StringLength(12)]
        public string IntlCode { get; set; }


        [Column(Order = 13)]
        public bool IsMobile { get; set; }


        [Column(Order = 14)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public short UserID { get; set; }

        [Column(Order = 15, TypeName = "smalldatetime")]
        public DateTime Modified { get; set; }

        [NotMapped]
        public string FirstPinyin { get; set; }
    }
}
