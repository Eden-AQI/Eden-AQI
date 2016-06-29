using AutoMapper;
using Eden.Core;
using Eden.Core.Infrastructure;
using Eden.ServicesDefine;
using Eden.Web.Console.Models;
using Eden.Web.Framework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.Mvc;


namespace Eden.Web.Console.EntityWebServices
{

    /// <summary>
    /// Rest服务基础类
    /// </summary>
    /// <typeparam name="E">领域对象</typeparam>
    /// <typeparam name="M">视图模型</typeparam>
    public abstract class BaseRestService<E, M> : IRestService where E : BaseEntity where M : BaseViewModel
    {
        /// <summary>
        /// 默认分页大小
        /// </summary>
        protected const int DefaultPageSize = 30;

        /// <summary>
        /// 全局唯一领域名称
        /// </summary>
        public string Name { get; set; }

        /// <summary>
        /// 领域对象数据服务
        /// </summary>
        protected IEntityService<E> EntityService;

        public BaseRestService(string name)
        {
            this.Name = name;
            EntityService = EngineContext.Current.Resolve<IEntityService<E>>();
        }

        protected string ReplaceNgSelectValue(string ngVal)
        {
            string val = null;
            if (!string.IsNullOrEmpty(ngVal))
            {
                var sp = ngVal.Split(':');
                val = sp.Length == 2 ? sp[1] : ngVal;
                if (val.Trim() == "")
                    return null;
                return val;
            }
            return null;
        }

        /// <summary>
        /// 搜索行为
        /// </summary>
        /// <returns></returns>
        protected object BaseSearch()
        {
            string key = HttpContext.Current.Request["key"];
            string strPageIndex = HttpContext.Current.Request["pageIndex"];
            string strPageSize = HttpContext.Current.Request["pageSize"];

            int pageIndex, pageSize;
            int.TryParse(strPageIndex, out pageIndex);
            int.TryParse(strPageSize, out pageSize);
            if (pageIndex < 0) pageIndex = 0;
            if (pageSize == 0) pageSize = DefaultPageSize;
            IPagedList<E> source = EntityService.Search(key, pageIndex, pageSize);
            return CreateListModel(source, pageSize);
        }

        /// <summary>
        /// 组装列表数据，默认返回领域对象的所有属性
        /// </summary>
        /// <param name="source"></param>
        /// <param name="pageSize"></param>
        /// <returns></returns>
        protected virtual object CreateListModel(IPagedList<E> source, int pageSize)
        {
            var gridModel = new DataSourceResult<E>(pageSize)
            {
                Data = source,
                Total = source.TotalCount,
                PageCount = source.TotalPages
            };
            return gridModel;
        }

        /// <summary>
        /// 删除对象
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public virtual ActionResult Delete(int id)
        {
            IEntityService<E> service = EngineContext.Current.Resolve<IEntityService<E>>();
            service.Delete(id);
            return new ContentResult();
        }

        public virtual ActionResult Search()
        {
            return new JsonResult() { Data = BaseSearch(), JsonRequestBehavior = JsonRequestBehavior.AllowGet };
        }

        public virtual ActionResult Get(int id)
        {
            if (0 == id)
                return new BadResponse("参数错误");
            E entity = EntityService.GetById(id, true);
            if (null == entity)
                return new BadResponse("参数错误");
            M model = Activator.CreateInstance<M>();
            WebTools.CopyProperties(entity, model);
            FillOtherInfo(model, entity);
            return new JsonResult() { Data = model, JsonRequestBehavior = JsonRequestBehavior.AllowGet };
        }

        public virtual ActionResult Put(FormCollection form)
        {
            M model = Activator.CreateInstance<M>();
            updateModel(model, form);

            if (0 == model.Id)
            {
                E entity = Activator.CreateInstance<E>();
                WebTools.CopyProperties(model, entity);
                var otherOk = ProcessPutOtherThing(model, entity);
                if (otherOk.Successfully)
                    EntityService.Insert(entity);
                else
                    return new BadResponse(otherOk.ErrorMessage);
            }
            else {
                var entity = EntityService.GetById(model.Id, false);
                if (null == entity)
                    return new BadResponse("参数错误");
                WebTools.CopyProperties(model, entity, false);
                var otherOk = ProcessPutOtherThing(model, entity);
                if (otherOk.Successfully)
                    EntityService.Update(entity);
                else
                    return new BadResponse(otherOk.ErrorMessage);
            }
            return new ContentResult();
        }

        protected virtual OtherThingResult ProcessPutOtherThing(M model, E entity)
        {
            return new OtherThingResult() { Successfully = true };
        }

        protected virtual void FillOtherInfo(M model, E entity)
        {

        }

        private ModelBinderDictionary _binders;
        protected internal ModelBinderDictionary Binders
        {
            get
            {
                if (_binders == null)
                {
                    _binders = ModelBinders.Binders;
                }
                return _binders;
            }
            set { _binders = value; }
        }

        public ControllerContext ControllerContext
        {
            get; set;
        }

        private void updateModel(M model, FormCollection form)
        {
            Predicate<string> propertyFilter = propertyName => IsPropertyAllowed(propertyName, null, null);
            IModelBinder binder = Binders.GetBinder(typeof(M));

            ModelBindingContext bindingContext = new ModelBindingContext()
            {
                ModelMetadata = ModelMetadataProviders.Current.GetMetadataForType(() => model, typeof(M)),
                ModelName = null,
                ModelState = new ModelStateDictionary(),
                PropertyFilter = propertyFilter,
                ValueProvider = form
            };
            binder.BindModel(ControllerContext, bindingContext);
        }

        

        private bool IsPropertyAllowed(string propertyName, ICollection<string> includeProperties, ICollection<string> excludeProperties)
        {
            // We allow a property to be bound if its both in the include list AND not in the exclude list.
            // An empty include list implies all properties are allowed.
            // An empty exclude list implies no properties are disallowed.
            bool includeProperty = (includeProperties == null) || (includeProperties.Count == 0) || includeProperties.Contains(propertyName, StringComparer.OrdinalIgnoreCase);
            bool excludeProperty = (excludeProperties != null) && excludeProperties.Contains(propertyName, StringComparer.OrdinalIgnoreCase);
            return includeProperty && !excludeProperty;
        }
    }

    public class OtherThingResult
    {
        public bool Successfully { get; set; }
        public string ErrorMessage { get; set; }
    }

}