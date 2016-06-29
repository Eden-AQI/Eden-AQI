using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Core
{
    [AttributeUsage(AttributeTargets.Property, Inherited = true)]
    public class FullIndexKey : Attribute
    {
    }

    public class OrderBy : Attribute
    {
        private bool desc = true;
        public bool Asc { get; set; }
        public bool Desc
        {
            get { return desc; }
            set { desc = value; }
        }
        public int Index { get; set; }
    }

    public struct PropertyOrderInfo
    {
        public string PropertyName;
        public int Index;
        public bool Asc;
    }
}
