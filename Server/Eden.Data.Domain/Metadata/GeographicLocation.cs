
using Eden.Core;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Domain.Metadata
{
    /// <summary>
    /// 地理位置
    /// </summary>
    [Table("GeographicLocation")]
    public class GeographicLocation : BaseEntity
    {
        public new int Id { get; set; }

        public string Code { get; set; }

        public string Province { get; set; }

        public string City { get; set; }

        public string District { get; set; }

        public int ParentId { get; set; }

        [NotMapped]
        public GeographicLocation Parent { get; set; }

        private List<GeographicLocation> _childs = new List<GeographicLocation>();

        [NotMapped]
        public List<GeographicLocation> Items
        {
            get { return _childs; }
        }

    }
}
