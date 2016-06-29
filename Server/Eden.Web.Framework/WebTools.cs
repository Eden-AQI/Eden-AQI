using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Web.Framework
{
    public class WebTools
    {
        public static void CopyProperties(object from, object to, bool excludeNull = true)
        {
            var fromPs = from.GetType().GetProperties();
            var toPs = to.GetType().GetProperties();
            foreach (var p in toPs)
            {
                var fromP = fromPs.Where(fp => fp.Name == p.Name).FirstOrDefault();
                if (null == fromP)
                    continue;
                object val = fromP.GetValue(from);
                if (excludeNull && null == val)
                    continue;
                p.SetValue(to, val);
            }
        }
    }
}
