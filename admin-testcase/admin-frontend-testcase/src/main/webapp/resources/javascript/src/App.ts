/// <reference path="../definitions/jquery.d.ts"/>
/// <reference path="../definitions/jqueryui.d.ts"/>
/// <reference path="../definitions/backbone.d.ts"/>
/// <reference path="../definitions/underscore.d.ts"/>
/// <reference path="./components/BaseView.ts"/>
/// <reference path="./components/SessionTimeout.ts"/>



declare var PF:any;

module EVA {

	var applicationInstance:Application = null;
	var globalLoaderTimeout:number = null;

	export interface IApplicationConfiguration {
		csrfToken:string;
		conversationId:string;
		keepAlive:EVA.Components.ISessionTimeoutConfig;
	}
	
	
	function hasApplicationInstance():boolean {
		return !_.isNull(applicationInstance) && !_.isUndefined(applicationInstance);
	}
	
	export function escapeClientId(...args):string {
		return EVA.Components.PrimeFacesHelper.escapeClientId.apply(this, arguments);
	}

	export class Application extends EVA.Components.BaseView {

		private loaderDialog:string = 'loaderDialog';
		private view:EVA.Components.BaseView;
		private conversationId:string = null;
		private CSRFToken:string = null;
		private sessionTimeout:EVA.Components.SessionTimeout;

		constructor(config:IApplicationConfiguration) {
			super({el: 'body'});

			this.conversationId = config.conversationId;
			this.CSRFToken = config.csrfToken;
			
			if(config.hasOwnProperty('keepAlive') && typeof config.keepAlive == 'object') {
				this.sessionTimeout = new EVA.Components.SessionTimeout(config.keepAlive);
			}
			
			this.onDocumentReady(()=> {
				this.insertCSRFToken();
				this.attachCSRFHeader();
				this.bootstrap();
			});
		}

		/**
		 * This method handles setting a custom request header needed for all ajax requests.
		 * Candidate for moving this to {@link EVA.Components.RequestBroker}
		 */
		private attachCSRFHeader():void {

			jQuery.ajaxPrefilter((options:any, originalOptions:JQueryAjaxSettings, jqXHR:JQueryXHR)=> {
				jqXHR.setRequestHeader('X-CSRF-Token', this.CSRFToken);
			});
		}

		public getCSRFToken():string {
			return this.CSRFToken;
		}

		/**
		 * @param viewInstance {EVA.Components.BaseView}
		 */
		public setView(viewInstance:EVA.Components.BaseView):void {
			this.view = viewInstance;
		}

		/**
		 * @returns {EVA.Components.BaseView}
		 */
		public getView():EVA.Components.BaseView {
			return this.view;
		}

		/**
		 * This method handles calling a anonymous handler in the scope of the view when
		 * @param handler
		 */
		public onViewReady(handler:()=>void) {
			this.onDocumentReady(()=> {
				handler.call(this.getView());
			});
		}

		/**
		 * This method create a complete url. 
		 * @param viewPath {string} path to view without "/secure" and no trailing slash.
		 * @param appendConversationId {boolean}
		 * @returns {string}
		 */
		public createRoute(viewPath:string, appendConversationId:boolean = true):string {

			if (!viewPath) {
				return '';
			}

			var url:string = '/secure' + viewPath;

			if (appendConversationId && this.conversationId) {
				url = this.getRequestBroker().updateQueryString('cid', this.conversationId, url);
			}

			return url;
		}

		/**
		 * @param viewPath {string} path to view without "/secure" and no trailing slash.
		 * @param appendConversationId {boolean}
		 */
		public redirect(viewPath:string, appendConversationId:boolean = true):void {
			window.location.href = this.createRoute(viewPath, appendConversationId);
		}

		public showLoader():void {
			clearTimeout(globalLoaderTimeout);
			globalLoaderTimeout = setTimeout(() => {
				var dialog:IDialog = <IDialog>this.widget(this.loaderDialog);
				dialog.show();
			}, 1000);
		}

		public hideLoader():void {
			clearTimeout(globalLoaderTimeout);
			var dialog:IDialog = <IDialog>this.widget(this.loaderDialog);
			dialog.hide();
		}

		private bootstrap():void {

			this.determineCookieSupport();
			var viewName:string = this.$('.page-container > .page').attr('data-view');
			var view = this.resolveViewClass(viewName);
			if (!_.isUndefined(view)) {
				this.setView(new view());
			}

			if (this.conversationId) {
				var url:string = this.getRequestBroker().updateQueryString('cid', this.conversationId, window.location.href);
				if (url != window.location.href && typeof window.history.replaceState != 'undefined') {
					window.history.replaceState(null, document.title, url);
				}
			}
		}

		private insertCSRFToken():void {

			var insertCSRF = ()=>{
				this.$("form:not([data-csrf])").each((i:number, element:HTMLFormElement)=> {

					var form:JQuery = this.$(element);
					if (form.find('input[name="CSRFToken"]:hidden').length > 0) {
						return;
					}

					var csrfInput:JQuery = $(document.createElement('input'));
					csrfInput.attr('id', 'CSRFToken');
					csrfInput.attr('type', 'hidden');
					csrfInput.attr('name', 'CSRFToken');
					csrfInput.val(this.CSRFToken);

					form.append(csrfInput);
					form.attr('data-csrf','true');
				});
			};
			
			this.getRequestBroker().attachEvent('*','complete',()=>{
				insertCSRF();
			}, true);

			insertCSRF();
		}

		/**
		 * @param viewName {string}
		 * @returns {Function|undefined}
		 */
		private resolveViewClass(viewName:string):any {
			return EVA.View[viewName];
		}

		private isCookiesEnabled():boolean {
			return navigator.cookieEnabled
				|| ("cookie" in document && (document.cookie.length > 0 || (document.cookie = "test").indexOf
					.call(document.cookie, "test") > -1));
		}

		private determineCookieSupport():void {

			if (!this.isCookiesEnabled()) {
				this.onDocumentReady(()=> {
					jQuery('#no_cookies_msg').show();
				});
			}
		}

		/**
		 * @returns {EVA.Application}
		 */
		public static getInstance():Application {

			if (!hasApplicationInstance()) {
				throw "No application instance exists";
			}

			return applicationInstance;
		}

		/**
		 * This method attaches an event to the applications "HTML" context. At the time this comment
		 * was written, this was "body". Just like jQuery on means "body" now delegates the event for any 
		 * elements that matches.
		 * @param args
		 * @returns {JQuery}
		 */
		public static on(...args):JQuery {
			return applicationInstance.$el.on.apply(applicationInstance.$el, arguments);
		}

		/**
		 * This method attaches an event to the applications "HTML" context. At the time this comment
		 * was written, this was "body". Just like jQuery one means event only will fire once.
		 * @param args
		 * @returns {JQuery}
		 */
		public static one(...args):JQuery {
			return applicationInstance.$el.one.apply(applicationInstance.$el, arguments);
		}

		/**
		 * @param config {IApplicationConfiguration}
		 * @returns {Application}
		 */
		public static createInstance(config:IApplicationConfiguration):Application {

			if ((_.isUndefined(config) || !config.hasOwnProperty('csrfToken')) && !hasApplicationInstance()) {
				throw "Missing CSRFToken nessesary for creating Application instance";
			} else {
				applicationInstance = new Application(config);
				return applicationInstance;
			}
		}
	}
}