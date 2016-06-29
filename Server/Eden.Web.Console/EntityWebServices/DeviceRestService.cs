using Eden.Domain.Customers;
using Eden.Web.Console.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Eden.Core;
using Eden.Web.Framework;

namespace Eden.Web.Console.EntityWebServices
{
    public class DeviceRestService : BaseRestService<Device, DeviceModel>
    {
        public DeviceRestService() : base("device") { }

        protected override object CreateListModel(IPagedList<Device> source, int pageSize)
        {
            List<DeviceListModel> result = new List<DeviceListModel>();
            foreach (Device item in source)
            {
                DeviceListModel listModel = new DeviceListModel()
                {
                    Id = item.Id,
                    CreateTime = item.CreateTime.ToString("yyyy-MM-dd HH:mm:ss"),
                    DeviceNumber = item.DeviceNumber,
                    DeviceTypeName = item.DeviceType == 1 ? "手机" : "平板",
                    PlatformName = item.Platform == 1 ? "IOS" : "Android"
                };
                result.Add(listModel);
            }
            var gridModel = new DataSourceResult<DeviceListModel>(pageSize)
            {
                Data = result,
                Total = source.TotalCount,
                PageCount = source.TotalPages
            };
            return gridModel;
        }

        protected override void FillOtherInfo(DeviceModel model, Device entity)
        {
            model.CreateTimeString = entity.CreateTime.ToString("yyyy-MM-dd HH:mm:ss");
            model.DeviceTypeName = entity.DeviceType == 1 ? "手机" : "平板";
            model.PlatformName = entity.Platform == 1 ? "IOS" : "Android";
        }
    }
}