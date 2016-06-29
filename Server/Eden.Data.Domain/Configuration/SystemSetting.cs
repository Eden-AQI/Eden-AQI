using Eden.Core.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Domain.Configuration
{
    public class SystemSetting : ISettings
    {
        public string MobileApiAddress { get; set; }

        public string DataApiAddress { get; set; }

        public string CityCode { get; set; }
    }
}
