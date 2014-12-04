/// <reference path="../../definitions/underscore.d.ts"/>
/// <reference path="../../definitions/jqueryui.d.ts"/>
/// <reference path="../../definitions/backbone.d.ts"/>
/// <reference path="../../definitions/jquery.d.ts"/>
/// <reference path="../components/BaseView.ts"/>
/// <reference path="../interfaces/PrimeFaces.ts"/>
/// <reference path="../primefaces/PrimeFacesHelper.ts"/>

module EVA.View {

	'use strict';

	export class LocalConfiguration extends EVA.Components.BaseView {

		public static CENTRAL_OVERVIEW_TAB_INDEX:number = 0;
		public static AREA_TAB_INDEX:number = 1;
		public static ELECTION_TAB_INDEX:number = 2;
		public static NO_CENTRAL_AREA_TAB_INDEX:number = 0;
		public static NO_CENTRAL_ELECTION_TAB_INDEX:number = 1;

		public static AREA_CONFIGURATION:string = '/local_area_configuration.xhtml';
		public static ELECTION_CONFIGURATION:string = '/local_election_configuration.xhtml';
		public static CENTRAL_CONFIGURATION:string = '/central_configuration_overview.xhtml';

		
		public buttons:string[] = [
			'localAreaConfiguration:configurationTabView:j_idt242',
			'localAreaConfiguration:configurationTabView:j_idt243',
			'localElectionConfiguration:configurationTabView:j_idt152'
		];
		
		constructor() {
			super({el: '#page-local-area-configuration, #page-local-election-configuration'});
		}

		public initialize():void {
			
			super.initialize();
			
			this.onDocumentReady(()=> {
				this.setupTabs();
				this.waitButtonManager.attachButtonsById(this.buttons);
			});
		}

		private getTabViewWidget():ITabView {
			return <ITabView>this.widget('tabViewWidget');
		}

		private getConfirmDialog():IConfirmDialog {
			return <IConfirmDialog>this.widget('centralConfirmDialog');
		}

		private setupTabs():void {


			var tabViewWidget:ITabView = this.getTabViewWidget();

			if (!tabViewWidget) {
				return;
			}

			tabViewWidget.cfg.onTabChange = (targetTab:number)=> {
				return this.onTabChange(tabViewWidget.getActiveIndex(), targetTab);
			};
		}

		private hasCentralConfigurationTab():boolean {
			return this.getTabViewWidget().getLength() === 3;
		}

		public onTabChange(activeIndex:number, targetTab:number):boolean {

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
		}

		private promptUser():void {

			var dialog:IConfirmDialog = this.getConfirmDialog();

			EVA.Application.one('click', this.escapeClientId(dialog.id) + ' .btn', (event:JQueryEventObject)=> {

				event.preventDefault();
				event.stopImmediatePropagation();

				var targetElement:Element = <Element>event.currentTarget;
				var button = dialog.footer.find(this.escapeClientId(targetElement.getAttribute('id')));

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
		}
	}
}