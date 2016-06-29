using Eden.Core.Caching;
using Eden.Core.Infrastructure;
using Eden.Core.Security;
using Eden.ServicesDefine.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace Eden.Web.Api.Controllers
{
    public class NotificationController : Controller
    {

        public string ClearCache(string password)
        {
            string pwd = Sp.GetCurrentPassword();
            if (password != pwd)
            {
                return "密码错误";
            }
            else
            {
                var cacheManager = new MemoryCacheManager();
                cacheManager.Clear();
                return "ok";
            }
        }

        public string UpdateData(string password)
        {
            string pwd = Sp.GetCurrentPassword();
            if (password != pwd)
            {
                return "密码错误";
            }
            else
            {
                var aqiManager = EngineContext.Current.Resolve<IAqiManager>();
                aqiManager.Update();
                return "ok";
            }
        }
    }
}