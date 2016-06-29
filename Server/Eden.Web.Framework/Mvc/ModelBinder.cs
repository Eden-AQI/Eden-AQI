using System.Web.Mvc;

namespace Eden.Web.Framework.Mvc
{
    public class ModelBinder : DefaultModelBinder
    {
        public override object BindModel(ControllerContext controllerContext, ModelBindingContext bindingContext)
        {
            var model = base.BindModel(controllerContext, bindingContext);
            if (model is BaseModel)
            {
                ((BaseModel)model).BindModel(controllerContext, bindingContext);
            }
            return model;
        }
    }
}
