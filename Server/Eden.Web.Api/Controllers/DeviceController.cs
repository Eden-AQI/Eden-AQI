using Eden.Core.Infrastructure;
using Eden.Domain.Customers;
using Eden.ServicesDefine.Customers;
using Eden.Web.Api.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;

namespace Eden.Web.Api.Controllers
{
    /// <summary>
    /// 设备相关
    /// </summary>
    [RoutePrefix("Device")]
    public class DeviceController : BaseApiController
    {
        /// <summary>
        /// 注册设备信息，第一次使用时
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        [Route("RegisterDevice")]
        public IHttpActionResult RegisterDevice(DeviceModel model)
        {
            IDeviceService deviceService = EngineContext.Current.Resolve<IDeviceService>();
            Device entity = new Device()
            {
                CreateTime = DateTime.Now,
                Latitude = model.Latitude,
                Longitude = model.Longitude,
                DeviceType = model.DeviceType,
                DeviceNumber = model.DeviceNumber,
                Platform = model.Platform,
                PushId = model.PushId
            };
            deviceService.RegisterDevice(entity);
            return Ok();
        }

        /// <summary>
        /// 心跳一下，当从主屏打开系统时调用此接口
        /// </summary>
        /// <param name="deviceNumber">设备编号</param>
        /// <returns></returns>
        [HttpPost]
        [Route("Heartbeat")]
        public IHttpActionResult Heartbeat(string deviceNumber)
        {
            if (string.IsNullOrEmpty(deviceNumber))
                return ArgumentError();
            IDeviceService deviceService = EngineContext.Current.Resolve<IDeviceService>();
            deviceService.Heartbeat(deviceNumber);
            return Ok();
        }

    }
}