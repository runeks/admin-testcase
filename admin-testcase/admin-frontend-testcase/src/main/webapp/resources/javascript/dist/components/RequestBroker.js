var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};

var EVA;
(function (EVA) {
    (function (Components) {
        var instance = null;
        var noOperation = function () {
        };

        var ComponentQueueModel = (function (_super) {
            __extends(ComponentQueueModel, _super);
            function ComponentQueueModel(componentId) {
                this.idAttribute = 'componentId';
                _super.call(this);

                this.set({
                    componentId: componentId,
                    complete: [],
                    start: [],
                    success: [],
                    error: []
                });
            }
            ComponentQueueModel.prototype.executeEvent = function (queue, response) {
                _.each(queue, function (item, index) {
                    item.handler(response);

                    if (!item.always) {
                        queue.splice(index, 1);
                    }
                });

                return queue;
            };

            ComponentQueueModel.prototype.error = function (response) {
                var queue = this.executeEvent(this.get('error'), response);
                this.set('error', queue);
            };

            ComponentQueueModel.prototype.success = function (response) {
                var queue = this.executeEvent(this.get('success'), response);
                this.set('success', queue);
            };

            ComponentQueueModel.prototype.complete = function (response) {
                var queue = this.executeEvent(this.get('complete'), response);
                this.set('complete', queue);
            };

            ComponentQueueModel.prototype.start = function (response) {
                var queue = this.executeEvent(this.get('start'), response);
                this.set('start', queue);
            };
            return ComponentQueueModel;
        })(Backbone.Model);

        var RequestBroker = (function () {
            function RequestBroker() {
                this.errorDialogId = 'errorDialog';
                this.primeFacesHelper = new Components.PrimeFacesHelper();
                this.componentQueue = new Backbone.Collection();
                this.bootstrap();
                this.handleAjaxError();
            }
            RequestBroker.prototype.bootstrap = function () {
                var _this = this;
                this.originalSendMethod = PrimeFaces.ajax.Request.send;
                PrimeFaces.ajax.Request.send = function (config) {
                    _this.sendRequest(config, this);
                };
            };

            RequestBroker.prototype.attachEvent = function (componentId, eventName, handler, always) {
                if (typeof always === "undefined") { always = false; }
                var queueModel = this.componentQueue.get(componentId);

                if (!queueModel) {
                    queueModel = new ComponentQueueModel(componentId);
                    this.componentQueue.add(queueModel);
                }

                var valueObject = {};
                valueObject[eventName] = queueModel.get(eventName).concat(this.createQueueItem(handler, always));

                queueModel.set(valueObject);
            };

            RequestBroker.prototype.executeEvent = function (eventName, response) {
                var queueModel = this.componentQueue.get(response.getComponentId());

                if (!queueModel) {
                    return;
                }

                queueModel[eventName].call(queueModel, response);
            };

            RequestBroker.prototype.executeAlways = function (eventName, response) {
                var queueModel = this.componentQueue.get('*');

                if (!queueModel) {
                    return;
                }

                queueModel[eventName].call(queueModel, response);
            };

            RequestBroker.prototype.attachOnComplete = function (componentId, handler) {
                this.attachEvent(componentId, 'complete', handler);
            };

            RequestBroker.prototype.attachOnStart = function (componentId, handler) {
                this.attachEvent(componentId, 'start', handler);
            };

            RequestBroker.prototype.attachOnError = function (componentId, handler) {
                this.attachEvent(componentId, 'error', handler);
            };

            RequestBroker.prototype.attachOnSuccess = function (componentId, handler) {
                this.attachEvent(componentId, 'success', handler);
            };

            RequestBroker.prototype.updateQueryString = function (key, value, url) {
                if (!url) {
                    throw "No url supplied";
                }

                var parameterRegexp = new RegExp("([?&])" + key + "=.*?(&|#|$)(.*)", "gi");

                if (parameterRegexp.test(url)) {
                    return this.handleExistingUrlParameter(key, url, value, parameterRegexp);
                } else {
                    return this.handleNonExistingUrlParameter(key, url, value);
                }
            };

            RequestBroker.prototype.createQueueItem = function (handler, alwaysExecute) {
                return {
                    handler: handler,
                    always: alwaysExecute
                };
            };

            RequestBroker.prototype.sendRequest = function (config, scope) {
                var _this = this;
                var response = new Components.Response();
                var onComplete = config.oncomplete || noOperation;
                config.oncomplete = function (jqXHR, textStatus, primefacesArgs) {
                    response.setPrimeFacesArguments(primefacesArgs);
                    response.setStatus(jqXHR['status']);
                    response.setComponentId(config.source);
                    response.jqXHR = jqXHR;
                    var result = onComplete.apply(scope, arguments);
                    _this.executeEvent('complete', response);
                    _this.executeAlways('complete', response);

                    return result;
                };

                var onStart = config.onstart || noOperation;
                config.onstart = function (jqXHR, textStatus, primefacesArgs) {
                    response.setPrimeFacesArguments(primefacesArgs);
                    response.setStatus(jqXHR['status']);
                    response.setComponentId(config.source);
                    response.jqXHR = jqXHR;
                    var result = onStart.apply(scope, arguments);
                    _this.executeEvent('start', response);
                    _this.executeAlways('start', response);
                    return result;
                };

                var onError = config.onerror || noOperation;
                config.onerror = function (jqXHR, textStatus, primefacesArgs) {
                    response.setPrimeFacesArguments(primefacesArgs);
                    response.setStatus(jqXHR['status']);
                    response.setComponentId(config.source);
                    response.jqXHR = jqXHR;
                    var result = onError.apply(scope, arguments);
                    _this.executeEvent('error', response);
                    _this.executeAlways('error', response);
                    return result;
                };

                var onSuccess = config.onsuccess || noOperation;
                config.onsuccess = function (jqXHR, textStatus, primefacesArgs) {
                    response.setPrimeFacesArguments(primefacesArgs);
                    response.setStatus(jqXHR['status']);
                    response.setComponentId(config.source);
                    response.jqXHR = jqXHR;
                    var result = onSuccess.apply(scope, arguments);
                    _this.executeEvent('success', response);
                    _this.executeAlways('success', response);
                    return result;
                };

                this.originalSendMethod.call(scope, config);
            };

            RequestBroker.prototype.widget = function (widgetVar) {
                return EVA.Components.PrimeFacesHelper.widget(widgetVar);
            };

            RequestBroker.prototype.handleAjaxError = function () {
                var _this = this;
                jQuery.ajaxSetup({
                    statusCode: {
                        500: function (jqXHR) {
                            var dialog = _this.widget(_this.errorDialogId);
                            var reponseError = $(jqXHR.responseText).find('.content-frame .content');

                            reponseError.find('#toggleStacktrace').show();
                            dialog.content.html(reponseError.html());
                            dialog.show();
                        }
                    }
                });
            };

            RequestBroker.prototype.handleExistingUrlParameter = function (key, url, value, parameterRegexp) {
                if (typeof value !== 'undefined' && value !== null) {
                    return url.replace(parameterRegexp, '$1' + key + "=" + value + '$2$3');
                } else {
                    var hash = url.split('#');
                    url = hash[0].replace(parameterRegexp, '$1$3').replace(/(&|\?)$/, '');
                    if (typeof hash[1] !== 'undefined' && hash[1] !== null) {
                        url += '#' + hash[1];
                    }
                    return url;
                }
            };

            RequestBroker.prototype.handleNonExistingUrlParameter = function (key, url, value) {
                if (typeof value !== 'undefined' && value !== null) {
                    var separator = url.indexOf('?') !== -1 ? '&' : '?', hash = url.split('#');
                    url = hash[0] + separator + key + '=' + value;
                    if (typeof hash[1] !== 'undefined' && hash[1] !== null) {
                        url += '#' + hash[1];
                    }
                    return url;
                } else {
                    return url;
                }
            };

            RequestBroker.createInstance = function () {
                if (_.isNull(instance) || _.isUndefined(instance)) {
                    instance = new RequestBroker();
                }

                return instance;
            };
            return RequestBroker;
        })();
        Components.RequestBroker = RequestBroker;
    })(EVA.Components || (EVA.Components = {}));
    var Components = EVA.Components;
})(EVA || (EVA = {}));
