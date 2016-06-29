using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Thirdparty.Biadu
{
    public class MapTools
    {
        /// <summary>
        /// 计算两个点之间的距离
        /// </summary>
        /// <param name="lat_a"></param>
        /// <param name="lng_a"></param>
        /// <param name="lat_b"></param>
        /// <param name="lng_b"></param>
        /// <returns></returns>
        public static double CalcDistanceFromXtoY(double lat_a, double lng_a, double lat_b, double lng_b)
        {
            double pk = 180 / 3.14159;
            double a1 = lat_a / pk;
            double a2 = lng_a / pk;
            double b1 = lat_b / pk;
            double b2 = lng_b / pk;
            double t1 = Math.Cos(a1) * Math.Cos(a2) * Math.Cos(b1) * Math.Cos(b2);
            double t2 = Math.Cos(a1) * Math.Sin(a2) * Math.Cos(b1) * Math.Sin(b2);
            double t3 = Math.Sin(a1) * Math.Sin(b1);
            double tt = Math.Acos(t1 + t2 + t3);
            return 6371000 * tt;
        }

        /// <summary>
        /// 获取距离字符串
        /// </summary>
        /// <param name="distance"></param>
        /// <returns></returns>
        public static string GetDistanceString(double distance)
        {
            if (distance > 1000)
                return new decimal(Math.Round(distance / 1000, 1)) + "km";
            else
                return new decimal(distance) + "m";
        }
    }
}
