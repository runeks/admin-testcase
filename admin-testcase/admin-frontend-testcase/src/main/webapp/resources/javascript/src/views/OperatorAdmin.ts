/// <reference path="../../definitions/underscore.d.ts"/>
/// <reference path="../../definitions/jqueryui.d.ts"/>
/// <reference path="../../definitions/backbone.d.ts"/>
/// <reference path="../../definitions/jquery.d.ts"/>
/// <reference path="../components/BaseView.ts"/>

module EVA.View {

	'use strict';

	export class OperatorAdmin extends EVA.Components.BaseView {

		public buttons:string[] = [
			'newOperator',
			'j_idt158:searchButton',
			'anotherOperatorForm:oneMoreOperator',
			'editOperatorForm:j_idt133',
			'editOperatorForm:j_idt136',
			'editOperatorForm:j_idt135'
		];
		
		public static EDIT:string = "edit";
		public static NEW_FROM_ELECTORAL_ROLL:string = "new";
		public static BRAND_NEW:string = "brandNew";
		public static SEARCH:string = "search";
		public static LIST:string = "list";
		public static KEY_CODE_ENTER:number = 13;
//		public static CANDIDATES:string = "candidates";
//		public static EXISTING_OPERATOR:string = "existingOperator";
//		public static CREATED:string = "created";
//		public static CANDIDATE_DETAILS:string = "candidateDetails";
		private mode:string;

		constructor() {
			super({el: '#page-operator-admin'});

			this.onDocumentReady(()=> {
				this.handleFilters();
				this.handleRequiredInput();
			});
		}
		
		public initialize():void {
			super.initialize();
			
			this.onDocumentReady(()=>{
				this.waitButtonManager.attachButtonsById(this.buttons);	
			});
		}

		public setMode(mode:string):void {
			this.mode = mode;
			this.initializeMode();
		}

		public validateOperator():void {

			var enabled:boolean = true;
			var createOperatorButton:any = this.widget('createOperatorButton');
			var saveOperatorButton:any = this.widget('saveOperatorButton');

			this.$('.required input').each((i:number, element:HTMLElement)=> {
				if (_.isEmpty(this.$(element).val())) {
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
		}

		private initializeMode():void {

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
		}

		private disableSearchFilter(disabled:boolean):void {
			this.$el.find('#searchField').prop('disabled', disabled);
		}

		private getOperatorDataTableWidget():IDataTable {
			return <IDataTable>this.widget('operatorTable');
		}
		
		private getSearchField():JQuery {
			return this.$('#searchField');
		}
		
		private getGlobalFilter():JQuery {
			return this.$('input[id*=globalFilter]');
		}
		
		private getSelectRoleFilter():ISelectOneMenu {
			return <ISelectOneMenu>this.widget('widget_form_datatable_selectRole');
		}
		
		private getSelectPollingPlaceFilter():ISelectOneMenu {
			return <ISelectOneMenu>this.widget('widget_form_datatable_selectPollingPlace');
		}
		
		private clearFilters():void {
			setTimeout(()=>{
				this.getOperatorDataTableWidget().clearFilters();
			},0);
		}
		
		private handleFilters():void {

			var pollingPlaceFilter:ISelectOneMenu = this.getSelectPollingPlaceFilter();
			var selectRoleFilter:ISelectOneMenu = this.getSelectRoleFilter();
			
			if(!pollingPlaceFilter && !selectRoleFilter) {
				return;
			}
			
			pollingPlaceFilter.input.on('change',()=>{
				if(pollingPlaceFilter.getSelectedValue() == 0) {
					this.clearFilters();
				}
			});
			
			selectRoleFilter.input.on('change',()=>{
				if(selectRoleFilter.getSelectedValue() == 0) {
					this.clearFilters();
				}
			});
			
			this.$el.on('keydown', '#searchField', (e:JQueryEventObject)=> {
				if (e.keyCode === OperatorAdmin.KEY_CODE_ENTER) {
					e.preventDefault();
					e.stopImmediatePropagation();
					return false;
				}
			});
			
			this.$el.on('keyup', '#searchField', ()=> {
				this.getGlobalFilter().val(this.getSearchField().val());
				this.getOperatorDataTableWidget().filter();
			});

			this.$el.on('click', '#clearNameSearchField', ()=> {
				
				if (this.getSearchField().is(':disabled')) {
					return;
				}

				console.log(this.getGlobalFilter().val());
				
				this.getGlobalFilter().val('');
				this.getSearchField().val('');
				this.clearFilters();
			});
		}

		private handleRequiredInput():void {
			this.$el.on('change', '.required input', ()=> {
				this.validateOperator();
			});
			this.$el.on('mouseleave', '.required input', ()=> {
				this.validateOperator();
			});
			this.validateOperator();
		}

		private handleEditAndCreateMode():void {

			this.handleRoleDialog();

			this.$el.on('click', '.delete-role a', ()=> {
				this.validateOperator();
			});

			this.validateOperator();
		}

		private handleRoleDialog():void {
			var roleAndLocationDialog:any = this.widget('createRoleAndLocationWidget');
			var dialogContent:JQuery = roleAndLocationDialog.content;

			dialogContent.on('click', '.close-dialog', (e:JQueryEventObject)=> {

				e.preventDefault();
				e.stopImmediatePropagation();

				roleAndLocationDialog.hide();
				this.validateOperator();

				return false;
			});

			dialogContent.on('change', '.ui-datatable-tablewrapper .ui-radiobutton', ()=> {
				var radioButton:JQuery = dialogContent.find(<HTMLElement>event.target);
				radioButton.parents('tbody').find('select').hide();
				radioButton.parents('tr').find('select').show();
			});
		}

		private handleSearchMode():void {

			var form:JQuery = this.$el.find('form.search-operator-form');

			form.on('click.selectOneRadio', '.ui-radiobutton-box, input:text', (e:JQueryEventObject) => {

				var searchCriteriaWidget:any = this.widget('searchCriteria');
				var eventRow:JQuery = searchCriteriaWidget.checkedRadio.closest('.row');

				eventRow.find('input:text').removeAttr('disabled');

				eventRow
					.siblings('.row')
					.find('input:text')
					.removeClass('ui-state-error')
					.attr('disabled', 'disabled');


			});

			//call function so that fnr gets selected and other text inputs gets deactivated
			form.find('.ui-radiobutton-box:eq(0)').trigger('click');
		}
	}
}
