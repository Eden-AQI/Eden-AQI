using Eden.Core.Data;
using Eden.Data;
using Eden.Domain.Customers;
using Eden.Services.Tasks;
using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Services.Customers
{
    public class ReportCalcTask : DatabaseService, ITask
    {
        private readonly IRepository<Report> _reportRepository;
        private readonly IRepository<UserActivity> _activityRepository;

        public ReportCalcTask(IDbContext dbContext, IDataProvider dataProvider, IRepository<Report> reportRepository, IRepository<UserActivity> activityRepository) : base(dbContext, dataProvider)
        {
            _reportRepository = reportRepository;
            _activityRepository = activityRepository;
        }

        public void Execute()
        {
            DateTime calcTime = DateTime.Now;
            calcTime = new DateTime(calcTime.Year, calcTime.Month, calcTime.Day, calcTime.Hour, 0, 0);
            if (calcTime.Hour == 1)
            {
                genDayActivityReport(calcTime.AddDays(-1));
                calcDayUserReport(calcTime.AddDays(-1));
            }
            calcTime = calcTime.AddHours(-1);
            genHourActivityReport(calcTime);
            calcHourUserReport(calcTime);
        }

        private void genHourActivityReport(DateTime time)
        {
            var history = _reportRepository.Table.Where(r => r.Duration == 0 && r.LST == time && r.Type == 0).FirstOrDefault();
            if (null != history)
                _reportRepository.Delete(history);

            string sql = "SELECT COUNT(*) FROM (SELECT DISTINCT DeviceNumber FROM UserActivity WHERE EventType=0 AND EventTime BETWEEN @d1 AND @d2) t";
            DateTime d1 = new DateTime(time.Year, time.Month, time.Day, time.Hour, 0, 0);
            DateTime d2 = new DateTime(time.Year, time.Month, time.Day, time.Hour, 59, 59);
            var result = DbContext.SqlQuery<int>(sql, parameters: new DbParameter[] { CreateParameter("d1", d1), CreateParameter("d2", d2) }).FirstOrDefault();

            Report entity = new Report() { Value = result, Duration = 0, LST = d1, Type = 0 };
            _reportRepository.Insert(entity);

        }

        private void genDayActivityReport(DateTime time)
        {
            string sql = "SELECT SUM(Value) FROM Report WHERE LST BETWEEN @d1 AND @d2 AND Type=0 AND Duration=0";
            DateTime d1 = new DateTime(time.Year, time.Month, time.Day, 0, 0, 0);
            DateTime d2 = new DateTime(time.Year, time.Month, time.Day, 23, 59, 59);
            string clearSql = "DELETE FROM Report WHERE LST=@lst AND Type=0 AND Duration=1";
            DbContext.ExecuteSqlCommand(clearSql, parameters: new DbParameter[] { CreateParameter("lst", d1) });
            var result = DbContext.SqlQuery<int>(sql, parameters: new DbParameter[] { CreateParameter("d1", d1), CreateParameter("d2", d2) });

            Report entity = new Report() { Value = result.ElementAt(0), Duration = 1, LST = d1, Type = 0 };
            _reportRepository.Insert(entity);

        }

        private void calcHourUserReport(DateTime time)
        {
            string clearSql = "DELETE FROM Report WHERE LST=@lst AND Type=1 AND Duration=0";
            DbContext.ExecuteSqlCommand(clearSql, parameters: new DbParameter[] { CreateParameter("lst", time) });

            string sql = "SELECT COUNT(*) FROM Device WHERE CreateTime BETWEEN @d1 AND @d2";
            DateTime d1 = new DateTime(time.Year, time.Month, time.Day, time.Hour, 0, 0);
            DateTime d2 = new DateTime(time.Year, time.Month, time.Day, time.Hour, 59, 59);
            var result = DbContext.SqlQuery<int>(sql, parameters: new DbParameter[] { CreateParameter("d1", d1), CreateParameter("d2", d2) });

            Report entity = new Report() { Value = result.ElementAt(0), Duration = 0, LST = d1, Type = 1 };
            _reportRepository.Insert(entity);
        }

        private void calcDayUserReport(DateTime time)
        {
            string sql = "SELECT SUM(Value) FROM Report WHERE LST BETWEEN @d1 AND @d2 AND Type=1 AND Duration=0";
            DateTime d1 = new DateTime(time.Year, time.Month, time.Day, 0, 0, 0);
            DateTime d2 = new DateTime(time.Year, time.Month, time.Day, 23, 59, 59);
            string clearSql = "DELETE FROM Report WHERE LST=@lst AND Type=1 AND Duration=1";
            DbContext.ExecuteSqlCommand(clearSql, parameters: new DbParameter[] { CreateParameter("lst", d1) });
            var result = DbContext.SqlQuery<int>(sql, parameters: new DbParameter[] { CreateParameter("d1", d1), CreateParameter("d2", d2) });

            Report entity = new Report() { Value = result.ElementAt(0), Duration = 1, LST = d1, Type = 1 };
            _reportRepository.Insert(entity);
        }



    }
}
