using Eden.Core;
using Eden.Domain.Logging;
using Eden.Web.Console.Models;
using Eden.Web.Framework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Eden.Web.Console.EntityWebServices
{
    public class RequestLogRestService : BaseRestService<RequestLog, RequestLogViewModel>
    {
        public RequestLogRestService() : base("requestlog") { }

        protected override object CreateListModel(IPagedList<RequestLog> source, int pageSize)
        {
            List<RequestLogViewModel> result = new List<RequestLogViewModel>();
            foreach (RequestLog item in source)
            {
                RequestLogViewModel listModel = new RequestLogViewModel()
                {
                    Id = item.Id,
                    EventTimeString = item.EventTime.ToString("yyyy-MM-dd HH:mm:ss"),
                    IpAddress = item.IpAddress,
                    RequestUrl = item.RequestUrl
                };
                result.Add(listModel);
            }
            var gridModel = new DataSourceResult<RequestLogViewModel>(pageSize)
            {
                Data = result,
                Total = source.TotalCount,
                PageCount = source.TotalPages
            };
            return gridModel;
        }

        protected override void FillOtherInfo(RequestLogViewModel model, RequestLog entity)
        {
            model.EventTimeString = entity.EventTime.ToString("yyyy-MM-dd HH:mm:ss");
        }
    }
}