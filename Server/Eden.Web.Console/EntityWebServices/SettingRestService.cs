using Eden.Domain.Configuration;
using Eden.Web.Console.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Eden.Web.Console.EntityWebServices
{
    public class SettingRestService : BaseRestService<Setting, SettingViewModel>
    {
        public SettingRestService() : base("setting") { }
    }
}