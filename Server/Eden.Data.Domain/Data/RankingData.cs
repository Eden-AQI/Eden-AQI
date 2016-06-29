using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Domain.Data
{
    public class RankingData
    {
        private List<RankingParameterData> _items = new List<RankingParameterData>();

        public string Duration { get; set; }

        public List<RankingParameterData> Items { get { return _items; } }
    }
}
