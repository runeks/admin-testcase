/// <reference path="../../definitions/underscore.d.ts"/>
/// <reference path="../../definitions/jqueryui.d.ts"/>
/// <reference path="../../definitions/backbone.d.ts"/>
/// <reference path="../../definitions/jquery.d.ts"/>
/// <reference path="../components/BaseView.ts"/>
/// <reference path="../components/Response.ts"/>

module EVA.View {

	export interface ContextEditorLevel {
		edit:string;
		create:string;
		confirm:string;
	}

	export interface ContextEditorFormActions {
		save:JQuery;
		'delete':JQuery;
		cancel:JQuery;
	}

	export class ListAreas extends EVA.Components.BaseView {

		public static LEVEL1:ContextEditorLevel = {
			edit: 'editAreaLevel1Widget',
			create: 'createAreaLevel1Widget',
			confirm: 'level1Confirmation'
		};

		public static LEVEL2:ContextEditorLevel = {
			edit: 'editAreaLevel2Widget',
			create: 'createAreaLevel2Widget',
			confirm: 'level2Confirmation'
		};

		public static LEVEL3:ContextEditorLevel = {
			edit: 'editAreaLevel3Widget',
			create: 'createAreaLevel3Widget',
			confirm: 'level3Confirmation'
		};

		public static LEVEL4:ContextEditorLevel = {
			edit: 'editAreaLevel4Widget',
			create: 'createAreaLevel4Widget',
			confirm: 'level4Confirmation'
		};

		public static LEVEL5:ContextEditorLevel = {
			edit: 'editAreaLevel5Widget',
			create: 'createAreaLevel5Widget',
			confirm: 'level5Confirmation'
		};

		public static LEVEL6:ContextEditorLevel = {
			edit: 'editAreaLevel6Widget',
			create: 'createAreaLevel6Widget',
			confirm: 'level6Confirmation'
		};

		private levels:number[] = [1, 2, 3, 4, 5, 6];

		constructor() {
			super({el: '#page-configuration-list-areas'});

			this.onDocumentReady(()=> {
				this.attachEventHandlers();
			});
		}

		private attachEventHandlers():void {
			
			this.$el.on('click', '.context-level .context-actions a, .context-level .create-context a.create', (event:JQueryEventObject)=> {
				var target:JQuery = this.$(event.target);
				var currentLevel:ContextEditorLevel = this.resolveLevelForElement(target);
				this.attachOnComplete(target.attr('id'), ()=> {		
					if(target.hasClass('create')) {
						this.handleShowDialog(currentLevel.create);
					} else if(target.hasClass('edit') || target.hasClass('show')) {
						this.handleShowDialog(currentLevel.edit);
					}
				});
			});

			_.each(this.levels, (levelNumber:number)=> {
				var contextEditorLevel:ContextEditorLevel = this.getContextEditorLevel(levelNumber);
				this.attachEventsForContextLevel(contextEditorLevel);
			})
		}

		private attachEventsForContextLevel(contextEditorLevel:ContextEditorLevel):void {

			var editLevelWidget:any = this.widget(contextEditorLevel.edit);
			var createLevelWidget:any = this.widget(contextEditorLevel.create);

			this.attachEvensForEditWidget(editLevelWidget);
			this.attachEvensForCreateWidget(createLevelWidget);
		}

		private attachEvensForEditWidget(widgetInstance:any):void {

			var actions:ContextEditorFormActions = this.getFormActions(widgetInstance);

			if (actions.save.length === 1) {
				//only attach oncomplete handler once
				this.$el.one('click', '#' + this.escapeClientId(actions.save.attr('id')), ()=> {
					this.attachOnComplete(actions.save.attr('id'), (response:EVA.Components.Response)=> {
						if (!response.isValidationFailed()) {
							widgetInstance.hide();
						}
					});

				});
			}

			if (actions.delete.length === 1) {
				//only attach oncomplete handler once
				this.$el.one('click', '#' + this.escapeClientId(actions.delete.attr('id')), ()=> {
					this.attachOnComplete(actions.save.attr('id'), (response:EVA.Components.Response)=> {
						if (!response.isValidationFailed()) {
							widgetInstance.hide();
						}
					});
				});
			}

			if (actions.cancel.length === 1) {
				this.$el.on('click', '#' + this.escapeClientId(actions.cancel.attr('id')), (event:JQueryEventObject)=> {
					event.preventDefault();
					event.stopImmediatePropagation();
					widgetInstance.hide();
				});
			}
		}

		private attachEvensForCreateWidget(widgetInstance:any):void {

			var actions:ContextEditorFormActions = this.getFormActions(widgetInstance);

			if (actions.save.length === 1) {
				//only attach oncomplete handler once
				this.$el.one('click', '#' + this.escapeClientId(actions.save.attr('id')), ()=> {
					this.attachOnComplete(actions.save.attr('id'), (response:EVA.Components.Response)=> {
						if (!response.isValidationFailed()) {
							widgetInstance.hide();
						}
					});
				});
			}

			if (actions.cancel.length === 1) {
				this.$el.on('click', '#' + this.escapeClientId(actions.cancel.attr('id')), (event:JQueryEventObject)=> {
					event.preventDefault();
					event.stopImmediatePropagation();
					widgetInstance.hide();
				});
			}
		}

		private handleShowDialog(widgetVar:string):void {
			var dialog:IConfirmDialog = <IConfirmDialog>this.widget(widgetVar);
			dialog.show();
		}

		private resolveLevelForElement(element:JQuery):ContextEditorLevel {
			var id:string = element.closest('.context-level').attr('id');
			var levelNumber:string = id.substr(id.length - 1);
			return this.getContextEditorLevel(levelNumber);
		}

		private getContextEditorLevel(levelNumber:any):ContextEditorLevel {
			return ListAreas['LEVEL' + levelNumber.toString()];
		}

		private getFormActions(widgetInstance:any):ContextEditorFormActions {

			var buttons = widgetInstance.content.find('.form-actions .btn');
			return {
				save: buttons.filter('.btn-primary'),
				cancel: buttons.filter('.btn-link'),
				'delete': buttons.filter('.btn-danger')
			}
		}
	}
}