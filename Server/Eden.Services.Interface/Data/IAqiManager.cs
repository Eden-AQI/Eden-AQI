using Eden.Domain.Data;
using Eden.Domain.Metadata;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.ServicesDefine.Data
{
    public interface IAqiManager
    {
        string GetSiteAqiJson(string stationCode);

        Dictionary<string, SiteData> BuildAllSiteRealTimeAqi();

        int[] GetSiteCurrentAqi(string stationCodes);

        string GetRankingData();

        void Update();

        List<Station> GetAllStation();

        SiteData GetSiteCurrentData(string stationCode);
    }
}
