using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Domain.Data
{
    public class ForecastItem
    {
        public string Time { get; set; }

        public string AqiLevel { get; set; }

        public string Aqi { get; set; }

        public string PrimaryParameter { get; set; }

        public string Weather { get; set; }

        public string Temperature { get; set; }

    }
}
