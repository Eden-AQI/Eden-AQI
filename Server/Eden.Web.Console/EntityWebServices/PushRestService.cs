using Eden.Domain.Customers;
using Eden.Web.Console.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Eden.Core;
using Eden.Web.Framework;
using System.Web.Mvc;
using Eden.ServicesDefine.Push;
using Eden.Core.Infrastructure;

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
                    StartTimeString = item.StartTime.ToString("yyyy-MM-dd"),
                    EndTimeString = item.EndTime.ToString("yyyy-MM-dd"),
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
            model.CreateTimeString = entity.CreateTime.ToString("yyyy-MM-dd");
            model.StartTimeString = entity.StartTime.ToString("yyyy-MM-dd");
            model.EndTimeString = entity.EndTime.ToString("yyyy-MM-dd");
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
                case 1: return "所有设备";
                case 2: return "IOS设备";
                case 3: return "Android设备";
            }
            return "";
        }

        //public override ActionResult Put(FormCollection form)
        //{
        //    PushViewModel model = Activator.CreateInstance<PushViewModel>();
        //    updateModel(model, form);

        //    if (0 == model.Id)
        //    {
        //        Notify entity = Activator.CreateInstance<Notify>();
        //        WebTools.CopyProperties(model, entity);
        //        var otherOk = ProcessPutOtherThing(model, entity);

        //        var pushService = EngineContext.Current.Resolve<IPushService>();

        //        if (otherOk.Successfully)
        //        {
        //            bool pushOk = pushService.Push(model.Level.ToString(), model.Message, model.Platform.ToString(), model.Message);
        //            if (!pushOk)
        //                return new BadResponse("推送失败");
        //            EntityService.Insert(entity);
        //        }
        //        else
        //            return new BadResponse(otherOk.ErrorMessage);
        //    }
        //    return new ContentResult();
        //}
    }
}