using Eden.Web.Api.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Formatting;
using System.Web;
using System.Web.Http.Filters;
using System.Web.Mvc;

namespace Eden.Web.Api.Attributes
{
    public class GloabalErrorAttribute : ExceptionFilterAttribute
    {
        public override void OnException(HttpActionExecutedContext actionExecutedContext)
        {
            string exceptionStr = actionExecutedContext.Exception.Message;
            HttpStatusCode statusCode = HttpStatusCode.InternalServerError;
            ObjectContent<GlobalExceptionModel> oc = new ObjectContent<GlobalExceptionModel>(new GlobalExceptionModel() { Message = exceptionStr }, new JsonMediaTypeFormatter());
            actionExecutedContext.Response = new HttpResponseMessage(statusCode) { Content = oc };
        }

    }
}