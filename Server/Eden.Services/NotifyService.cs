using Eden.ServicesDefine;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Eden.Domain.Customers;
using Eden.Core.Data;

namespace Eden.Services
{
    public class NotifyService : INotifyService
    {
        private readonly IRepository<Notify> _repository;

        public NotifyService(IRepository<Notify> repository)
        {
            _repository = repository;
        }

        public List<Notify> GetCurrentNotifyList()
        {
            DateTime now = DateTime.Now;
            return _repository.Table.Where(n => n.StartTime <=now && n.EndTime >= now).ToList();
        }
    }
}
