using Eden.ServicesDefine.Sms;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Taobao.Top.Link.Endpoints;
using Top.Api;
using Top.Api.Request;
using Top.Api.Response;

namespace Eden.Thirdparty.Aliyun
{
    /// <summary>
    /// 阿里百川短消息服务
    /// </summary>
    public class TaobaoSmsSender : ISmsSender
    {
        //考虑到短信服务的不同实现，改配置可写死在程序集里。
        private static string APP_KEY = "23379260";
        private static string APP_SECRET = "a8e90e493c7508becf263975edf17c4a";

        public bool SendSmsValidateCode(string phoneNumber, string code)
        {
            ITopClient client = new DefaultTopClient("http://gw.api.taobao.com/router/rest", APP_KEY, APP_SECRET);
            //OpenSmsSendmsgRequest req = new OpenSmsSendmsgRequest();
            //req.SendMessageRequest = "{\"external_id\":\"wb" + APP_KEY + "\",\"template_id\":\"1212\",\"signature_id\":\"988\",\"mobile\":\"" + phoneNumber + "\",\"context\":{\"code\":\"" + code + "\"}}";
            //OpenSmsSendmsgResponse rsp = client.Execute(req);
            //return rsp.Body.Contains("<successful>true</successful>");

            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
            req.Extend = "";
            req.SmsType = "normal";
            req.SmsFreeSignName = "先知先觉";
            req.SmsParam = "{code:'" + code + "',time:'10分钟'}";
            req.RecNum = phoneNumber;
            req.SmsTemplateCode = "SMS_10245077";
            AlibabaAliqinFcSmsNumSendResponse rsp = client.Execute(req);
            if (rsp.IsError)
                return false;
            return rsp.Result.Success;
        }


    }
}
