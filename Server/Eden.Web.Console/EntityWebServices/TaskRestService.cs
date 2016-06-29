using Eden.Domain.Tasks;
using Eden.Web.Console.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Eden.Core;
using Eden.Web.Framework;

namespace Eden.Web.Console.EntityWebServices
{
    public class TaskRestService : BaseRestService<ScheduleTask, ScheduleTaskViewModel>
    {
        public TaskRestService() : base("task")
        {
        }

        protected override object CreateListModel(IPagedList<ScheduleTask> source, int pageSize)
        {
            List<ScheduleTaskViewModel> result = new List<ScheduleTaskViewModel>();
            foreach (ScheduleTask item in source)
            {
                ScheduleTaskViewModel listModel = new ScheduleTaskViewModel();
                WebTools.CopyProperties(item, listModel);

                listModel.EnabledString = item.Enabled.ToString();
                listModel.StopOnErrorString = item.StopOnError.ToString();
                listModel.LastEndTime = item.LastEndUtc != null ? item.LastEndUtc.Value.ToString("yyyy-MM-dd HH:mm:ss") : "";
                listModel.LastStartTime = item.LastStartUtc != null ? item.LastStartUtc.Value.ToString("yyyy-MM-dd HH:mm:ss") : "";
                listModel.LastSuccessTime = item.LastSuccessUtc != null ? item.LastSuccessUtc.Value.ToString("yyyy-MM-dd HH:mm:ss") : "";
                result.Add(listModel);
            }
            var gridModel = new DataSourceResult<ScheduleTaskViewModel>(pageSize)
            {
                Data = result,
                Total = source.TotalCount,
                PageCount = source.TotalPages
            };
            return gridModel;
        }

        protected override OtherThingResult ProcessPutOtherThing(ScheduleTaskViewModel model, ScheduleTask entity)
        {
            entity.Enabled = "on" == model.EnabledString;
            entity.StopOnError = "on" == model.StopOnErrorString;
            return base.ProcessPutOtherThing(model, entity);
        }
    }
}