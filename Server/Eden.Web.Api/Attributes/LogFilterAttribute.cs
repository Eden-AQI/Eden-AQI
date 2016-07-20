using Eden.Core.Infrastructure;
using Eden.Domain.Logging;
using Eden.ServicesDefine;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http.Controllers;
using System.Web.Http.Filters;

namespace Eden.Web.Api.Attributes
{
    public class LogFilterAttribute : ActionFilterAttribute
    {
        public override void OnActionExecuting(HttpActionContext actionContext)
        {
            var entityService = EngineContext.Current.Resolve<IEntityService<RequestLog>>();
            entityService.Insert(new RequestLog() { EventTime = DateTime.Now, IpAddress = HttpContext.Current.Request.UserHostAddress, Detail = actionContext.Request.ToString(), RequestUrl = actionContext.Request.RequestUri.ToString() });
            base.OnActionExecuting(actionContext);
        }
    }
}