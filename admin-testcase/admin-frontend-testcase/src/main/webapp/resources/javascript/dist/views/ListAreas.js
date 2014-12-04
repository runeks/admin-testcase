var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
var EVA;
(function (EVA) {
    (function (View) {
        var ListAreas = (function (_super) {
            __extends(ListAreas, _super);
            function ListAreas() {
                var _this = this;
                _super.call(this, { el: '#page-configuration-list-areas' });
                this.levels = [1, 2, 3, 4, 5, 6];

                this.onDocumentReady(function () {
                    _this.attachEventHandlers();
                });
            }
            ListAreas.prototype.attachEventHandlers = function () {
                var _this = this;
                this.$el.on('click', '.context-level .context-actions a, .context-level .create-context a.create', function (event) {
                    var target = _this.$(event.target);
                    var currentLevel = _this.resolveLevelForElement(target);
                    _this.attachOnComplete(target.attr('id'), function () {
                        if (target.hasClass('create')) {
                            _this.handleShowDialog(currentLevel.create);
                        } else if (target.hasClass('edit') || target.hasClass('show')) {
                            _this.handleShowDialog(currentLevel.edit);
                        }
                    });
                });

                _.each(this.levels, function (levelNumber) {
                    var contextEditorLevel = _this.getContextEditorLevel(levelNumber);
                    _this.attachEventsForContextLevel(contextEditorLevel);
                });
            };

            ListAreas.prototype.attachEventsForContextLevel = function (contextEditorLevel) {
                var editLevelWidget = this.widget(contextEditorLevel.edit);
                var createLevelWidget = this.widget(contextEditorLevel.create);

                this.attachEvensForEditWidget(editLevelWidget);
                this.attachEvensForCreateWidget(createLevelWidget);
            };

            ListAreas.prototype.attachEvensForEditWidget = function (widgetInstance) {
                var _this = this;
                var actions = this.getFormActions(widgetInstance);

                if (actions.save.length === 1) {
                    this.$el.one('click', '#' + this.escapeClientId(actions.save.attr('id')), function () {
                        _this.attachOnComplete(actions.save.attr('id'), function (response) {
                            if (!response.isValidationFailed()) {
                                widgetInstance.hide();
                            }
                        });
                    });
                }

                if (actions.delete.length === 1) {
                    this.$el.one('click', '#' + this.escapeClientId(actions.delete.attr('id')), function () {
                        _this.attachOnComplete(actions.save.attr('id'), function (response) {
                            if (!response.isValidationFailed()) {
                                widgetInstance.hide();
                            }
                        });
                    });
                }

                if (actions.cancel.length === 1) {
                    this.$el.on('click', '#' + this.escapeClientId(actions.cancel.attr('id')), function (event) {
                        event.preventDefault();
                        event.stopImmediatePropagation();
                        widgetInstance.hide();
                    });
                }
            };

            ListAreas.prototype.attachEvensForCreateWidget = function (widgetInstance) {
                var _this = this;
                var actions = this.getFormActions(widgetInstance);

                if (actions.save.length === 1) {
                    this.$el.one('click', '#' + this.escapeClientId(actions.save.attr('id')), function () {
                        _this.attachOnComplete(actions.save.attr('id'), function (response) {
                            if (!response.isValidationFailed()) {
                                widgetInstance.hide();
                            }
                        });
                    });
                }

                if (actions.cancel.length === 1) {
                    this.$el.on('click', '#' + this.escapeClientId(actions.cancel.attr('id')), function (event) {
                        event.preventDefault();
                        event.stopImmediatePropagation();
                        widgetInstance.hide();
                    });
                }
            };

            ListAreas.prototype.handleShowDialog = function (widgetVar) {
                var dialog = this.widget(widgetVar);
                dialog.show();
            };

            ListAreas.prototype.resolveLevelForElement = function (element) {
                var id = element.closest('.context-level').attr('id');
                var levelNumber = id.substr(id.length - 1);
                return this.getContextEditorLevel(levelNumber);
            };

            ListAreas.prototype.getContextEditorLevel = function (levelNumber) {
                return ListAreas['LEVEL' + levelNumber.toString()];
            };

            ListAreas.prototype.getFormActions = function (widgetInstance) {
                var buttons = widgetInstance.content.find('.form-actions .btn');
                return {
                    save: buttons.filter('.btn-primary'),
                    cancel: buttons.filter('.btn-link'),
                    'delete': buttons.filter('.btn-danger')
                };
            };
            ListAreas.LEVEL1 = {
                edit: 'editAreaLevel1Widget',
                create: 'createAreaLevel1Widget',
                confirm: 'level1Confirmation'
            };

            ListAreas.LEVEL2 = {
                edit: 'editAreaLevel2Widget',
                create: 'createAreaLevel2Widget',
                confirm: 'level2Confirmation'
            };

            ListAreas.LEVEL3 = {
                edit: 'editAreaLevel3Widget',
                create: 'createAreaLevel3Widget',
                confirm: 'level3Confirmation'
            };

            ListAreas.LEVEL4 = {
                edit: 'editAreaLevel4Widget',
                create: 'createAreaLevel4Widget',
                confirm: 'level4Confirmation'
            };

            ListAreas.LEVEL5 = {
                edit: 'editAreaLevel5Widget',
                create: 'createAreaLevel5Widget',
                confirm: 'level5Confirmation'
            };

            ListAreas.LEVEL6 = {
                edit: 'editAreaLevel6Widget',
                create: 'createAreaLevel6Widget',
                confirm: 'level6Confirmation'
            };
            return ListAreas;
        })(EVA.Components.BaseView);
        View.ListAreas = ListAreas;
    })(EVA.View || (EVA.View = {}));
    var View = EVA.View;
})(EVA || (EVA = {}));
