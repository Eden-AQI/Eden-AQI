using Eden.Web.Framework;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.Mvc;

namespace Eden.Web.Console.Controllers
{
    [Authorize]
    public class BaseController : Controller
    {
        public virtual string RenderPartialViewToString(string viewName, object model)
        {
            //Original source code: http://craftycodeblog.com/2010/05/15/asp-net-mvc-render-partial-view-to-string/
            if (string.IsNullOrEmpty(viewName))
                viewName = this.ControllerContext.RouteData.GetRequiredString("action");

            this.ViewData.Model = model;

            using (var sw = new StringWriter())
            {
                ViewEngineResult viewResult = System.Web.Mvc.ViewEngines.Engines.FindPartialView(this.ControllerContext, viewName);
                var viewContext = new ViewContext(this.ControllerContext, viewResult.View, this.ViewData, this.TempData, sw);
                viewResult.View.Render(viewContext, sw);

                return sw.GetStringBuilder().ToString();
            }
        }

        protected ActionResult BadArguments()
        {
            StringBuilder sbText = new StringBuilder();
            ModelState.Values.Where(m => m.Errors.Count > 0).ToList().ForEach(t =>
            {
                t.Errors.ToList().ForEach(e =>
                {
                    sbText.Append(e.ErrorMessage + "\n");
                });
            });
            return new BadResponse("参数错误\n" + sbText.ToString());
        }

        protected ActionResult Error(string message)
        {
            return new BadResponse(message);
        }

        protected ActionResult Ok(string response = "")
        {
            return new ContentResult() { Content = response };
        }
    }
}