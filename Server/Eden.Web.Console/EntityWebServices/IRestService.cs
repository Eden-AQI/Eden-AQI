using Eden.Web.Console.EntityWebServices;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace Eden.Web.Console.EntityWebServices
{

    public interface IRestService
    {
        ActionResult Delete(int id);

        ActionResult Search();

        ActionResult Get(int id);

        ActionResult Put(FormCollection form);

        ControllerContext ControllerContext { get; set; }
    }
}