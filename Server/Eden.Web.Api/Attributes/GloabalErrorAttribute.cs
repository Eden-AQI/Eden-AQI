using Eden.Core.Infrastructure;
using Eden.Domain.Logging;
using Eden.Services.Logging;
using Eden.ServicesDefine.Logging;
using Eden.Web.Api.Models;
using System.Net;
using System.Net.Http;
using System.Net.Http.Formatting;
using System.Web.Http.Filters;

namespace Eden.Web.Api.Attributes
{
    public class GloabalErrorAttribute : ExceptionFilterAttribute
    {
        public override void OnException(HttpActionExecutedContext actionExecutedContext)
        {
            try
            {
                var logger = EngineContext.Current.Resolve<ILogger>();
                var exceptioin = actionExecutedContext.Exception.InnerException;
                if (null == exceptioin)
                    exceptioin = actionExecutedContext.Exception;
                logger.Error(exceptioin.Message, exceptioin, eventSource: EventSource.Api);
            }
            catch 
            {
                
            }
            string exceptionStr = actionExecutedContext.Exception.Message;
            HttpStatusCode statusCode = HttpStatusCode.InternalServerError;
            ObjectContent<GlobalExceptionModel> oc = new ObjectContent<GlobalExceptionModel>(new GlobalExceptionModel() { Message = exceptionStr }, new JsonMediaTypeFormatter());
            actionExecutedContext.Response = new HttpResponseMessage(statusCode) { Content = oc };
        }

    }
}