using Eden.Core.Infrastructure;
using Eden.ServicesDefine.Logging;
using System.Web.Mvc;
using Eden.Services.Logging;

namespace Eden.Web.Console
{
    public class GlobalExceptionAttribute : HandleErrorAttribute
    {
        public override void OnException(ExceptionContext filterContext)
        {
            var logger = EngineContext.Current.Resolve<ILogger>();

            var exceptioin = filterContext.Exception.InnerException;
            if (null == exceptioin)
                exceptioin = filterContext.Exception;

            logger.Error(exceptioin.Message, exceptioin, eventSource: Domain.Logging.EventSource.Console);

            filterContext.HttpContext.Response.Clear();
            filterContext.HttpContext.Response.StatusCode = 500;
            filterContext.HttpContext.Response.Write(filterContext.Exception.Message);
            filterContext.HttpContext.Response.End();

            base.OnException(filterContext);
        }
    }
}