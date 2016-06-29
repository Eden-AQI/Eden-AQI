using Eden.Domain.Logging;
using Eden.Web.Console.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Eden.Core;
using Eden.Web.Framework;

namespace Eden.Web.Console.EntityWebServices
{
    public class LogResetService : BaseRestService<Log, LogViewModel>
    {
        public LogResetService() : base("log") { }

        protected override object CreateListModel(IPagedList<Log> source, int pageSize)
        {
            List<LogListModel> result = new List<LogListModel>();
            foreach (Log item in source)
            {
                LogListModel listModel = new LogListModel()
                {
                    Id = item.Id,
                    ShortMessage = item.ShortMessage,
                    Level = getLogLevelString(item.LogLevel),
                    EventTimeString = item.CreatedOnUtc.Value.ToString("yyyy-MM-dd HH:mm:ss"),
                    EventSourceString = item.EventSource == 0 ? "接口" : "控制台"
                };
                result.Add(listModel);
            }
            var gridModel = new DataSourceResult<LogListModel>(pageSize)
            {
                Data = result,
                Total = source.TotalCount,
                PageCount = source.TotalPages
            };
            return gridModel;
        }

        protected override void FillOtherInfo(LogViewModel model, Log entity)
        {
            model.LogLevelString = getLogLevelString(entity.LogLevel);
            model.EventTimeString = entity.CreatedOnUtc.Value.ToString("yyyy-MM-dd HH:mm:ss");
            model.EventSourceString = entity.EventSource == 0 ? "接口" : "控制台";
        }

        private string getLogLevelString(LogLevel level)
        {
            switch (level)
            {
                case LogLevel.Debug: return "调试";
                case LogLevel.Error: return "错误";
                case LogLevel.Fatal: return "致命";
                case LogLevel.Information: return "消息";
                case LogLevel.Warning: return "警告";
            }
            return string.Empty;
        }
    }
}