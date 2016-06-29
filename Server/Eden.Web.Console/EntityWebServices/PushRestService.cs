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
    public class PushRestService : BaseRestService<Notify, PushViewModel>
    {
        public PushRestService() : base("push") { }

        protected override object CreateListModel(IPagedList<Notify> source, int pageSize)
        {
            List<PushViewModel> result = new List<PushViewModel>();
            foreach (Notify item in source)
            {
                PushViewModel listModel = new PushViewModel()
                {
                    Id = item.Id,
                    CreateTimeString = item.CreateTime.ToString("yyyy-MM-dd HH:mm:ss"),
                    PlatformName = getPlatform(item.Platform),
                    Type = getPushTarget(item.Level),
                    Message = item.Message
                };
                result.Add(listModel);
            }
            var gridModel = new DataSourceResult<PushViewModel>(pageSize)
            {
                Data = result,
                Total = source.TotalCount,
                PageCount = source.TotalPages
            };
            return gridModel;
        }

        protected override void FillOtherInfo(PushViewModel model, Notify entity)
        {
            model.CreateTimeString = entity.CreateTime.ToString("yyyy-MM-dd HH:mm:ss");
            model.Type = getPushTarget(entity.Level);
            model.PlatformName = getPlatform(entity.Platform);
        }

        protected override OtherThingResult ProcessPutOtherThing(PushViewModel model, Notify entity)
        {
            if (model.Id == 0)
                entity.CreateTime = DateTime.Now;
            return base.ProcessPutOtherThing(model, entity);
        }

        private string getPushTarget(int level)
        {
            switch (level)
            {
                case 1: return "一般消息";
                case 2: return "重污染预警";
                case 3: return "版本更新";
            }
            return "";
        }

        private string getPlatform(int platform)
        {
            switch (platform)
            {
                case 0: return "所有设备";
                case 1: return "IOS设备";
                case 2: return "Android设备";
            }
            return "";
        }
    }
}