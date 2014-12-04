/// <reference path="../../definitions/underscore.d.ts"/>
/// <reference path="../../definitions/jqueryui.d.ts"/>
/// <reference path="../../definitions/backbone.d.ts"/>
/// <reference path="../../definitions/jquery.d.ts"/>
/// <reference path="../components/BaseView.ts"/>
/// <reference path="../components/Response.ts"/>
/// <reference path="../interfaces/PrimeFaces.ts"/>


declare var PrimeFaces:any;

module EVA.Components {

	var instance:RequestBroker = null;
	var noOperation:()=>void = function () {
	};

	export interface QueueItem {
		handler:{(response:Response):any}
		always:boolean;
	}

	class ComponentQueueModel extends Backbone.Model {

		constructor(componentId:string) {
			this.idAttribute = 'componentId';
			super();

			this.set({
				componentId: componentId,
				complete: [],
				start: [],
				success: [],
				error: []
			});
		}

		private executeEvent(queue:QueueItem[], response:EVA.Components.Response):QueueItem[] {
			
			_.each(queue, (item:QueueItem, index:number)=> {
				item.handler(response);
				
				if(!item.always) {
					queue.splice(index, 1);
				}
			});	
			
			return queue;
		}
		
		public error(response:EVA.Components.Response):void {
			var queue:QueueItem[] = this.executeEvent(this.get('error'), response);
			this.set('error', queue);
		}

		public success(response:EVA.Components.Response):void {
			var queue:QueueItem[] = this.executeEvent(this.get('success'), response);
			this.set('success', queue);
		}

		public complete(response:EVA.Components.Response):void {
			var queue:QueueItem[] = this.executeEvent(this.get('complete'), response);
			this.set('complete', queue);
		}

		public start(response:EVA.Components.Response):void {
			var queue:QueueItem[] = this.executeEvent(this.get('start'), response);
			this.set('start', queue);
		}
	}

	export class RequestBroker {

		private primeFacesHelper:PrimeFacesHelper;
		private errorDialogId:string = 'errorDialog';

		private originalSendMethod:(config:IPrimeFacesAjaxConfig)=>any;
		private componentQueue:Backbone.Collection<ComponentQueueModel>;

		constructor() {
			this.primeFacesHelper = new PrimeFacesHelper();
			this.componentQueue = new Backbone.Collection<ComponentQueueModel>();
			this.bootstrap();
			this.handleAjaxError();
		}

		public bootstrap():void {
			var _this:RequestBroker = this;
			this.originalSendMethod = PrimeFaces.ajax.Request.send;
			PrimeFaces.ajax.Request.send = function (config:IPrimeFacesAjaxConfig) {
				_this.sendRequest(config, <IPrimeFacesRequest>this);
			};
		}

		public attachEvent(componentId:string, eventName:string, handler:(response:Response)=>any, always:boolean = false):void {

			var queueModel = this.componentQueue.get(componentId);

			if (!queueModel) {
				queueModel = new ComponentQueueModel(componentId);
				this.componentQueue.add(queueModel);
			}

			var valueObject:Object = {};
			valueObject[eventName] = queueModel.get(eventName).concat(
				this.createQueueItem(handler, always)
			);

			queueModel.set(valueObject);
		}

		private executeEvent(eventName:string, response:EVA.Components.Response):void {
			var queueModel = this.componentQueue.get(response.getComponentId());

			if (!queueModel) {
				return;
			}

			queueModel[eventName].call(queueModel, response);
		}
		
		private executeAlways(eventName:string,response:EVA.Components.Response):void {
			var queueModel = this.componentQueue.get('*');
			
			if (!queueModel) {
				return;
			}
			
			queueModel[eventName].call(queueModel, response);
		}

		public attachOnComplete(componentId:string, handler:(response:Response)=>any):void {
			this.attachEvent(componentId, 'complete', handler);
		}

		public attachOnStart(componentId:string, handler:(response:Response)=>any):void {
			this.attachEvent(componentId, 'start', handler);
		}

		public attachOnError(componentId:string, handler:(response:Response)=>any):void {
			this.attachEvent(componentId, 'error', handler);
		}

		public attachOnSuccess(componentId:string, handler:(response:Response)=>any):void {
			this.attachEvent(componentId, 'success', handler);
		}

		public updateQueryString(key:string, value:string, url:string):string {

			if (!url) {
				throw "No url supplied";
			}

			var parameterRegexp = new RegExp("([?&])" + key + "=.*?(&|#|$)(.*)", "gi");

			if (parameterRegexp.test(url)) {
				return this.handleExistingUrlParameter(key, url, value, parameterRegexp);
			}
			else {
				return this.handleNonExistingUrlParameter(key, url, value);
			}
		}

