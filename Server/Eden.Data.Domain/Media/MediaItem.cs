namespace Eden.Domain.Media
{
    using Core;
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;
    using System.Drawing.Imaging;

    [Table("MediaItem")]
    public partial class MediaItem : BaseEntity
    {

        [Required]
        [StringLength(1000)]
        [FullIndexKey]
        public string Name { get; set; }

        [OrderBy(Desc = true)]
        public DateTime CreateOnUtc { get; set; }

        public int Size { get; set; }

        public int LocationId { get; set; }

        public string CustomerId { get; set; }

        [Required]
        [StringLength(500)]
        public string RelativePath { get; set; }

        [Required]
        [StringLength(100)]
        public string ContentType { get; set; }

        public string SpecificationId { get; set; }

        [NotMapped]
        public MediaSaveLocation SaveLocation
        {
            get { return (MediaSaveLocation)LocationId; }
            set { LocationId = (int)value; }
        }

        [NotMapped]
        public string Url
        {
            get; set;
        }

        public ImageFormat GetImageFormat()
        {
            if (string.IsNullOrEmpty(ContentType))
                return null;
            string it = ContentType.ToLower();
            if (it == ("image/jpeg"))
                return ImageFormat.Jpeg;
            else if (it == "image/png")
                return ImageFormat.Png;
            return null;
        }

        public static string GetContentTypeFromName(string fileName)
        {
            int pointIndex = fileName.LastIndexOf(".");
            if (pointIndex == -1)
                return null;

            string ext = fileName.Substring(pointIndex).ToLower();
            switch (ext)
            {
                case ".jpg": return "image/jpeg";
                case ".png": return "image/png";
                case ".mp4": return "video/mp4";
                case ".mp3": return "audio/mpeg";
                case ".mp4v": return "video/mp4";
            }
            return null;
        }
    }

    /// <summary>
    /// √ΩÃÂ¥Ê¥¢Œª÷√
    /// </summary>
    public enum MediaSaveLocation
    {
        Location = 0,
        OSS = 1
    }
}
