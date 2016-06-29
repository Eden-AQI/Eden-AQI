using Eden.Core.Infrastructure;
using Eden.Domain.Logging;
using Eden.Services.Logging;
using Eden.ServicesDefine.Logging;
using System;
using System.IO;
using System.Net;
using System.Text;

namespace Eden.Services
{
    public class AqiWebRequest
    {
        public static string ReqUrl(string reqUrl, string method = "POST", string paramData = null)
        {
            var logger = EngineContext.Current.Resolve<ILogger>();
            try
            {
                HttpWebRequest request = WebRequest.Create(reqUrl) as HttpWebRequest;
                request.Method = method.ToUpperInvariant();

                if (request.Method.ToString() != "GET" && !string.IsNullOrEmpty(paramData) && paramData.Length > 0)
                {
                    request.ContentType = "application/x-www-form-urlencoded";
                    byte[] buffer = Encoding.UTF8.GetBytes(paramData);
                    request.ContentLength = buffer.Length;
                    request.GetRequestStream().Write(buffer, 0, buffer.Length);
                }

                using (HttpWebResponse resp = request.GetResponse() as HttpWebResponse)
                {
                    using (StreamReader stream = new StreamReader(resp.GetResponseStream(), Encoding.UTF8))
                    {
                        string result = stream.ReadToEnd();
                        if (result == "env003")
                            return null;
                        else if (result == "env002" || result == "env001" || result == "env000")
                        {
                            logger.InsertLog(LogLevel.Error, "调用接口失败", method + ":" + reqUrl + "\r\n" + (null != paramData ? paramData : ""), EventSource.Api, null);
                            return null;
                        }
                        return result;
                    }
                }
            }
            catch (Exception ex)
            {
                logger.InsertLog(LogLevel.Error, ex.Message, ex.ToString(), EventSource.Api, null);
                return null;
            }
        }
    }
}
