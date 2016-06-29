using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Web;
using System.Web.Http;
using System.Web.Http.Results;

namespace Eden.Web.Api.Controllers
{
    public class BaseApiController : ApiController
    {
        private static string _serverUrl = null;

        public string ServerUrl
        {
            get
            {
                if (null == _serverUrl)
                    _serverUrl = ConfigurationManager.AppSettings["server"];
                return _serverUrl;
            }
        }

        protected IHttpActionResult E = null;

        private OkEntity okEntity = new OkEntity()
        {
            Result = "Ok"
        };

        protected IHttpActionResult Allright()
        {
            return base.Ok(okEntity);
        }

        protected IHttpActionResult ModelError()
        {
            foreach (var item in ModelState.Values)
            {
                if (item.Errors.Count > 0)
                {
                    var e = item.Errors[0];
                    return BadRequest(string.IsNullOrEmpty(e.ErrorMessage) ? e.Exception.Message : e.ErrorMessage);
                }
            }
            return null;
        }

        protected IHttpActionResult ArgumentError()
        {
            return BadRequest("参数错误");
        }

        protected void CheckModel(object model)
        {
            if (!ModelState.IsValid)
            {
                E = ModelError();
            }
            if (null == model)
                E = ArgumentError();
            else
                E = null;
        }

        protected string UserId
        {
            get
            {
                return ActionContext.ActionArguments["userid"] as string;
            }
        }
    }

    public class OkEntity
    {
        public string Result;
    }
}