using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Mvc;

namespace Eden.Web.Framework
{
    public class BadResponse : ActionResult
    {
        private string _response;

        public BadResponse(string response)
        {
            _response = response;
        }

        public override void ExecuteResult(ControllerContext context)
        {
            context.HttpContext.Response.StatusCode = 400;
            context.HttpContext.Response.Write(_response);
        }
    }
}
