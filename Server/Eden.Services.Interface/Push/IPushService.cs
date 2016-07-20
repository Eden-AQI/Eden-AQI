using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.ServicesDefine.Push
{
    public interface IPushService
    {
        bool Push(string type, string title, string platform, string message);
    }
}
