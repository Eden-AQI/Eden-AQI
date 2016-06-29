


using Eden.Domain.Customers;

namespace Eden.ServicesDefine
{
    /// <summary>
    /// Work context
    /// </summary>
    public interface IWorkContext
    {
        Customer CurrentCustomer { get; set; }

        string ServerUrl { get; set; }


    }
}
