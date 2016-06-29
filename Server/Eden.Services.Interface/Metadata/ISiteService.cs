using Eden.Domain.Metadata;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.ServicesDefine.Metadata
{
    public interface ISiteService
    {
        ICollection<Site> GetAllSite();

        Site GetById(int id);
    }
}
