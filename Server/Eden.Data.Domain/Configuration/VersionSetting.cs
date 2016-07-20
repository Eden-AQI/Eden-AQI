using Eden.Core.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Domain.Configuration
{
    public class VersionSetting : ISettings
    {
        public string VersionName { get; set; }

        public string VersionCode { get; set; }

        public string Description { get; set; }

        public string UpdateTime { get; set; }

        public string DownloadUrl { get; set; }

        public string IosDownloadUrl { get; set; }

        public bool Mandatory { get; set; }
    }
}
