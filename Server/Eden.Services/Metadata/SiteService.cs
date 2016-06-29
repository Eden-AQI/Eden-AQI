using Eden.ServicesDefine.Metadata;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Eden.Domain.Metadata;
using Eden.ServicesDefine;
using Microsoft.International.Converters.PinYinConverter;
using Eden.Core.Data;

namespace Eden.Services.Metadata
{
    public class SiteService : ISiteService
    {

        private readonly IEntityService<Site> _entityService;

        private readonly IRepository<Site> _repository;

        public SiteService(IEntityService<Site> entityService, IRepository<Site> repository)
        {
            _entityService = entityService;
            _repository = repository;
        }

        public ICollection<Site> GetAllSite()
        {
            var all = _entityService.GetAll();
            foreach (var s in all)
            {
                s.FirstPinyin = getFirstPinyin(s.Name);
            }
            return all;
            //throw new NotImplementedException();
        }
        
        public Site GetById(int id)
        {
            return _repository.Table.Where(s => s.SiteID == id).FirstOrDefault();
        }

        /// <summary> 
        /// 汉字转化为拼音首字母
        /// </summary> 
        /// <param name="str">汉字</param> 
        /// <returns>首字母</returns> 
        private string getFirstPinyin(string str)
        {
            if (string.IsNullOrEmpty(str))
                return string.Empty;

            foreach (char obj in str)
            {
                try
                {
                    ChineseChar chineseChar = new ChineseChar(obj);
                    return chineseChar.Pinyins[0][0].ToString();
                }
                catch
                {
                }
            }
            return string.Empty;
        }
    }
}
