/// <reference path="../../definitions/jquery.d.ts"/>
/// <reference path="../../definitions/underscore.d.ts"/>
/// <reference path="../../definitions/jqueryui.d.ts"/>
/// <reference path="../interfaces/IPCComponent.ts"/>
/// <reference path="./../primefaces/PrimeFacesHelper.ts"/>
/// <reference path="./RequestBroker.ts"/>
/// <reference path="./Response.ts"/>


module EVA.Components {

	var requestBroker:RequestBroker;
	var documentReadyStates:string[] = [
		'uninitialized',
		'loading',
		'interactive',
		//'complete', <-- we dont want this in the list.
	];

	/**
	 * Helper method to determine if we are running
	 * in test mode with karma.
	 * @returns {boolean}
	 */
	function isTest():boolean {
		return '__karma__' in window;
	}

	export class BaseView extends Backbone.View<BaseView> implements IPCComponent {

		public waitButtonManager:EVA.Components.WaitButtonManager;

		constructor(options?:Backbone.ViewOptions<any>) {
			super(options);
			this.getRequestBroker();
		}

		/**
		 * Standard initialize method that Backbone.View calls after the {@link constructor} method
		 * has been called. If this method is overwritten in a subclass, the behavior defined below is
		 * wished you need to call super.initialize() normally before doing anything else.
		 */
		public initialize():void {
			this.onDocumentReady(()=> {
				this.waitButtonManager = EVA.Components.WaitButtonManager.createInstance();
			});
		}

		public onFirst(eventName:string, handler:()=>any):JQuery {
			return this.$el.bindFirst(eventName, handler);
		}

		/**
		 * Helper method for resolving a Primefaces widget based on its defined widgetVar
		 * @param widgetVar
		 * @returns {IPrimeFacesWidget}
		 */
		public widget(widgetVar:string):IPrimeFacesWidget {
			return EVA.Components.PrimeFacesHelper.widget(widgetVar);
		}

		/**
		 * Resolves a Primefaces widget instance based on a single element found within it.
		 * @param eventOrElement
		 * @returns {IPrimeFacesWidget}
		 */
		public resolveWidget(eventOrElement:any):any {
			return EVA.Components.PrimeFacesHelper.resolveWidget(eventOrElement);
		}

		/**
		 * Attaches a oncomplete event for the ajax request sent for an component identified by its id attribute
		 * @param componentId {string}
		 * @param handler {Function}
		 * @returns {EVA.Components.BaseView}
		 */
		public attachOnComplete(componentId:string, handler:(response:Response)=>any):BaseView {
			this.getRequestBroker().attachOnComplete(componentId, handler);
			return this;
		}

		/**
		 * Attaches a onstart event for the ajax request sent for an component identified by its id attribute
		 * @param componentId {string}
		 * @param handler {Function}
		 * @returns {EVA.Components.BaseView}
		 */
		public attachOnStart(componentId:string, handler:(response:Response)=>any):BaseView {
			this.getRequestBroker().attachOnStart(componentId, handler);
			return this;
		}

		/**
		 * Attaches a onerror event for the ajax request sent for an component identified by its id attribute
		 * @param componentId {string}
		 * @param handler {Function}
		 * @returns {EVA.Components.BaseView}
		 */
		public attachOnError(componentId:string, handler:(response:Response)=>any):BaseView {
			this.getRequestBroker().attachOnError(componentId, handler);
			return this;
		}

		/**
		 * Performs escaping of a string componentId. Default JSF uses : as a separator. This is configurable,
		 * in both JSF, and in the PrimeFacesHelper class.
		 * @param componentId {string}
		 * @returns {string}
		 */
		public escapeClientId(componentId:string):string {
			return "#" + PrimeFacesHelper.escapeClientId(componentId);
		}

		/**
		 * Returns the RequestBroker singleton. See {@link EVA.Components.RequestBroker} for more
		 * information about this component.
		 * @returns {RequestBroker}
		 */
		public getRequestBroker():RequestBroker {
			if (_.isUndefined(requestBroker) || _.isNull(requestBroker)) {
				requestBroker = RequestBroker.createInstance();
			}

			return requestBroker;
		}

		/**
		 * This method is responsible for attaching document.ready handlers.
		 * ready handlers will execute if document.readyState == complete
		 * @param handler
		 */
		public onDocumentReady(handler:()=>void):BaseView {

			if (!isTest() && documentReadyStates.indexOf(document.readyState) != -1) {
				jQuery(document).ready(handler);
			} else {
				handler();
			}

			return this;
		}
	}
} 