using Eden.Domain.Configuration;
using Eden.Services.Configuration;
using Eden.Web.Console.EntityWebServices;
using Eden.Web.Console.Models;
using Eden.Web.Framework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace Eden.Web.Console.Controllers
{
    public class SettingController : BaseController
    {
        private readonly ISettingService _settingService;

        public SettingController(ISettingService settingService)
        {
            _settingService = settingService;
        }

        public ActionResult Version()
        {
            var vendorSettings = _settingService.LoadSetting<VersionSetting>();
            return new JsonResult() { Data = vendorSettings, JsonRequestBehavior = JsonRequestBehavior.AllowGet };
        }

        [HttpPost]
        public ActionResult Version(VersionSettingModel model)
        {
            VersionSetting entity = new VersionSetting();
            WebTools.CopyProperties(model, entity);
            entity.Mandatory = model.MandatoryStr == "on";
            _settingService.SaveSetting(entity, v => v.VersionCode, false);
            _settingService.SaveSetting(entity, v => v.VersionName, false);
            _settingService.SaveSetting(entity, v => v.UpdateTime, false);
            _settingService.SaveSetting(entity, v => v.IosDownloadUrl, false);
            _settingService.SaveSetting(entity, v => v.Mandatory, false);
            _settingService.SaveSetting(entity, v => v.DownloadUrl, false);
            _settingService.SaveSetting(entity, v => v.Description, false);
            _settingService.ClearCache();
            return Ok();
        }

        public ActionResult System()
        {
            var systemSettings = _settingService.LoadSetting<SystemSetting>();
            return new JsonResult() { Data = systemSettings, JsonRequestBehavior = JsonRequestBehavior.AllowGet };
        }

        [HttpPost]
        public ActionResult System(SystemSetting model)
        {
            _settingService.SaveSetting(model, v => v.DataApiAddress, false);
            _settingService.SaveSetting(model, v => v.MobileApiAddress, false);
            _settingService.SaveSetting(model, v => v.CityCode, false);
            _settingService.ClearCache();
            return Ok();
        }

    }
}