var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
var EVA;
(function (EVA) {
    (function (Components) {
        var requestBroker;
        var documentReadyStates = [
            'uninitialized',
            'loading',
            'interactive'
        ];

        function isTest() {
            return '__karma__' in window;
        }

        var BaseView = (function (_super) {
            __extends(BaseView, _super);
            function BaseView(options) {
                _super.call(this, options);
                this.getRequestBroker();
            }
            BaseView.prototype.initialize = function () {
                var _this = this;
                this.onDocumentReady(function () {
                    _this.waitButtonManager = EVA.Components.WaitButtonManager.createInstance();
                });
            };

            BaseView.prototype.onFirst = function (eventName, handler) {
                return this.$el.bindFirst(eventName, handler);
            };

            BaseView.prototype.widget = function (widgetVar) {
                return EVA.Components.PrimeFacesHelper.widget(widgetVar);
            };

            BaseView.prototype.resolveWidget = function (eventOrElement) {
                return EVA.Components.PrimeFacesHelper.resolveWidget(eventOrElement);
            };

            BaseView.prototype.attachOnComplete = function (componentId, handler) {
                this.getRequestBroker().attachOnComplete(componentId, handler);
                return this;
            };

            BaseView.prototype.attachOnStart = function (componentId, handler) {
                this.getRequestBroker().attachOnStart(componentId, handler);
                return this;
            };

            BaseView.prototype.attachOnError = function (componentId, handler) {
                this.getRequestBroker().attachOnError(componentId, handler);
                return this;
            };

            BaseView.prototype.escapeClientId = function (componentId) {
                return "#" + Components.PrimeFacesHelper.escapeClientId(componentId);
            };

            BaseView.prototype.getRequestBroker = function () {
                if (_.isUndefined(requestBroker) || _.isNull(requestBroker)) {
                    requestBroker = Components.RequestBroker.createInstance();
                }

                return requestBroker;
            };

            BaseView.prototype.onDocumentReady = function (handler) {
                if (!isTest() && documentReadyStates.indexOf(document.readyState) != -1) {
                    jQuery(document).ready(handler);
                } else {
                    handler();
                }

                return this;
            };
            return BaseView;
        })(Backbone.View);
        Components.BaseView = BaseView;
    })(EVA.Components || (EVA.Components = {}));
    var Components = EVA.Components;
})(EVA || (EVA = {}));
