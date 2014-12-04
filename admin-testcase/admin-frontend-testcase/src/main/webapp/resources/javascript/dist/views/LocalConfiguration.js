var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
var EVA;
(function (EVA) {
    (function (View) {
        'use strict';

        var LocalConfiguration = (function (_super) {
            __extends(LocalConfiguration, _super);
            function LocalConfiguration() {
                _super.call(this, { el: '#page-local-area-configuration, #page-local-election-configuration' });
                this.buttons = [
                    'localAreaConfiguration:configurationTabView:j_idt242',
                    'localAreaConfiguration:configurationTabView:j_idt243',
                    'localElectionConfiguration:configurationTabView:j_idt152'
                ];
            }
            LocalConfiguration.prototype.initialize = function () {
                var _this = this;
                _super.prototype.initialize.call(this);

                this.onDocumentReady(function () {
                    _this.setupTabs();
                    _this.waitButtonManager.attachButtonsById(_this.buttons);
                });
            };

            LocalConfiguration.prototype.getTabViewWidget = function () {
                return this.widget('tabViewWidget');
            };

            LocalConfiguration.prototype.getConfirmDialog = function () {
                return this.widget('centralConfirmDialog');
            };

            LocalConfiguration.prototype.setupTabs = function () {
                var _this = this;
                var tabViewWidget = this.getTabViewWidget();

                if (!tabViewWidget) {
                    return;
                }

                tabViewWidget.cfg.onTabChange = function (targetTab) {
                    return _this.onTabChange(tabViewWidget.getActiveIndex(), targetTab);
                };
            };

            LocalConfiguration.prototype.hasCentralConfigurationTab = function () {
                return this.getTabViewWidget().getLength() === 3;
            };

            LocalConfiguration.prototype.onTabChange = function (activeIndex, targetTab) {
                if (activeIndex === targetTab) {
                    return false;
                }

                if (!this.hasCentralConfigurationTab()) {
                    if (targetTab == LocalConfiguration.NO_CENTRAL_ELECTION_TAB_INDEX) {
                        EVA.Application.getInstance().redirect(LocalConfiguration.ELECTION_CONFIGURATION, false);
                    } else if (targetTab === LocalConfiguration.NO_CENTRAL_AREA_TAB_INDEX) {
                        EVA.Application.getInstance().redirect(LocalConfiguration.AREA_CONFIGURATION, false);
                    } else {
                        throw "Unhandled tab";
                    }
                } else {
                    if (targetTab == LocalConfiguration.ELECTION_TAB_INDEX) {
                        EVA.Application.getInstance().redirect(LocalConfiguration.ELECTION_CONFIGURATION, false);
                    } else if (targetTab == LocalConfiguration.AREA_TAB_INDEX) {
                        EVA.Application.getInstance().redirect(LocalConfiguration.AREA_CONFIGURATION, false);
                    } else if (targetTab == LocalConfiguration.CENTRAL_OVERVIEW_TAB_INDEX) {
                        this.promptUser();
                    } else {
                        throw "Unhandled tab";
                    }
                }

                return false;
            };

            LocalConfiguration.prototype.promptUser = function () {
                var _this = this;
                var dialog = this.getConfirmDialog();

                EVA.Application.one('click', this.escapeClientId(dialog.id) + ' .btn', function (event) {
                    event.preventDefault();
                    event.stopImmediatePropagation();

                    var targetElement = event.currentTarget;
                    var button = dialog.footer.find(_this.escapeClientId(targetElement.getAttribute('id')));

                    if (button.hasClass('cancel')) {
                        dialog.hide();
                    } else if (button.hasClass('confirm')) {
                        EVA.Application.getInstance().redirect(LocalConfiguration.CENTRAL_CONFIGURATION, false);
                    } else {
                        throw "Unrecognized action";
                    }

                    return false;
                });

                if (dialog) {
                    dialog.show();
                }
            };
            LocalConfiguration.CENTRAL_OVERVIEW_TAB_INDEX = 0;
            LocalConfiguration.AREA_TAB_INDEX = 1;
            LocalConfiguration.ELECTION_TAB_INDEX = 2;
            LocalConfiguration.NO_CENTRAL_AREA_TAB_INDEX = 0;
            LocalConfiguration.NO_CENTRAL_ELECTION_TAB_INDEX = 1;

            LocalConfiguration.AREA_CONFIGURATION = '/local_area_configuration.xhtml';
            LocalConfiguration.ELECTION_CONFIGURATION = '/local_election_configuration.xhtml';
            LocalConfiguration.CENTRAL_CONFIGURATION = '/central_configuration_overview.xhtml';
            return LocalConfiguration;
        })(EVA.Components.BaseView);
        View.LocalConfiguration = LocalConfiguration;
    })(EVA.View || (EVA.View = {}));
    var View = EVA.View;
})(EVA || (EVA = {}));