		private createQueueItem(handler:(response:Response)=>any, alwaysExecute:boolean):QueueItem {
			return {
				handler: handler,
				always: alwaysExecute
			}
		}

		private sendRequest(config:IPrimeFacesAjaxConfig, scope:IPrimeFacesRequest):void {
			var response:Response = new Response();
			var onComplete:(jqXHR:JQueryAjaxSettings, textStatus:string, primefacesArgs:IPrimeFacesResponseArgs)=>any = config.oncomplete || noOperation;
			config.oncomplete = (jqXHR:JQueryAjaxSettings, textStatus:string, primefacesArgs:IPrimeFacesResponseArgs) => {
				response.setPrimeFacesArguments(primefacesArgs);
				response.setStatus(jqXHR['status']);
				response.setComponentId(config.source);
				response.jqXHR = jqXHR;
				var result:boolean = onComplete.apply(scope, arguments);
				this.executeEvent('complete', response);
				this.executeAlways('complete', response);

				return result;
			};

			var onStart:(jqXHR:JQueryAjaxSettings, textStatus:string, primefacesArgs:IPrimeFacesResponseArgs)=>any = config.onstart || noOperation;
			config.onstart = (jqXHR:JQueryAjaxSettings, textStatus:string, primefacesArgs:IPrimeFacesResponseArgs) => {
				response.setPrimeFacesArguments(primefacesArgs);
				response.setStatus(jqXHR['status']);
				response.setComponentId(config.source);
				response.jqXHR = jqXHR;
				var result:boolean = onStart.apply(scope, arguments);
				this.executeEvent('start', response);
				this.executeAlways('start', response);
				return result;
			};

			var onError:(jqXHR:JQueryAjaxSettings, textStatus:string, primefacesArgs:IPrimeFacesResponseArgs)=>any = config.onerror || noOperation;
			config.onerror = (jqXHR:JQueryAjaxSettings, textStatus:string, primefacesArgs:IPrimeFacesResponseArgs) => {
				response.setPrimeFacesArguments(primefacesArgs);
				response.setStatus(jqXHR['status']);
				response.setComponentId(config.source);
				response.jqXHR = jqXHR;
				var result:boolean = onError.apply(scope, arguments);
				this.executeEvent('error', response);
				this.executeAlways('error', response);
				return result;
			};

			var onSuccess:(jqXHR:JQueryAjaxSettings, textStatus:string, primefacesArgs:IPrimeFacesResponseArgs)=>any = config.onsuccess || noOperation;
			config.onsuccess = (jqXHR:JQueryAjaxSettings, textStatus:string, primefacesArgs:IPrimeFacesResponseArgs) => {
				response.setPrimeFacesArguments(primefacesArgs);
				response.setStatus(jqXHR['status']);
				response.setComponentId(config.source);
				response.jqXHR = jqXHR;
				var result:boolean = onSuccess.apply(scope, arguments);
				this.executeEvent('success', response);
				this.executeAlways('success', response);
				return result;
			};

			this.originalSendMethod.call(scope, config);
		}

		private widget(widgetVar:string):IPrimeFacesWidget {
			return EVA.Components.PrimeFacesHelper.widget(widgetVar);
		}

		private handleAjaxError():void {
			jQuery.ajaxSetup(<any>{
				statusCode: {
					500: (jqXHR:JQueryXHR)=> {
						var dialog:any = this.widget(this.errorDialogId);
						var reponseError:JQuery = $(jqXHR.responseText).find('.content-frame .content');

						reponseError.find('#toggleStacktrace').show();
						dialog.content.html(reponseError.html());
						dialog.show();
					}
				}
			});
		}

		private handleExistingUrlParameter(key:string, url:string, value:string, parameterRegexp:RegExp):string {

			if (typeof value !== 'undefined' && value !== null) {
				return url.replace(parameterRegexp, '$1' + key + "=" + value + '$2$3');
			} else {
				var hash = url.split('#');
				url = hash[0].replace(parameterRegexp, '$1$3').replace(/(&|\?)$/, '');
				if (typeof hash[1] !== 'undefined' && hash[1] !== null) {
					url += '#' + hash[1];
				}
				return url;
			}
		}

		private handleNonExistingUrlParameter(key:string, url:string, value:string):string {
			if (typeof value !== 'undefined' && value !== null) {
				var separator = url.indexOf('?') !== -1 ? '&' : '?',
					hash = url.split('#');
				url = hash[0] + separator + key + '=' + value;
				if (typeof hash[1] !== 'undefined' && hash[1] !== null) {
					url += '#' + hash[1];
				}
				return url;
			}
			else {
				return url;
			}
		}

		public static createInstance():RequestBroker {

			if (_.isNull(instance) || _.isUndefined(instance)) {
				instance = new RequestBroker();
			}

			return instance;
		}
	}
}