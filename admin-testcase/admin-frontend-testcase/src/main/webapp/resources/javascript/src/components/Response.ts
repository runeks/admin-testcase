/// <reference path="../../definitions/underscore.d.ts"/>
/// <reference path="../../definitions/jqueryui.d.ts"/>
/// <reference path="../../definitions/backbone.d.ts"/>
/// <reference path="../../definitions/jquery.d.ts"/>
/// <reference path="../components/BaseView.ts"/>
/// <reference path="../interfaces/PrimeFaces.ts"/>


module EVA.Components {

	export class Response {

		private validationFailed:boolean = false;
		private status:number;
		private componentId:string;
		public response:any;
		public jqXHR:JQueryAjaxSettings;

		public setPrimeFacesArguments(args:IPrimeFacesResponseArgs):void {
			if (args && args.validationFailed) {
				this.validationFailed = true;
			}

			this.response = args;
		}

		public isValidationFailed():boolean {
			return this.validationFailed === true;
		}

		public setStatus(code:number):void {
			this.status = code;
		}

		public getStatus():number {
			return this.status;
		}

		public setComponentId(componentId:string):void {
			this.componentId = componentId;
		}
		
		public getComponentId():string {
			return this.componentId;
		}
	}


}