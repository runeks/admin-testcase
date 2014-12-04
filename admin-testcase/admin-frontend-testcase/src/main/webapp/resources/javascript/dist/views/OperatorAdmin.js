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

        var OperatorAdmin = (function (_super) {
            __extends(OperatorAdmin, _super);
            function OperatorAdmin() {
                var _this = this;
                _super.call(this, { el: '#page-operator-admin' });
                this.buttons = [
                    'newOperator',
                    'j_idt158:searchButton',
                    'anotherOperatorForm:oneMoreOperator',
                    'editOperatorForm:j_idt133',
                    'editOperatorForm:j_idt136',
                    'editOperatorForm:j_idt135'
                ];

                this.onDocumentReady(function () {
                    _this.handleFilters();
                    _this.handleRequiredInput();
                });
            }
            OperatorAdmin.prototype.initialize = function () {
                var _this = this;
                _super.prototype.initialize.call(this);

                this.onDocumentReady(function () {
                    _this.waitButtonManager.attachButtonsById(_this.buttons);
                });
            };

            OperatorAdmin.prototype.setMode = function (mode) {
                this.mode = mode;
                this.initializeMode();
            };

            OperatorAdmin.prototype.validateOperator = function () {
                var _this = this;
                var enabled = true;
                var createOperatorButton = this.widget('createOperatorButton');
                var saveOperatorButton = this.widget('saveOperatorButton');

                this.$('.required input').each(function (i, element) {
                    if (_.isEmpty(_this.$(element).val())) {
                        enabled = false;
                    }
                });

                if (this.$('div.operator-role').length == 0) {
                    enabled = false;
                }

                if (createOperatorButton) {
                    createOperatorButton.jq.attr('disabled', !enabled);
                }
                if (saveOperatorButton) {
                    saveOperatorButton.jq.attr('disabled', !enabled);
                }
            };

            OperatorAdmin.prototype.initializeMode = function () {
                switch (this.mode) {
                    case OperatorAdmin.SEARCH:
                        this.handleSearchMode();
                        break;

                    case OperatorAdmin.EDIT:
                    case OperatorAdmin.BRAND_NEW:
                    case OperatorAdmin.NEW_FROM_ELECTORAL_ROLL:
                        this.handleEditAndCreateMode();
                        break;
                }

                var disableSearch = !(this.mode == OperatorAdmin.LIST || _.isEmpty(this.mode));
                this.disableSearchFilter(disableSearch);
            };

            OperatorAdmin.prototype.disableSearchFilter = function (disabled) {
                this.$el.find('#searchField').prop('disabled', disabled);
            };

            OperatorAdmin.prototype.getOperatorDataTableWidget = function () {
                return this.widget('operatorTable');
            };

            OperatorAdmin.prototype.getSearchField = function () {
                return this.$('#searchField');
            };

            OperatorAdmin.prototype.getGlobalFilter = function () {
                return this.$('input[id*=globalFilter]');
            };

            OperatorAdmin.prototype.getSelectRoleFilter = function () {
                return this.widget('widget_form_datatable_selectRole');
            };

            OperatorAdmin.prototype.getSelectPollingPlaceFilter = function () {
                return this.widget('widget_form_datatable_selectPollingPlace');
            };

            OperatorAdmin.prototype.clearFilters = function () {
                var _this = this;
                setTimeout(function () {
                    _this.getOperatorDataTableWidget().clearFilters();
                }, 0);
            };

            OperatorAdmin.prototype.handleFilters = function () {
                var _this = this;
                var pollingPlaceFilter = this.getSelectPollingPlaceFilter();
                var selectRoleFilter = this.getSelectRoleFilter();

                if (!pollingPlaceFilter && !selectRoleFilter) {
                    return;
                }

                pollingPlaceFilter.input.on('change', function () {
                    if (pollingPlaceFilter.getSelectedValue() == 0) {
                        _this.clearFilters();
                    }
                });

                selectRoleFilter.input.on('change', function () {
                    if (selectRoleFilter.getSelectedValue() == 0) {
                        _this.clearFilters();
                    }
                });

                this.$el.on('keydown', '#searchField', function (e) {
                    if (e.keyCode === OperatorAdmin.KEY_CODE_ENTER) {
                        e.preventDefault();
                        e.stopImmediatePropagation();
                        return false;
                    }
                });

                this.$el.on('keyup', '#searchField', function () {
                    _this.getGlobalFilter().val(_this.getSearchField().val());
                    _this.getOperatorDataTableWidget().filter();
                });

                this.$el.on('click', '#clearNameSearchField', function () {
                    if (_this.getSearchField().is(':disabled')) {
                        return;
                    }

                    console.log(_this.getGlobalFilter().val());

                    _this.getGlobalFilter().val('');
                    _this.getSearchField().val('');
                    _this.clearFilters();
                });
            };

            OperatorAdmin.prototype.handleRequiredInput = function () {
                var _this = this;
                this.$el.on('change', '.required input', function () {
                    _this.validateOperator();
                });
                this.$el.on('mouseleave', '.required input', function () {
                    _this.validateOperator();
                });
                this.validateOperator();
            };

            OperatorAdmin.prototype.handleEditAndCreateMode = function () {
                var _this = this;
                this.handleRoleDialog();

                this.$el.on('click', '.delete-role a', function () {
                    _this.validateOperator();
                });

                this.validateOperator();
            };

            OperatorAdmin.prototype.handleRoleDialog = function () {
                var _this = this;
                var roleAndLocationDialog = this.widget('createRoleAndLocationWidget');
                var dialogContent = roleAndLocationDialog.content;

                dialogContent.on('click', '.close-dialog', function (e) {
                    e.preventDefault();
                    e.stopImmediatePropagation();

                    roleAndLocationDialog.hide();
                    _this.validateOperator();

                    return false;
                });

                dialogContent.on('change', '.ui-datatable-tablewrapper .ui-radiobutton', function () {
                    var radioButton = dialogContent.find(event.target);
                    radioButton.parents('tbody').find('select').hide();
                    radioButton.parents('tr').find('select').show();
                });
            };

            OperatorAdmin.prototype.handleSearchMode = function () {
                var _this = this;
                var form = this.$el.find('form.search-operator-form');

                form.on('click.selectOneRadio', '.ui-radiobutton-box, input:text', function (e) {
                    var searchCriteriaWidget = _this.widget('searchCriteria');
                    var eventRow = searchCriteriaWidget.checkedRadio.closest('.row');

                    eventRow.find('input:text').removeAttr('disabled');

                    eventRow.siblings('.row').find('input:text').removeClass('ui-state-error').attr('disabled', 'disabled');
                });

                form.find('.ui-radiobutton-box:eq(0)').trigger('click');
            };
            OperatorAdmin.EDIT = "edit";
            OperatorAdmin.NEW_FROM_ELECTORAL_ROLL = "new";
            OperatorAdmin.BRAND_NEW = "brandNew";
            OperatorAdmin.SEARCH = "search";
            OperatorAdmin.LIST = "list";
            OperatorAdmin.KEY_CODE_ENTER = 13;
            return OperatorAdmin;
        })(EVA.Components.BaseView);
        View.OperatorAdmin = OperatorAdmin;
    })(EVA.View || (EVA.View = {}));
    var View = EVA.View;
})(EVA || (EVA = {}));
