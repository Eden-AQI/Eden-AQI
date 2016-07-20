using Eden.Core;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Domain.Logging
{
    [Table("RequestLog")]
    public class RequestLog : BaseEntity
    {
        public DateTime EventTime { get; set; }

        public string IpAddress { get; set; }

        public string RequestUrl { get; set; }

        public string Detail { get; set; }
    }
}
