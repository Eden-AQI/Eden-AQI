using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.Common;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.Thirdparty.Openfire
{
    public class OpenfireService
    {
        private static DbProviderFactory dbFactory = null;
        private static string CONNECTION_STRING = null;

        //默认密码为:sxgisgood
        private static string SQL_INSERT_USER = "INSERT INTO ofUser(username,plainPassword,name,creationDate,modificationDate) VALUES(@username,'sxgisgood',@name,@creationDate,0);" +
            "INSERT INTO ofRoster VALUES (@n1,@username,'customerservice@push.suixinguang.cn',3,-1,-1,'customerservice');" +
            "INSERT INTO ofRoster VALUES (@n2,'customerservice',@jid,3,-1,-1,@username);" +
            "INSERT INTO ofRosterGroups VALUES(@n1,0,'Friends');INSERT INTO ofRosterGroups VALUES(@n2,0,'Friends');";

        private static string SQL_MAX_ID = "SELECT MAX(rosterID) FROM ofRoster";

        static OpenfireService()
        {
            if (null == dbFactory)
            {
                var config = ConfigurationManager.ConnectionStrings["openfire"];
                CONNECTION_STRING = config.ConnectionString;
                dbFactory = DbProviderFactories.GetFactory(config.ProviderName);
            }
        }

        private DbParameter createParameter(DbCommand command, string name, object value)
        {
            var p = command.CreateParameter();
            p.ParameterName = name;
            p.Value = value;
            return p;
        }

        public bool CreateOpenfireUser(string username)
        {
            try
            {
                using (var connection = dbFactory.CreateConnection())
                {
                    connection.ConnectionString = CONNECTION_STRING;
                    connection.Open();

                    var maxIdCommand = connection.CreateCommand();
                    maxIdCommand.CommandText = SQL_MAX_ID;
                    object maxIdObj = maxIdCommand.ExecuteScalar();

                    int maxId = 0;
                    if (null != maxIdObj && maxIdObj != DBNull.Value)
                        maxId = Convert.ToInt32(maxIdObj);

                    var command = connection.CreateCommand();
                    command.CommandText = SQL_INSERT_USER;

                    command.Parameters.Add(createParameter(command, "username", username));
                    command.Parameters.Add(createParameter(command, "name", username));
                    string creationDate = ((int)(DateTime.Now - new DateTime(1970, 1, 1, 0, 0, 0, 0)).TotalMilliseconds).ToString().PadLeft(15, '0');
                    command.Parameters.Add(createParameter(command, "creationDate", creationDate));
                    command.Parameters.Add(createParameter(command, "n1", maxId + 1));
                    command.Parameters.Add(createParameter(command, "n2", maxId + 2));
                    command.Parameters.Add(createParameter(command, "jid", username + "push.suixinguang.cn"));

                    int val = command.ExecuteNonQuery();
                    return val == 1;
                }
            }
            catch (Exception ex)
            {
                return false;
            }
        }
    }
}
