using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(Eden.Web.Console.Startup))]
namespace Eden.Web.Console
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
