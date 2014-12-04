var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};

var EVA;
(function (EVA) {
    var applicationInstance = null;
    var globalLoaderTimeout = null;

    function hasApplicationInstance() {
        return !_.isNull(applicationInstance) && !_.isUndefined(applicationInstance);
    }

    function escapeClientId() {
        var args = [];
        for (var _i = 0; _i < (arguments.length - 0); _i++) {
            args[_i] = arguments[_i + 0];
        }
        return EVA.Components.PrimeFacesHelper.escapeClientId.apply(this, arguments);
    }
    EVA.escapeClientId = escapeClientId;

    var Application = (function (_super) {
        __extends(Application, _super);
        function Application(config) {
            var _this = this;
            _super.call(this, { el: 'body' });
            this.loaderDialog = 'loaderDialog';
            this.conversationId = null;
            this.CSRFToken = null;

            this.conversationId = config.conversationId;
            this.CSRFToken = config.csrfToken;

            if (config.hasOwnProperty('keepAlive') && typeof config.keepAlive == 'object') {
                this.sessionTimeout = new EVA.Components.SessionTimeout(config.keepAlive);
            }

            this.onDocumentReady(function () {
                _this.insertCSRFToken();
                _this.attachCSRFHeader();
                _this.bootstrap();
            });
        }
        Application.prototype.attachCSRFHeader = function () {
            var _this = this;
            jQuery.ajaxPrefilter(function (options, originalOptions, jqXHR) {
                jqXHR.setRequestHeader('X-CSRF-Token', _this.CSRFToken);
            });
        };

        Application.prototype.getCSRFToken = function () {
            return this.CSRFToken;
        };

        Application.prototype.setView = function (viewInstance) {
            this.view = viewInstance;
        };

        Application.prototype.getView = function () {
            return this.view;
        };

        Application.prototype.onViewReady = function (handler) {
            var _this = this;
            this.onDocumentReady(function () {
                handler.call(_this.getView());
            });
        };

        Application.prototype.createRoute = function (viewPath, appendConversationId) {
            if (typeof appendConversationId === "undefined") { appendConversationId = true; }
            if (!viewPath) {
                return '';
            }

            var url = '/secure' + viewPath;

            if (appendConversationId && this.conversationId) {
                url = this.getRequestBroker().updateQueryString('cid', this.conversationId, url);
            }

            return url;
        };

        Application.prototype.redirect = function (viewPath, appendConversationId) {
            if (typeof appendConversationId === "undefined") { appendConversationId = true; }
            window.location.href = this.createRoute(viewPath, appendConversationId);
        };

        Application.prototype.showLoader = function () {
            var _this = this;
            clearTimeout(globalLoaderTimeout);
            globalLoaderTimeout = setTimeout(function () {
                var dialog = _this.widget(_this.loaderDialog);
                dialog.show();
            }, 1000);
        };

        Application.prototype.hideLoader = function () {
            clearTimeout(globalLoaderTimeout);
            var dialog = this.widget(this.loaderDialog);
            dialog.hide();
        };

        Application.prototype.bootstrap = function () {
            this.determineCookieSupport();
            var viewName = this.$('.page-container > .page').attr('data-view');
            var view = this.resolveViewClass(viewName);
            if (!_.isUndefined(view)) {
                this.setView(new view());
            }

            if (this.conversationId) {
                var url = this.getRequestBroker().updateQueryString('cid', this.conversationId, window.location.href);
                if (url != window.location.href && typeof window.history.replaceState != 'undefined') {
                    window.history.replaceState(null, document.title, url);
                }
            }
        };

        Application.prototype.insertCSRFToken = function () {
            var _this = this;
            var insertCSRF = function () {
                _this.$("form:not([data-csrf])").each(function (i, element) {
                    var form = _this.$(element);
                    if (form.find('input[name="CSRFToken"]:hidden').length > 0) {
                        return;
                    }

                    var csrfInput = $(document.createElement('input'));
                    csrfInput.attr('id', 'CSRFToken');
                    csrfInput.attr('type', 'hidden');
                    csrfInput.attr('name', 'CSRFToken');
                    csrfInput.val(_this.CSRFToken);

                    form.append(csrfInput);
                    form.attr('data-csrf', 'true');
                });
            };

            this.getRequestBroker().attachEvent('*', 'complete', function () {
                insertCSRF();
            }, true);

            insertCSRF();
        };

        Application.prototype.resolveViewClass = function (viewName) {
            return EVA.View[viewName];
        };

        Application.prototype.isCookiesEnabled = function () {
            return navigator.cookieEnabled || ("cookie" in document && (document.cookie.length > 0 || (document.cookie = "test").indexOf.call(document.cookie, "test") > -1));
        };

        Application.prototype.determineCookieSupport = function () {
            if (!this.isCookiesEnabled()) {
                this.onDocumentReady(function () {
                    jQuery('#no_cookies_msg').show();
                });
            }
        };

        Application.getInstance = function () {
            if (!hasApplicationInstance()) {
                throw "No application instance exists";
            }

            return applicationInstance;
        };

        Application.on = function () {
            var args = [];
            for (var _i = 0; _i < (arguments.length - 0); _i++) {
                args[_i] = arguments[_i + 0];
            }
            return applicationInstance.$el.on.apply(applicationInstance.$el, arguments);
        };

        Application.one = function () {
            var args = [];
            for (var _i = 0; _i < (arguments.length - 0); _i++) {
                args[_i] = arguments[_i + 0];
            }
            return applicationInstance.$el.one.apply(applicationInstance.$el, arguments);
        };

        Application.createInstance = function (config) {
            if ((_.isUndefined(config) || !config.hasOwnProperty('csrfToken')) && !hasApplicationInstance()) {
                throw "Missing CSRFToken nessesary for creating Application instance";
            } else {
                applicationInstance = new Application(config);
                return applicationInstance;
            }
        };
        return Application;
    })(EVA.Components.BaseView);
    EVA.Application = Application;
})(EVA || (EVA = {}));
