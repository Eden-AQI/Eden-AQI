using cn.jpush.api;
using cn.jpush.api.push.mode;
using Eden.Core.Infrastructure;
using Eden.ServicesDefine.Logging;
using Eden.ServicesDefine.Push;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Services
{
    public class JPush : IPushService
    {
        private static String app_key = "ac7b3986c97a69dff5abfd47";
        private static String master_secret = "0ee0cf6a989b941e68d17f8c";

        public bool Push(string type, string title, string platform, string message)
        {
            JPushClient client = new JPushClient(app_key, master_secret);

            PushPayload pushPayload = new PushPayload();
            platform = platform.ToLower();
            if ("1" == platform)
                pushPayload.platform = Platform.all();
            else if ("2" == platform)
                pushPayload.platform = Platform.ios();
            else if ("3" == platform)
                pushPayload.platform = Platform.android();

            pushPayload.audience = Audience.all();

            var notification = new Notification().setAlert(title);
            notification.IosNotification = new cn.jpush.api.push.notification.IosNotification();
            notification.IosNotification.AddExtra("type", type);
            notification.IosNotification.AddExtra("message", message);

            notification.AndroidNotification = new cn.jpush.api.push.notification.AndroidNotification();
            notification.AndroidNotification.AddExtra("type", type);
            notification.AndroidNotification.AddExtra("message", message);

            pushPayload.notification = notification;

            try
            {
                var result = client.SendPush(pushPayload);
                return true;
                ////由于统计数据并非非是即时的,所以等待一小段时间再执行下面的获取结果方法
                //System.Threading.Thread.Sleep(10000);
                ////如需查询上次推送结果执行下面的代码
                //var apiResult = client.getReceivedApi(result.msg_id.ToString());
                //var apiResultv3 = client.getReceivedApi_v3(result.msg_id.ToString());
                ////如需查询某个messageid的推送结果执行下面的代码
                //var queryResultWithV2 = client.getReceivedApi("1739302794");
                //var querResultWithV3 = client.getReceivedApi_v3("1739302794");
            }
            catch (Exception ex)
            {
                var logger = EngineContext.Current.Resolve<ILogger>();
                logger.InsertLog(Domain.Logging.LogLevel.Error, ex.Message, ex.StackTrace, Domain.Logging.EventSource.Console);
                //Console.WriteLine("Error response from JPush server. Should review and fix it. ");
                //Console.WriteLine("HTTP Status: " + e.Status);
                //Console.WriteLine("Error Code: " + e.ErrorCode);
                //Console.WriteLine("Error Message: " + e.ErrorMessage);
            }
            return false;
        }
    }
}
