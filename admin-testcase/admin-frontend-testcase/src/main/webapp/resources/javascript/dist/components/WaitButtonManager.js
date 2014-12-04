var EVA;
(function (EVA) {
    (function (Components) {
        "use strict";

        var instance = null;

        var WaitButtonManager = (function () {
            function WaitButtonManager() {
                this.handledButtons = {};
            }
            WaitButtonManager.prototype.attachButtons = function (buttonSelectors) {
                var _this = this;
                buttonSelectors.each(function (i, button) {
                    _this.attachButton($(button).attr('id'));
                });
            };

            WaitButtonManager.prototype.attachButtonsById = function (buttonIds) {
                var _this = this;
                _.each(buttonIds, function (id) {
                    _this.attachButton(id);
                });
            };

            WaitButtonManager.prototype.attachButton = function (buttonId) {
                var _this = this;
                if (!_.isUndefined(this.handledButtons[buttonId])) {
                    return;
                }

                this.handledButtons[buttonId] = true;

                EVA.Application.getInstance().getRequestBroker().attachOnStart(buttonId, function () {
                    _this.disableButton(buttonId);
                });

                EVA.Application.getInstance().getRequestBroker().attachOnComplete(buttonId, function () {
                    _this.enableButton(buttonId);
                });
            };

            WaitButtonManager.prototype.getButton = function (buttonSelector) {
                if (buttonSelector.indexOf(':')) {
                    buttonSelector = '#' + EVA.escapeClientId(buttonSelector);
                }

                var buttonResult = $(buttonSelector);

                if (buttonResult.length > 1) {
                    throw new RangeError('Too many results for ' + buttonSelector);
                }

                return buttonResult;
            };

            WaitButtonManager.prototype.enableButton = function (buttonId) {
                var button = this.getButton(buttonId);

                if (button.length === 0) {
                    return;
                }

                var icon = button.find('.ui-icon');
                if (icon.hasClass(WaitButtonManager.PLEASE_WAIT_CSS_CLASS)) {
                    icon.removeClass(WaitButtonManager.PLEASE_WAIT_CSS_CLASS);
                    button.removeAttr('disabled');
                }
            };

            WaitButtonManager.prototype.disableButton = function (buttonId) {
                var button = this.getButton(buttonId);

                if (button.length === 0) {
                    return;
                }

                button.find('.ui-icon').addClass(WaitButtonManager.PLEASE_WAIT_CSS_CLASS);
                button.attr('disabled', 'disabled');
            };

            WaitButtonManager.createInstance = function () {
                if (_.isNull(instance)) {
                    instance = new WaitButtonManager();
                }

                return instance;
            };
            WaitButtonManager.PLEASE_WAIT_CSS_CLASS = 'eva-icon-please-wait';
            return WaitButtonManager;
        })();
        Components.WaitButtonManager = WaitButtonManager;
    })(EVA.Components || (EVA.Components = {}));
    var Components = EVA.Components;
})(EVA || (EVA = {}));
