using Eden.Domain.Logging;
using Eden.Core.Infrastructure;
using Eden.ServicesDefine.Logging;
using Eden.Services.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Http.Filters;

namespace Eden.Web.Api
{
    public class GlobalExceptionFilter : ExceptionFilterAttribute
    {
        public override void OnException(HttpActionExecutedContext context)
        {
            try
            {
                var logger = EngineContext.Current.Resolve<ILogger>();
                logger.Error(context.Exception.Message, context.Exception, eventSource: EventSource.Api);
            }
            catch(Exception ex) {
                int a = 1;
            }
            base.OnException(context);
        }
    }
}