using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Services.Utility
{
    public class DecimalUtility
    {
        public static bool IsNumber(params string[] nums)
        {
            decimal d = 0;
            foreach (string s in nums)
            {
                if (!decimal.TryParse(s, out d))
                    return false;
            }
            return true;
        }
    }
}
