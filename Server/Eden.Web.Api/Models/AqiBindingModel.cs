using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace Eden.Web.Api.Models
{

    public class SiteGroup
    {

        private List<SiteModel> items = new List<SiteModel>();

        public string GroupName { get; set; }

        public List<SiteModel> Items { get { return items; } }
    }

    public class SiteModel
    {
        public string Id { get; set; }

        public string Name { get; set; }

        public int Aqi { get; set; }

        public string BackgroundImageUrl { get; set; }

        public string Group { get; set; }

        public decimal Latitude { get; set; }

        public decimal Longitude { get; set; }

        public int Grade { get; set; }
    }

    public class SiteAqi
    {
        public string SiteId { get; set; }

        public int Aqi { get; set; }
    }

    public class StationId
    {
        [Required]
        public string Id { get; set; }
    }

    public class NotifyViewModel
    {
        public int Level { get; set; }
        public string Message { get; set; }
    }
}