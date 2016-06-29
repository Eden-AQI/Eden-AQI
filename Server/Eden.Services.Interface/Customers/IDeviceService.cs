using Eden.Domain.Customers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.ServicesDefine.Customers
{
    public interface IDeviceService
    {
        void RegisterDevice(Device device);
        void Heartbeat(string deviceNumber);
    }
}
