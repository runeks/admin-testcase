
var EVA;
(function (EVA) {
    (function (Components) {
        var componentSeperator = /:/g;

        var PrimeFacesHelper = (function () {
            function PrimeFacesHelper() {
            }
            PrimeFacesHelper.widget = function (widgetId) {
                return PF(widgetId);
            };

            PrimeFacesHelper.resolveWidget = function (eventElement) {
                var $eventElement;
                if (eventElement instanceof HTMLElement) {
                    $eventElement = $(eventElement);
                } else if (_.isObject(eventElement)) {
                    var sourceId = eventElement.source.replace(/:/g, "\\:");
                    $eventElement = $('#' + sourceId);
                }

                var $widgetElement = $eventElement.closest('[data-widgetVar]');

                return PrimeFacesHelper.widget($widgetElement.attr('data-widgetVar'));
            };

            PrimeFacesHelper.escapeClientId = function (componentId) {
                return componentId.replace(componentSeperator, '\\:');
            };
            return PrimeFacesHelper;
        })();
        Components.PrimeFacesHelper = PrimeFacesHelper;
    })(EVA.Components || (EVA.Components = {}));
    var Components = EVA.Components;
})(EVA || (EVA = {}));
