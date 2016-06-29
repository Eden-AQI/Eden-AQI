using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Core
{
    public interface ICacheEntity
    {
        string GetPatternCachekey();

        string GetAllCacheKey();

        string Id { get; set; }
    }
}
