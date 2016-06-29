using Eden.Core.Infrastructure;
using Eden.Domain.Configuration;
using Eden.Domain.Data;
using Eden.Services.Configuration;
using Eden.ServicesDefine;
using Eden.Web.Api.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;

namespace Eden.Web.Api.Controllers
{
    /// <summary>
    /// 基础数据
    /// </summary>
    [RoutePrefix("Metadata")]
    public class MetadataController : BaseApiController
    {
        /// <summary>
        /// 污染等级
        /// </summary>
        /// <returns></returns>
        [Route("GradeInfo")]
        public IHttpActionResult GetGradeInfo()
        {
            IEntityService<AQIGrade> gradeService = EngineContext.Current.Resolve<IEntityService<AQIGrade>>();
            return Ok(gradeService.GetAll());
        }

        /// <summary>
        /// 获取版本信息
        /// </summary>
        /// <returns></returns>
        [Route("Version")]
        public IHttpActionResult GetVersion()
        {
            ISettingService settingService = EngineContext.Current.Resolve<ISettingService>();
            var vendorSettings = settingService.LoadSetting<VersionSetting>();
            return Ok(vendorSettings);
        }
    }
}