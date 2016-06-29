using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Core.Security
{
    public class Sp
    {
        private static string pwd = null;

        public static string GetCurrentPassword()
        {
            if (null == pwd)
                pwd = ConfigurationManager.AppSettings["system-password"];
            string key = DateTime.Now.ToString("yyyy-MM-dd-HH:mm") + "-sxg-" + pwd;

            MD5CryptoServiceProvider md5 = new MD5CryptoServiceProvider();
            byte[] InBytes = Encoding.GetEncoding("GB2312").GetBytes(key);
            byte[] OutBytes = md5.ComputeHash(InBytes);
            string OutString = "";
            for (int i = 0; i < OutBytes.Length; i++)
            {
                OutString += OutBytes[i].ToString("x2");
            }
            return OutString;
        }

    }
}
