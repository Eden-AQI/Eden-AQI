using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;
using System.Web.Http.Results;

namespace Eden.Web.Api.Infrastructure
{
    public class JsonStringActoinResult : IHttpActionResult
    {
        private readonly string _json;
        private readonly HttpRequestMessage _request;

        public JsonStringActoinResult(string json, HttpRequestMessage request)
        {
            _json = json;
            _request = request;
        }
        public Task<HttpResponseMessage> ExecuteAsync(CancellationToken cancellationToken)
        {
            return Task.FromResult(Execute());
        }

        private HttpResponseMessage Execute()
        {
            HttpResponseMessage response = new HttpResponseMessage(HttpStatusCode.OK);

            try
            {
                response.Content = new StringContent(_json);
                MediaTypeHeaderValue contentType = new MediaTypeHeaderValue("application/json");
                contentType.CharSet = Encoding.UTF8.WebName;
                response.Content.Headers.ContentType = contentType;
                response.RequestMessage = _request;
            }
            catch
            {
                response.Dispose();
                throw;
            }

            return response;
        }

    }
}