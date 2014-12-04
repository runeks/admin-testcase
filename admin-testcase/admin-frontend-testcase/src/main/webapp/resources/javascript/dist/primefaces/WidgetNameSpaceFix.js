(function (jQuery, PrimeFaces) {
    $(document).ready(function () {
        for (var widgetName in PrimeFaces.widgets) {
            if (PrimeFaces.widgets.hasOwnProperty(widgetName)) {
                window[widgetName] = PrimeFaces.widgets[widgetName];
            }
        }
    });
})(jQuery, PrimeFaces);
