using Autofac;
using Autofac.Integration.Mvc;
using Eden.Core;
using Eden.Core.Caching;
using Eden.Core.Data;
using Eden.Core.Infrastructure;
using Eden.Core.Infrastructure.DependencyManagement;
using Eden.Data;
using Eden.Services;

using Eden.ServicesDefine;

using Eden.ServicesDefine.Logging;

using Eden.Services.Logging;

using Eden.Services.Tasks;
using System.Linq;
using System.Web;


using Eden.ServicesDefine.Data;
using Eden.Services.Data;
using Eden.Services.Metadata;
using Eden.ServicesDefine.Metadata;
using Eden.ServicesDefine.Customers;
using Eden.Services.Customers;
using Eden.Services.Configuration;

namespace Eden.Web.Framework
{
    public class DependencyRegistrar : IDependencyRegistrar
    {
        public int Order
        {
            get
            {
                return 0;
            }
        }

        public virtual void Register(ContainerBuilder builder, ITypeFinder typeFinder)
        {

            //HTTP context and other related stuff
            builder.Register(c =>
                new HttpContextWrapper(HttpContext.Current) as HttpContextBase)
                .As<HttpContextBase>()
                .InstancePerLifetimeScope();
            builder.Register(c => c.Resolve<HttpContextBase>().Request)
                .As<HttpRequestBase>()
                .InstancePerLifetimeScope();
            builder.Register(c => c.Resolve<HttpContextBase>().Response)
                .As<HttpResponseBase>()
                .InstancePerLifetimeScope();
            builder.Register(c => c.Resolve<HttpContextBase>().Server)
                .As<HttpServerUtilityBase>()
                .InstancePerLifetimeScope();
            builder.Register(c => c.Resolve<HttpContextBase>().Session)
                .As<HttpSessionStateBase>()
                .InstancePerLifetimeScope();

            //web helper
            builder.RegisterType<WebHelper>().As<IWebHelper>().InstancePerLifetimeScope();
            //user agent helper

            //controllers
            builder.RegisterControllers(typeFinder.GetAssemblies().ToArray());

            //data layer
            var dataSettingsManager = new DataSettingsManager();
            var dataProviderSettings = dataSettingsManager.LoadSettings();
            builder.Register(c => dataSettingsManager.LoadSettings()).As<DataSettings>();
            builder.Register(x => new EfDataProviderManager(x.Resolve<DataSettings>())).As<BaseDataProviderManager>().InstancePerDependency();

            builder.Register(x => x.Resolve<BaseDataProviderManager>().LoadDataProvider()).As<IDataProvider>().InstancePerDependency();

            if (dataProviderSettings != null && dataProviderSettings.IsValid())
            {
                var efDataProviderManager = new EfDataProviderManager(dataSettingsManager.LoadSettings());
                var dataProvider = efDataProviderManager.LoadDataProvider();
                dataProvider.InitConnectionFactory();

                builder.Register<IDbContext>(c => new EdenObjectContext(dataProviderSettings.DataConnectionString)).InstancePerLifetimeScope();
            }
            else
            {
                builder.Register<IDbContext>(c => new EdenObjectContext(dataSettingsManager.LoadSettings().DataConnectionString)).InstancePerLifetimeScope();
            }

            builder.RegisterGeneric(typeof(EfRepository<>)).As(typeof(IRepository<>)).InstancePerLifetimeScope();

            builder.RegisterGeneric(typeof(DefaultEntityService<>)).As(typeof(IEntityService<>)).InstancePerLifetimeScope();

            //cache manager
            //builder.RegisterType<MemoryCacheManager>().As<ICacheManager>().Named<ICacheManager>("sxg_cache_static").SingleInstance();
            //builder.RegisterType<PerRequestCacheManager>().As<ICacheManager>().Named<ICacheManager>("sxg_cache_per_request").InstancePerLifetimeScope();
            builder.RegisterType<MemoryCacheManager>().As<ICacheManager>().SingleInstance();
            builder.RegisterType<SettingService>().As<ISettingService>().InstancePerLifetimeScope();

            //work context
            builder.RegisterType<WebWorkContext>().As<IWorkContext>().InstancePerLifetimeScope();
            builder.RegisterType<DefaultLogger>().As<ILogger>().InstancePerLifetimeScope();


            //task
            builder.RegisterType<ScheduleTaskService>().As<IScheduleTaskService>().InstancePerLifetimeScope();

            builder.RegisterType<AqiGradeService>().As<IAqiGradeService>().InstancePerLifetimeScope();
            builder.RegisterType<RestAqiManager>().As<IAqiManager>().InstancePerLifetimeScope();

            builder.RegisterType<SiteService>().As<ISiteService>().InstancePerLifetimeScope();

            builder.RegisterType<DeviceService>().As<IDeviceService>().InstancePerLifetimeScope();
        }
    }
}
