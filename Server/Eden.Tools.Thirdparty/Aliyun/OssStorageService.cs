
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Configuration;
using Aliyun.OpenServices.OpenStorageService;
using Eden.ServicesDefine.Media;
using Eden.ServicesDefine.Logging;
using Eden.Domain.Logging;

namespace Eden.Thirdparty.Aliyun
{
    public class OssStorageService : IResourceStorageService
    {

        private static string ALIYUN_ACCESS_KEY_ID;
        private static string ALIYUN_ACCESS_KEY_SECRET;
        private static string OSS_ENDPOINT;
        private static string OSS_BUCKETNAME;
        private static string OSS_ACCESSBASEURL;

        private static string RESOURCE_LOCALPATH;

        private readonly ILogger _logger;

        static OssStorageService()
        {
            ALIYUN_ACCESS_KEY_ID = ConfigurationManager.AppSettings["aliyun-accessKeyID"];
            ALIYUN_ACCESS_KEY_SECRET = ConfigurationManager.AppSettings["aliyun-accessKeySecret"];
            OSS_ENDPOINT = ConfigurationManager.AppSettings["oss-endpoint"];
            OSS_BUCKETNAME = ConfigurationManager.AppSettings["oss-bucketName"];
            OSS_ACCESSBASEURL = ConfigurationManager.AppSettings["oss-accessbaseurl"];
            RESOURCE_LOCALPATH = ConfigurationManager.AppSettings["resource-local"];
        }

        public OssStorageService(ILogger logger)
        {
            _logger = logger;
        }

        public string GetPathFromLocal(string name, string relativePath)
        {
            return checkPath(RESOURCE_LOCALPATH) + checkPath(relativePath) + name;
        }

        public string GetUrlFromNetwork(string name, string relativePath)
        {
            return checkPath(OSS_ACCESSBASEURL) + checkPath(relativePath) + name;
        }

        public bool UploadToNetwork(string name, string relativePath, Stream stream, string contentType)
        {
            try
            {
                if (!string.IsNullOrEmpty(relativePath))
                {
                    //尝试创建虚拟路径
                    relativePath = tryCreateVirtalPath(relativePath);
                }
                OssClient client = new OssClient(OSS_ENDPOINT, ALIYUN_ACCESS_KEY_ID, ALIYUN_ACCESS_KEY_SECRET);
                var metadata = new ObjectMetadata();
                metadata.CacheControl = "No-Cache";
                metadata.ContentType = contentType;
                metadata.ContentLength = stream.Length;

                PutObjectResult result = client.PutObject(OSS_BUCKETNAME, relativePath + name, stream, metadata);
                return true;
            }
            catch (Exception ex)
            {
                _logger.InsertLog(LogLevel.Error, ex.Message, ex.ToString());
                return false;
            }
        }

        private string tryCreateVirtalPath(string path)
        {
            try
            {
                OssClient client = new OssClient(OSS_ENDPOINT, ALIYUN_ACCESS_KEY_ID, ALIYUN_ACCESS_KEY_SECRET);
                path = checkPath(path);
                using (var stream = new MemoryStream())
                {
                    client.PutObject(OSS_BUCKETNAME, path, stream);
                }
            }
            catch { }
            return path;
        }

        public bool UploadToLocal(string name, string relativePath, Stream stream)
        {
            try
            {
                string savePath = checkPath(RESOURCE_LOCALPATH) + checkPath(relativePath);
                if (!Directory.Exists(savePath))
                    Directory.CreateDirectory(savePath);
                using (FileStream fs = new FileStream(savePath + name, FileMode.Create))
                {
                    byte[] buffer = new byte[256];
                    int count = stream.Read(buffer, 0, buffer.Length);
                    while (count != 0)
                    {
                        fs.Write(buffer, 0, count);
                        count = stream.Read(buffer, 0, buffer.Length);
                    }
                }
                return true;
            }
            catch (Exception ex)
            {
                _logger.InsertLog(LogLevel.Error, ex.Message, ex.ToString());
                return false;
            }
        }

        private string checkPath(string p)
        {
            if (string.IsNullOrEmpty(p))
                return String.Empty;
            char lastChar = p[p.Length - 1];
            p = p.Replace('\\', '/');
            if (lastChar != '/' && lastChar != '\\')
            {
                p += "/";
            }
            return p;
        }

        public void DeleteFromLoacal(string name, string relativePath)
        {
            string savePath = checkPath(RESOURCE_LOCALPATH) + checkPath(relativePath);
            savePath = savePath + name;
            if (File.Exists(savePath))
                File.Delete(savePath);
        }

        public void DeleteFromNetwork(string name, string relativePath)
        {
            string savePath = checkPath(relativePath);
            savePath = savePath + name;
            OssClient client = new OssClient(OSS_ENDPOINT, ALIYUN_ACCESS_KEY_ID, ALIYUN_ACCESS_KEY_SECRET);
            client.DeleteObject(OSS_BUCKETNAME, savePath);
        }
    }
}
