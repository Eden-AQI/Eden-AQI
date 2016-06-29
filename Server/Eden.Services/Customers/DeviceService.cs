using Eden.ServicesDefine.Customers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Eden.Domain.Customers;
using Eden.Core.Data;
using Eden.Core.Caching;
using Eden.Data;
using System.Data.Common;

namespace Eden.Services.Customers
{
    public class DeviceService : DefaultEntityService<Device>, IDeviceService
    {

        private readonly IRepository<UserActivity> _activityRepository;

        public DeviceService(IRepository<Device> repository, ICacheManager cacheManager, IDbContext context, IDataProvider dataProvider, IRepository<UserActivity> activityRepository) :
            base(repository, cacheManager, context, dataProvider)
        {
            _activityRepository = activityRepository;
        }

        public void RegisterDevice(Device device)
        {
            //string sql = "DELETE FROM Device WHERE PushId=@pushId;INSERT INTO Device VALUES(@platform,@deviceType,@pushId,@latitude,@longitude,getdate())";
            //DbContext.ExecuteSqlCommand(sql, parameters: new DbParameter[] {
            //    CreateParameter("pushId", device.PushId) ,
            //    CreateParameter("platform", device.Platform) ,
            //    CreateParameter("deviceType", device.DeviceType) ,
            //    CreateParameter("latitude", device.Latitude),
            //    CreateParameter("longitude", device.Longitude)
            //});
            _repository.Insert(device);
        }

        public void Heartbeat(string deviceNumber)
        {
            UserActivity entity = new UserActivity()
            {
                DeviceNumber = deviceNumber,
                EventTime = DateTime.Now,
                EventType = 0
            };
            _activityRepository.Insert(entity);
        }

    }
}
