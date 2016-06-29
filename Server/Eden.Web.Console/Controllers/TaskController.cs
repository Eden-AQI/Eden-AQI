using Eden.Services.Tasks;
using Eden.Web.Framework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace Eden.Web.Console.Controllers
{
    public class TaskController : BaseController
    {

        private readonly IScheduleTaskService _taskService;

        public TaskController(IScheduleTaskService taskService)
        {
            _taskService = taskService;
        }

        public ActionResult RunNow(int id)
        {
            //var a = new Eden.Web.Console.Infrastructure.RealtimeAqiMakeTask();
            //var t = Type.GetType("Eden.Web.Console.Infrastructure.RealtimeAqiMakeTask");
            try
            {
                var scheduleTask = _taskService.GetTaskById(id);
                if (scheduleTask == null)
                    return BadArguments();

                var task = new Task(scheduleTask);
                //ensure that the task is enabled
                task.Enabled = true;
                //do not dispose. otherwise, we can get exception that DbContext is disposed
                task.Execute(true, false);
            }
            catch (Exception ex)
            {
                return new BadResponse(ex.Message);
            }

            return Ok();
        }
    }
}