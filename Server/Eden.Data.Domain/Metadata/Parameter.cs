namespace Eden.Domain.Metadata
{
    using Core;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("Parameter")]
    public partial class Parameter : BaseEntity
    {
        [NotMapped]
        public new int Id { get { return ParameterID; } }

        [Key]
        [Column(Order = 0)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public short ParameterID { get; set; }

        [Key]
        [Column(Order = 1)]
        [StringLength(50)]
        public string Name { get; set; }

        [Key]
        [Column(Order = 2)]
        [StringLength(50)]
        public string IngestCode { get; set; }

        [Key]
        [Column(Order = 3)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public short UnitID { get; set; }

        [Key]
        [Column(Order = 4)]
        public byte Scale { get; set; }

        [Key]
        [Column(Order = 5)]
        [StringLength(50)]
        public string AQSCode { get; set; }

        [Key]
        [Column(Order = 6)]
        public bool IsAlpha { get; set; }

        [Key]
        [Column(Order = 7)]
        public bool Ambient { get; set; }

        [Key]
        [Column(Order = 8)]
        public bool Hourly { get; set; }

        [Key]
        [Column(Order = 9)]
        public byte HourlyDurationID { get; set; }

        [Key]
        [Column(Order = 10)]
        public byte HourlyPercent { get; set; }

        [Key]
        [Column(Order = 11)]
        public bool Daily { get; set; }

        [Key]
        [Column(Order = 12)]
        public bool NoonToNoon { get; set; }

        [Key]
        [Column(Order = 13)]
        public byte DailyPercent { get; set; }

        [Key]
        [Column(Order = 14)]
        public bool IsAQSImport { get; set; }

        [Key]
        [Column(Order = 15)]
        [StringLength(512)]
        public string Description { get; set; }

        [Key]
        [Column(Order = 16)]
        public bool IsAutoQC { get; set; }

        [Key]
        [Column(Order = 17)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public int RetentionInDays { get; set; }

        [Key]
        [Column(Order = 18)]
        public bool RetentionSaveFirstDatum { get; set; }

        [Key]
        [Column(Order = 19)]
        public bool SubhourlyAvg { get; set; }

        [Key]
        [Column(Order = 20)]
        public byte DurationSubhourlyID { get; set; }

        [Key]
        [Column(Order = 21)]
        public byte SubhourlyPercent { get; set; }

        [Key]
        [Column(Order = 22)]
        public byte SubhourlyAvgType { get; set; }
    }
}
