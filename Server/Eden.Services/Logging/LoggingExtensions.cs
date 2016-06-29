using System;
using Eden.Domain.Customers;
using Eden.Domain.Logging;
using Eden.ServicesDefine.Logging;
using System.Threading;

namespace Eden.Services.Logging
{
    public static class LoggingExtensions
    {
        public static void Debug(this ILogger logger, string message, Exception exception = null, EventSource? eventSource = null, Customer customer = null)
        {
            FilteredLog(logger, LogLevel.Debug, message, exception, eventSource, customer);
        }
        public static void Information(this ILogger logger, string message, Exception exception = null, EventSource? eventSource = null, Customer customer = null)
        {
            FilteredLog(logger, LogLevel.Information, message, exception, eventSource, customer);
        }
        public static void Warning(this ILogger logger, string message, Exception exception = null, EventSource? eventSource = null, Customer customer = null)
        {
            FilteredLog(logger, LogLevel.Warning, message, exception, eventSource, customer);
        }
        public static void Error(this ILogger logger, string message, Exception exception = null, EventSource? eventSource = null, Customer customer = null)
        {
            FilteredLog(logger, LogLevel.Error, message, exception, eventSource, customer);
        }
        public static void Fatal(this ILogger logger, string message, Exception exception = null, EventSource? eventSource = null, Customer customer = null)
        {
            FilteredLog(logger, LogLevel.Fatal, message, exception, eventSource, customer);
        }

        private static void FilteredLog(ILogger logger, LogLevel level, string message, Exception exception = null, EventSource? eventSource = null, Customer customer = null)
        {
            //don't log thread abort exception
            if (exception is System.Threading.ThreadAbortException)
                return;

            if (logger.IsEnabled(level))
            {
                string fullMessage = exception == null ? string.Empty : exception.ToString();
                logger.InsertLog(level, message, fullMessage, eventSource, customer);
            }
        }
    }
}
