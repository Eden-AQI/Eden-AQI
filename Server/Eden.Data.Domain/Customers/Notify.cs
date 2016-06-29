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

        public string Message { get; set; }

        public DateTime CreateTime { get; set; }

        public int CityId { get; set; }

        public int Platform { get; set; }
    }
}
