
(function (jQuery, PrimeFaces) {
    var errorMsg = [];
    if (typeof jQuery == 'undefined') {
        errorMsg.push('jQuery is missing! Please ensure that jquery is loaded before this file');
    }
    if (typeof PrimeFaces == 'undefined') {
        errorMsg.push('PrimeFaces is missing! Please ensure that PrimeFaces is loaded before this file');
    } else if (typeof PrimeFaces.widget.Dialog == 'undefined') {
        errorMsg.push('PrimeFaces Dialog is missing! Please ensure that the "Dialog" module is included and loaded before this file');
    }

    if (errorMsg.length > 0) {
        _.each(errorMsg, function (msg) {
            console.error(msg);
        });
        return;
    }

    var show = PrimeFaces.widget.Dialog.prototype.show;
    var hide = PrimeFaces.widget.Dialog.prototype.hide;
    var $htmlBody = jQuery('html,body');
    var dialogIdsThatAllowBodyScroll = ['loader', 'error'];

    function updateQueryString(key, value, url) {
        if (!url) {
            throw "No url supplied";
        }

        var parameterRegexp = new RegExp("([?&])" + key + "=.*?(&|#|$)(.*)", "gi");

        if (parameterRegexp.test(url)) {
            return handleExistingUrlParameter(url, value, parameterRegexp);
        } else {
            return handleNonExistingUrlParameter(url, value);
        }

        function handleNonExistingUrlParameter(url, value) {
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
        }

        function handleExistingUrlParameter(url, value, parameterRegexp) {
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
        }
    }

    PrimeFaces.widget.Dialog.prototype.hide = function () {
        hide.call(this);

        if (dialogIdsThatAllowBodyScroll.indexOf(this.id) == -1) {
            $htmlBody.css('overflow-y', '');
        }
    };

    PrimeFaces.widget.Dialog.prototype.show = function () {
        show.call(this);

        if (dialogIdsThatAllowBodyScroll.indexOf(this.id) == -1) {
            $htmlBody.css('overflow-y', 'hidden');
        }
    };

    PrimeFaces.widget.Dialog.prototype.fitViewport = function () {
        var _this = this;
        var winHeight = $(window).height(), contentPadding = this.content.innerHeight() - this.content.height();

        if (_this.jq.innerHeight() > winHeight) {
            this.content.height(winHeight - _this.titlebar.innerHeight() - _this.footer.innerHeight() - contentPadding);
        }
    };

    PrimeFaces.dialog.DialogHandler.openDialog = function (config) {
        var widgetId = config.sourceComponentId + "_dlg";
        if (document.getElementById(widgetId)) {
            return;
        }

        var widgetVar = config.sourceComponentId.replace(/:/g, "_") + "_dlgwidget";
        var $widget = $('<div id="' + widgetId + '" class="ui-dialog ui-widget ui-widget-content ui-corner-all ui-shadow ui-overlay-hidden" data-pfdlgcid="' + config.pfdlgcid + '" data-widgetvar="' + widgetVar + '"></div>').append('<div class="ui-dialog-titlebar ui-widget-header ui-helper-clearfix ui-corner-top"><span class="ui-dialog-title"></span></div>');

        if (config.options.closable !== false) {
            $widget.children(".ui-dialog-titlebar").append('<a class="ui-dialog-titlebar-icon ui-dialog-titlebar-close ui-corner-all" href="#" role="button"><span class="ui-icon ui-icon-closethick"></span></a>');
        }

        var $content = $('<div class="ui-dialog-content ui-widget-content" style="height: auto;"></div>');
        var $fragment = $(document.createDocumentFragment());
        var dialogUrl = updateQueryString('pfdlgcid', config.pfdlgcid, config.url);

        $fragment.load(dialogUrl, function (response, status) {
            if (status == 'success') {
                var htmlFragment = $fragment.get(0);
                var titleElement = htmlFragment.querySelector('title');
                var payLoadElement = htmlFragment.querySelector('.dialog-payload');

                $content.append(payLoadElement.innerHTML);
                $widget.find('.ui-dialog-title').html(titleElement.innerHTML);
                $widget.append($content);
                $widget.appendTo(document.body);

                var dialogConfig = {
                    id: widgetId,
                    position: config.options.position ? config.options.position : "center",
                    sourceComponentId: config.sourceComponentId,
                    sourceWidget: config.sourceWidget,
                    onHide: function () {
                        if (typeof config.options.onHide == 'function') {
                            config.options.onHide();
                        }
                        $widget.remove();
                        delete PrimeFaces.widgets[widgetVar];
                    },
                    modal: config.options.modal,
                    resizable: config.options.resizable,
                    draggable: config.options.draggable,
                    width: config.options.width,
                    height: config.options.height
                };

                PrimeFaces.cw("Dialog", widgetVar, dialogConfig);

                PF(widgetVar).show();
            } else if (status == 'error') {
                throw "Unable to load dialog from " + config.url + "(" + dialogUrl + ")";
            }
        });
    };
})(jQuery, PrimeFaces);
