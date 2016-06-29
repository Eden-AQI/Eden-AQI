using Eden.Core;
using Eden.Core.Caching;
using Eden.Core.Data;
using Eden.Data;
using Eden.ServicesDefine;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.Core.Objects;
using System.Linq;
using System.Linq.Expressions;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Services
{
    /// <summary>
    /// 默认的实体CURD操作,查询需要另外实现
    /// </summary>
    /// <typeparam name="T"></typeparam>
    public class DefaultEntityService<T> : DatabaseService, IEntityService<T> where T : BaseEntity
    {
        protected IRepository<T> _repository;
        protected ICacheManager _cacheManager;
        //private readonly IDbContext _context;

        private string cacheKey = null;
        private string cacheListKey = null;

        public DefaultEntityService(IRepository<T> repository, ICacheManager cacheManager, IDbContext context, IDataProvider dataProvider)
        {
            _repository = repository;
            _cacheManager = cacheManager;
            DbContext = context;
            DataProvider = dataProvider;
            var ps = typeof(T).GetCustomAttributes(typeof(CacheAttribute), true);
            if (ps.Length != 0)
            {
                CacheAttribute ca = (CacheAttribute)ps[0];
                cacheKey = ca.CacheKey;
                cacheListKey = ca.CacheListKey;
            }
            if (string.IsNullOrEmpty(cacheKey))
                cacheKey = typeof(T).ToString();
            if (string.IsNullOrEmpty(cacheListKey))
                cacheListKey = cacheKey + ".list";
        }

        public void Insert(T model)
        {
            if (null == model)
                throw new EdenException("model is null");
            _repository.Insert(model);
            if (!string.IsNullOrEmpty(cacheListKey))
                _cacheManager.Remove(cacheListKey);
        }



        public void Delete(int id)
        {
            var ta = typeof(T).GetCustomAttributes(typeof(TableAttribute));
            if (ta.Count() > 0)
            {
                TableAttribute theAttribute = ta.ElementAt(0) as TableAttribute;
                string sql = "DELETE FROM " + theAttribute.Name + " WHERE Id=@id";
                DbContext.ExecuteSqlCommand(sql, parameters: CreateParameter("id", id));
            }
            else {
                T m = GetById(id, false);
                if (null == m)
                    throw new EdenException("model is null");
                _repository.Delete(m);
            }
            if (!string.IsNullOrEmpty(cacheKey))
            {
                string ck = cacheKey + id;
                _cacheManager.Remove(ck);
                _cacheManager.Remove(cacheListKey);
            }
        }

        public void Update(T model)
        {
            if (null == model)
                throw new EdenException("model is null");
            _repository.Update(model);
            if (!string.IsNullOrEmpty(cacheKey))
            {
                string ck = cacheKey + model.Id;
                _cacheManager.Remove(ck);
                _cacheManager.Remove(cacheListKey);
            }
        }

        public T GetById(int id, bool fromCache = true)
        {
            if (fromCache && !string.IsNullOrEmpty(cacheKey))
            {
                string ck = cacheKey + id;
                return _cacheManager.Get<T>(ck, () =>
                {
                    return GetById(id, false);
                });
            }
            return _repository.Table.Where(t => t.Id == id).FirstOrDefault();
        }

        public ICollection<T> GetAll()
        {
            return _cacheManager.Get<ICollection<T>>(cacheListKey, () => { return _repository.Table.ToList(); });
        }

        public IPagedList<T> Search(string key, int pageIndex = 0, int pageSize = int.MaxValue)
        {
            Type t = typeof(T);

            List<PropertyInfo> indexProperties = _cacheManager.Get<List<PropertyInfo>>("INDEX_ATTRIBUTE_" + t.ToString(), () => { return getIndexProperties(t); });
            List<PropertyOrderInfo> orderProperties = _cacheManager.Get<List<PropertyOrderInfo>>("ORDER_ATTRIBUTE_" + t.ToString(), () => { return getOrderProperties(t); });

            IQueryable<T> query = null;
            if (!string.IsNullOrEmpty(key))
            {
                var method = typeof(string).GetMethod("Contains", new Type[] { typeof(string) });
                ParameterExpression param = Expression.Parameter(typeof(T), "p");
                Expression queryTree = null;
                foreach (PropertyInfo pro in indexProperties)
                {
                    var e = Expression.Call(Expression.Property(param, pro), method, Expression.Constant(key));
                    if (null == queryTree)
                        queryTree = e;
                    else
                        queryTree = Expression.Or(queryTree, e);
                }
                if (queryTree != null)
                {
                    var lambad = Expression.Lambda<Func<T, bool>>(queryTree, param);
                    query = _repository.Table.Where(lambad);
                }
            }
            else
                query = _repository.Table;
            bool then = false, descThen = false;
            foreach (PropertyOrderInfo pro in orderProperties.OrderByDescending(o => o.Index))
            {
                query = query.OrderBy(pro.PropertyName, pro.Asc, pro.Asc ? then : descThen);
                if (pro.Asc)
                    then = true;
                else
                    descThen = true;
            }
            if (orderProperties.Count == 0)
                query = query.OrderBy(e => e.Id);
            return new PagedList<T>(query, pageIndex, pageSize);
        }

        private List<PropertyInfo> getIndexProperties(Type t)
        {
            List<PropertyInfo> rs = new List<PropertyInfo>();
            foreach (PropertyInfo proInfo in t.GetProperties())
            {
                object[] qas = proInfo.GetCustomAttributes(typeof(FullIndexKey), true);
                foreach (var item in qas)
                {
                    FullIndexKey q = (FullIndexKey)item;
                    rs.Add(proInfo);
                    break;
                }

            }
            return rs;
        }

        private List<PropertyOrderInfo> getOrderProperties(Type t)
        {
            List<PropertyOrderInfo> rs = new List<PropertyOrderInfo>();
            foreach (PropertyInfo proInfo in t.GetProperties())
            {
                object[] oas = proInfo.GetCustomAttributes(typeof(OrderBy), true);
                foreach (var item in oas)
                {
                    OrderBy o = (OrderBy)item;
                    PropertyOrderInfo poi = new PropertyOrderInfo();
                    poi.PropertyName = proInfo.Name;
                    poi.Index = o.Index;
                    poi.Asc = o.Asc;
                    rs.Add(poi);
                    break;
                }
            }
            return rs;
        }


    }

}

