using Eden.Core.Infrastructure;
using Eden.Core.Security;
using Eden.Domain.Configuration;
using Eden.Services.Configuration;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;

namespace Eden.Web.Console.Controllers
{
    public class CommandController : BaseController
    {

        // GET: Command
        public ActionResult ClearCache()
        {
            ISettingService settingService = EngineContext.Current.Resolve<ISettingService>();
            var systemSettings = settingService.LoadSetting<SystemSetting>();

            string appUrl = systemSettings.MobileApiAddress;

            string rs;
            using (var wc = new WebClient())
            {
                rs = wc.DownloadString(appUrl + "/Notification/ClearCache?password=" + Sp.GetCurrentPassword());
            }
            if (rs != "ok")
                throw new ApplicationException(rs);
            return Ok();
        }
    }
}