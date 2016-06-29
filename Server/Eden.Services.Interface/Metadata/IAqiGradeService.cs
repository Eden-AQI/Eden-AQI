using Eden.Domain.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Eden.ServicesDefine.Metadata
{
    public interface IAqiGradeService
    {
        AQIGrade CalcGrade(int aqi);
    }
}
