using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Core.Caching
{
    [AttributeUsage(AttributeTargets.Class, Inherited = true)]
    public class CacheAttribute : Attribute
    {
        public string CacheKey { get; set; }

        public string CacheListKey { get; set; }
    }
}
