using Eden.Core.Infrastructure;
using Eden.Domain.Metadata;
using Eden.ServicesDefine;
using Eden.ServicesDefine.Data;
using Eden.ServicesDefine.Metadata;
using Eden.Web.Api.Infrastructure;
using Eden.Web.Api.Models;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Web;
using System.Web.Http;

namespace Eden.Web.Api.Controllers
{
    /// <summary>
    /// AQI相关
    /// </summary>
    [RoutePrefix("Aqi")]
    public class AqiController : BaseApiController
    {

        /// <summary>
        /// 获取实时数据
        /// </summary>
        /// <param name="siteId">站点编号，如果传入0，则返回郑州市的实时空气质量</param>
        /// <returns></returns>
        [Route("GetRealtime")]
        public IHttpActionResult GetSiteAqi(string siteId)
        {
            if (string.IsNullOrEmpty(siteId))
                siteId = "0";
            IAqiManager aqiManager = EngineContext.Current.Resolve<IAqiManager>();
            string json = aqiManager.GetSiteAqiJson(siteId);
            return new JsonStringActoinResult(json, this.Request);
        }


        /// <summary>
        /// 获取背景图片
        /// </summary>
        /// <param name="siteId"></param>
        /// <returns></returns>
        [Route("BackgroundImage")]
        [HttpGet]
        public HttpResponseMessage GetBackgroundImage(int siteId)
        {
            HttpResponseMessage result = new HttpResponseMessage(HttpStatusCode.OK);
            string filePath = HttpContext.Current.Server.MapPath("~/background.jpg");

            Image img = Image.FromFile(filePath);
            MemoryStream ms = new MemoryStream();
            img.Save(ms, System.Drawing.Imaging.ImageFormat.Jpeg);
            result.Content = new ByteArrayContent(ms.ToArray());
            result.Content.Headers.ContentType = new MediaTypeHeaderValue("image/jpeg");
            img.Dispose();
            ms.Close();
            return result;
        }

        /// <summary>
        /// 获取站点列表
        /// </summary>
        /// <returns></returns>
        [Route("GetCityList")]
        public IHttpActionResult GetSiteList()
        {
            //ISiteService siteService = EngineContext.Current.Resolve<ISiteService>();
            IAqiManager aqiManager = EngineContext.Current.Resolve<IAqiManager>();
            IAqiGradeService gradeService = EngineContext.Current.Resolve<IAqiGradeService>();

            var allSite = aqiManager.GetAllStation();

            List<SiteGroup> gp = new List<SiteGroup>();

            List<SiteModel> result = new List<SiteModel>();
            foreach (var site in allSite)
            {

                SiteGroup g = gp.Where(p => p.GroupName == site.FirstPinyin).FirstOrDefault();
                if (null == g)
                {
                    g = new SiteGroup() { GroupName = site.FirstPinyin };
                    gp.Add(g);
                }
                SiteModel sm = new SiteModel();
                sm.Id = site.StationCode;
                sm.Name = site.StationName;
                sm.Group = site.FirstPinyin;
                sm.Latitude = site.Latitude;
                sm.Longitude = site.Longitude;
                sm.Aqi = aqiManager.GetSiteCurrentAqi(site.StationCode)[0];
                sm.Grade = gradeService.CalcGrade(sm.Aqi).Grade;
                g.Items.Add(sm);
            }
            return Ok(gp.OrderBy(g => g.GroupName).ToList());
        }

        /// <summary>
        /// 获取排行数据
        /// </summary>
        [Route("GetRankingData")]
        public IHttpActionResult GetRankingData()
        {
            IAqiManager aqiManager = EngineContext.Current.Resolve<IAqiManager>();
            string json = aqiManager.GetRankingData();
            return new JsonStringActoinResult(json, this.Request);
        }

        /// <summary>
        /// 查询指定站点的实时AQI
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        [Route("GetSiteCurrentAqi")]
        public IHttpActionResult GetSiteCurrentAqi(StationId sites)
        {
            string[] siteIds = sites.Id.Split(',');
            List<SiteAqi> rs = new List<SiteAqi>();
            IAqiManager aqiManager = EngineContext.Current.Resolve<IAqiManager>();
            var aqis = aqiManager.GetSiteCurrentAqi(sites.Id);
            for (int i = 0; i < siteIds.Length; i++)
            {
                rs.Add(new SiteAqi() { SiteId = siteIds[i], Aqi = aqis[i] });
            }
            return Ok(rs);
        }

        /// <summary>
        /// 获取通知列表
        /// </summary>
        /// <returns></returns>
        [Route("GetNotifyList")]
        public IHttpActionResult GetNotifyList()
        {
            var service = EngineContext.Current.Resolve<INotifyService>();
            var source = service.GetCurrentNotifyList();
            List<NotifyViewModel> result = new List<NotifyViewModel>();
            foreach (var n in source)
            {
                result.Add(new NotifyViewModel()
                {
                    Level = n.Level,
                    Message = n.Message
                });
            }
            return Ok(result);
        }

        //[Route("Test")]
        //[HttpGet]
        //public IHttpActionResult Test()
        //{
        //    var aq = EngineContext.Current.Resolve<IAqiManager>();
        //    string json = aq.GetSiteAqiJson("410100004");
        //    return Ok(json);
        //}
    }
}