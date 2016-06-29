using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace  Eden.Domain.Metadata
{
    /// <summary>
    /// 地点树形节点
    /// </summary>
    public class GeographicLocationTreeLink
    {
        /// <summary>
        /// 省/直辖市
        /// </summary>
        public GeographicLocation ProvinceNation {get;set;}
        /// <summary>
        /// 城市
        /// </summary>
        public GeographicLocation CityNation {get;set;}
        /// <summary>
        /// 地区
        /// </summary>
        public GeographicLocation DistrictNation {get;set;}

        /// <summary>
        /// 是否有：省/直辖市
        /// </summary>
        public bool HasProvince
        {
            get
            {
                return ProvinceNation != null;
            }
        }

        /// <summary>
        /// 是否有：城市
        /// </summary>
        public bool HasCity
        {
            get
            {
                return CityNation != null;
            }
        }
        /// <summary>
        /// 是否有：地区
        /// </summary>
        public bool HasDistrict
        {
            get
            {
                return DistrictNation != null;
            }
        }

        /// <summary>
        /// 基准地点
        /// </summary>
        public GeographicLocation BaseNation {get;set;}
        /// <summary>
        /// 数量
        /// </summary>
        public int NationCount
        {
            get
            {
                int c = 0;
                if (ProvinceNation != null)
                    c++;
                if (CityNation != null)
                    c++;
                if (DistrictNation != null)
                    c++;
                return c;
            }
        }

        public GeographicLocationTreeLink(GeographicLocation baseNation)
        {
            this.BaseNation = baseNation;
        }

        public GeographicLocationTreeLink(GeographicLocation baseNation, IList<GeographicLocation> nationLinks)
        {
            this.BaseNation = baseNation;
            Init(nationLinks);
        }

        public bool Init(IList<GeographicLocation> nationLinks)
        {
            if (nationLinks == null || nationLinks.Count < 1)
                return false;
            ProvinceNation = null;
            CityNation = null;
            DistrictNation = null;

            if (nationLinks.Count >= 3)
            {
                ProvinceNation = nationLinks[2];
                CityNation = nationLinks[1];
                DistrictNation = nationLinks[0];
            }
            else if (nationLinks.Count == 2)
            {
                ProvinceNation = nationLinks[1];
                if (!string.IsNullOrEmpty(nationLinks[0].City))
                {
                    CityNation = nationLinks[0];
                }
                else if (!string.IsNullOrEmpty(nationLinks[0].District))
                {
                    DistrictNation = nationLinks[0];
                }
            }
            else if (nationLinks.Count == 1)
            {
                ProvinceNation = nationLinks[0];
            }
            return true;
        }
    }
}
