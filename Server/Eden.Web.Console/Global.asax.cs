using Eden.Core.Data;
using Eden.Core.Infrastructure;
using Eden.ServicesDefine.Logging;
using System.Web.Mvc;
using System.Web.Optimization;
using System.Web.Routing;
using Eden.Services.Logging;
using Eden.Domain.Logging;
using AutoMapper;
using Eden.Web.Console.Infrastructure;
using Eden.Services.Tasks;

namespace Eden.Web.Console
{
    public class MvcApplication : System.Web.HttpApplication
    {
        protected void Application_Start()
        {
            AreaRegistration.RegisterAllAreas();
            FilterConfig.RegisterGlobalFilters(GlobalFilters.Filters);
            RouteConfig.RegisterRoutes(RouteTable.Routes);
            BundleConfig.RegisterBundles(BundleTable.Bundles);

            EngineContext.Initialize(false);
            
            bool databaseInstalled = DataSettingsHelper.DatabaseIsInstalled();
            if (databaseInstalled)
            {
                try
                {
                    //log
                    var logger = EngineContext.Current.Resolve<ILogger>();
                    logger.Information("Application started", eventSource: EventSource.Console);
                }
                catch
                {
                    //don't throw new exception if occurs
                }
                if (databaseInstalled)
                {
                    
                }
                TaskManager.Instance.Initialize();
                TaskManager.Instance.Start();
            }
        }
    }
}
