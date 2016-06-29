using Eden.Core;
using System;
using System.ComponentModel.DataAnnotations.Schema;

namespace Eden.Domain
{
    [Table("SmsValidate")]
    public class SmsValidate : BaseEntity
    {
        public DateTime CreateOnUtc { get; set; }

        public string Code { get; set; }

        public string Phone { get; set; }

        public int Type { get; set; }
    }
}
