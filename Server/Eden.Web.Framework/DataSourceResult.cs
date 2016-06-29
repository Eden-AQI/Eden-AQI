using System.Collections.Generic;

namespace Eden.Web.Framework
{
    public class DataSourceResult<T>
    {
        private int _pageSize;
        private ICollection<T> _data;
        public DataSourceResult(int pageSize)
        {
            _pageSize = pageSize;
        }

        public ICollection<T> Data
        {
            get { return _data; }
            set
            {
                _data = value;
                if (null != Data)
                {
                    if (_data.Count == 0)
                        PageCount = 0;
                    else {
                        PageCount = _data.Count / _pageSize;
                        if (_data.Count % _pageSize > 0)
                            PageCount++;
                    }
                }
            }
        }

        public int Total { get; set; }

        public int PageCount { get; set; }


    }
}
