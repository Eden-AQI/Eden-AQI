using Eden.Core.Data;
using Eden.Data;
using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Services
{
    public class DatabaseService
    {
        protected  IDbContext DbContext;
        protected  IDataProvider DataProvider;

        public DatabaseService() { }

        public DatabaseService(IDbContext dbContext, IDataProvider dataProvider)
        {
            DbContext = dbContext;
            DataProvider = dataProvider;
        }

        protected DbParameter CreateParameter(string name, object value)
        {
            DbParameter p = DataProvider.GetParameter();
            p.ParameterName = name;
            p.Value = value;
            return p;
        }
    }
}
