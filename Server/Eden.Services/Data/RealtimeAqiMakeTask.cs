using Eden.Core.Infrastructure;
using Eden.Core.Security;
using Eden.Domain.Configuration;
using Eden.Services.Configuration;
using Eden.Services.Tasks;
using System;
using System.Net;

namespace Eden.Services.Data
{
    public class RealtimeAqiMakeTask : ITask
    {
        public RealtimeAqiMakeTask()
        {

        }

        public void Execute()
        {
            ISettingService settingService = EngineContext.Current.Resolve<ISettingService>();
            var systemSettings = settingService.LoadSetting<SystemSetting>();

            string appUrl = systemSettings.MobileApiAddress;

            string rs;
            using (var wc = new WebClient())
            {
                rs = wc.DownloadString(appUrl + "/Notification/ClearCache?password=" + Sp.GetCurrentPassword());
            }
            if (rs != "ok")
                throw new ApplicationException(rs);

        }
    }
}