using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Domain.Data
{
    public class RankingParameterData
    {

        private List<RankingSiteData> _data = new List<RankingSiteData>();

        public string Parameter { get; set; }

        public List<RankingSiteData> Data { get { return _data; } set { _data = value; } }
    }

    public class RankingSiteData
    {
        public string SiteName { get; set; }

        public decimal Value { get; set; }
    }
}
