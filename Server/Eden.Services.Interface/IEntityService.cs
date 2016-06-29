using Eden.Core;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.ServicesDefine
{
    public interface IEntityService<T> where T : BaseEntity
    {
        void Insert(T model);

        void Delete(int id);

        void Update(T model);

        T GetById(int id, bool fromCache = true);

        IPagedList<T> Search(string key, int pageIndex = 0, int pageSize = int.MaxValue);

        ICollection<T> GetAll();
    }
}
