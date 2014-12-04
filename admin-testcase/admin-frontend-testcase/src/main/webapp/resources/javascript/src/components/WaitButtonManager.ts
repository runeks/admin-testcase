/// <reference path="../../definitions/jquery.d.ts"/>
/// <reference path="../../definitions/underscore.d.ts"/>
/// <reference path="../../definitions/jqueryui.d.ts"/>
/// <reference path="../../definitions/backbone.d.ts"/>
/// <reference path="./BaseView.ts"/>
/// <reference path="./RequestBroker.ts"/>

module EVA.Components {

	"use strict";

	var instance:WaitButtonManager = null;

	export class WaitButtonManager {

		public static PLEASE_WAIT_CSS_CLASS = 'eva-icon-please-wait';

		private handledButtons:{
			[s:string]:boolean
		} = {};

		/**
		 * @param buttonSelectors
		 */
		public attachButtons(buttonSelectors:JQuery):void {
			buttonSelectors.each((i:number,button:HTMLElement)=>{
				this.attachButton($(button).attr('id'));
			});
		}
		
		public attachButtonsById(buttonIds:string[]):void {
			_.each(buttonIds, (id:string)=>{
				this.attachButton(id);
			})
		}

		public attachButton(buttonId:string):void {

			//button handled already, just return
			if (!_.isUndefined(this.handledButtons[buttonId])) {
				return;
			}
			
			this.handledButtons[buttonId] = true;

			EVA.Application.getInstance().getRequestBroker().attachOnStart(buttonId, ()=> {
				this.disableButton(buttonId);
			});

			EVA.Application.getInstance().getRequestBroker().attachOnComplete(buttonId, ()=> {
				this.enableButton(buttonId);
			});
		}

		private getButton(buttonSelector:string):JQuery {
			
			if(buttonSelector.indexOf(':')) {
				buttonSelector = '#' + EVA.escapeClientId(buttonSelector);
			}
			
			var buttonResult = $(buttonSelector);
			
			if(buttonResult.length > 1) {
				throw new RangeError('Too many results for ' + buttonSelector);
			}
			
			return buttonResult;
		}

		/**
		 * @param buttonId
		 */
		private enableButton(buttonId:string):void {
			
			var button:JQuery = this.getButton(buttonId);

			if (button.length === 0) {
				return;
			}

			var icon:JQuery = button.find('.ui-icon');
			if(icon.hasClass(WaitButtonManager.PLEASE_WAIT_CSS_CLASS)) {
				icon.removeClass(WaitButtonManager.PLEASE_WAIT_CSS_CLASS);
				button.removeAttr('disabled');
			}
		}

		/**
		 * @param buttonId
		 */
		private disableButton(buttonId:string):void {
			var button:JQuery = this.getButton(buttonId);

			if (button.length === 0) {
				return;
			}

			button.find('.ui-icon').addClass(WaitButtonManager.PLEASE_WAIT_CSS_CLASS);
			button.attr('disabled', 'disabled');
		}

		/**
		 * @returns {WaitButtonManager}
		 */
		public static createInstance():WaitButtonManager {

			if (_.isNull(instance)) {
				instance = new WaitButtonManager();
			}

			return instance;
		}
	}

}