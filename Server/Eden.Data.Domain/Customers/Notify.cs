using Eden.Core;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Domain.Customers
{
    [Table("Notify")]
    public class Notify : BaseEntity
    {
        public int Level { get; set; }

        [FullIndexKey]
        public string Message { get; set; }

        [OrderBy(Desc = true)]
        public DateTime CreateTime { get; set; }

        public int CityId { get; set; }

        public int Platform { get; set; }

        public DateTime StartTime { get; set; }

        public DateTime EndTime { get; set; }
    }
}
