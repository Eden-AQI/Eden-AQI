using Eden.ServicesDefine.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Eden.Domain.Data;
using Eden.Core.Data;
using Eden.Core.Caching;
using Eden.Data;
using Eden.ServicesDefine.Metadata;

namespace Eden.Services.Metadata
{
    public class AqiGradeService : DefaultEntityService<AQIGrade>, IAqiGradeService
    {
        public AqiGradeService(IRepository<AQIGrade> repository, ICacheManager cacheManager, IDbContext context, IDataProvider dataProvider) :
            base(repository, cacheManager, context, dataProvider)
        {
        }

        public AQIGrade CalcGrade(int aqi)
        {
            var all = GetAll().OrderBy(g => g.AQIMin);
            foreach (var g in all)
            {
                if (aqi >= g.AQIMin && aqi <= g.AQIMax)
                    return g;
            }
            return all.ElementAt(all.Count() - 1);
        }
    }
}
