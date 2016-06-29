using Eden.Core;
using System.ComponentModel.DataAnnotations.Schema;

namespace Eden.Domain.Configuration
{
    [Table("Setting")]
    public class Setting : BaseEntity
    {
        public Setting() { }

        public Setting(string name, string value)
        {
            this.Name = name;
            this.Value = value;
        }

        [FullIndexKey]
        public string Name { get; set; }

        [FullIndexKey]
        public string Value { get; set; }

        public override string ToString()
        {
            return Name;
        }
    }
}
