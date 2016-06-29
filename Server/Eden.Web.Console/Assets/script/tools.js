function formToJson($form) {
    var serializeObj = {};
    $($form.serializeArray()).each(function () {
        serializeObj[this.name] = this.value;
    });
    return serializeObj;
}