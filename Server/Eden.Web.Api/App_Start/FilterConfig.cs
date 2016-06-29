using Eden.Web.Api.Attributes;
using System.Web;
using System.Web.Mvc;

namespace Eden.Web.Api
{
    public class FilterConfig
    {
        public static void RegisterGlobalFilters(GlobalFilterCollection filters)
        {
            filters.Add(new HandleErrorAttribute());
        }
    }
}
