using Eden.Core.Infrastructure;
using Eden.ServicesDefine.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace Eden.Web.Api.Controllers
{
    public class ShareController : Controller
    {
        // GET: Share
        public ActionResult Index(string stationCode)
        {
            if (string.IsNullOrEmpty(stationCode))
                return View(new Domain.Data.SiteData());
            var aqiManager = EngineContext.Current.Resolve<IAqiManager>();
            var data = aqiManager.GetSiteCurrentData(stationCode);
            if (null == data)
                data = new Domain.Data.SiteData();
            
            return View(data);
        }
    }
}