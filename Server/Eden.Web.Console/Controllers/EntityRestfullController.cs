using Eden.Web.Console.EntityWebServices;
using System.Collections.Generic;
using System.Web.Mvc;

namespace Eden.Web.Console.Controllers
{

    public class EntityRestfullController : BaseController
    {
        private Dictionary<string, IRestService> resetServices = new Dictionary<string, IRestService>()
        {
            { "device",new DeviceRestService()},
            { "push",new PushRestService()},
            { "log",new LogResetService()},
            { "task",new TaskRestService()},
            { "config",new SettingRestService() },
            { "requestlog",new RequestLogRestService() }
        };

        [HttpGet]
        public ActionResult Get()
        {
            IRestService rs = GetResetService();
            return rs.Get(int.Parse(Request["id"]));
        }

        [HttpPost]
        public ActionResult Search()
        {
            IRestService rs = GetResetService();
            return rs.Search();
        }

        [HttpPost]
        public ActionResult Delete(FormCollection form)
        {
            IRestService rs = GetResetService();
            string id = Request["id"];
            return rs.Delete(int.Parse(id));
        }

        [HttpPost]
        public ActionResult Put(FormCollection form)
        {
            IRestService rs = GetResetService();
            return rs.Put(form);
        }

        private IRestService GetResetService()
        {
            string entity = Request["entity"];
            IRestService rs = resetServices[entity];
            rs.ControllerContext = ControllerContext;
            return rs;
        }

    }
}