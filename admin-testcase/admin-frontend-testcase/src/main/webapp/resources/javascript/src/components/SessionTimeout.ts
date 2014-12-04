/// <reference path="../../definitions/underscore.d.ts"/>
/// <reference path="../../definitions/jqueryui.d.ts"/>
/// <reference path="../../definitions/backbone.d.ts"/>
/// <reference path="../../definitions/jquery.d.ts"/>
/// <reference path="../components/BaseView.ts"/>
/// <reference path="../components/Response.ts"/>
/// <reference path="../interfaces/IPCComponent.ts"/>

module EVA.Components {

	export interface ISessionTimeoutConfig {
		url:string;
		message:string;
		buttonLabel:string;
		interval:number;
		padding:number;
		timeout:number;
	}
	
	
	export class SessionTimeout extends EVA.Components.BaseView {

		private settings:ISessionTimeoutConfig;
		private checkInterval:number;
		private initialized:number;
		private lastPoll:number;
		private lastPing:number;
		private showWarning:boolean;
		private loggingOut:boolean;
		private activitySinceLastPoll:boolean;
		private hasCreatedUI:boolean;

		public $widget:JQuery;
		public $overlay:JQuery;

		/**
		 * @param settings {ISessionTimeoutConfig}
		 */
		constructor(settings:ISessionTimeoutConfig) {
			super({el: 'body'});
			this.settings = settings;
		}

		public initialize():void {

			this.checkInterval = setInterval(()=> {
				this.onCheck();
			}, 1000);

			this.initialized = SessionTimeout.now();
			this.lastPoll = SessionTimeout.now();
			this.lastPing = SessionTimeout.now();

			this.showWarning = false;
			this.hasCreatedUI = false;
			this.activitySinceLastPoll = false;
			this.loggingOut = false;

			this.$el.on('click keypress scroll resize mousemove', () => {
				this.ping();

				if (this.hasCreatedUI && this.showWarning) {
					this.hide();
				}
			});
		}

		private onCheck():void {
			
			if (this.timeLeftClient() <= 0) {
				this.logout();
			} else if (this.nearTimeout() && !this.showWarning) {
				this.show();
			}
			
			if (this.shouldPoll()) {
				this.poll();
			} else if (this.showWarning && this.hasCreatedUI) {
				this.update();
			} 
		}

		private shouldPoll():boolean {
			return this.activitySinceLastPoll && this.activeSinceLoad() && this.timeLeftClient() > 0 && this.timeSincePoll() >= (this.settings.interval * 60) && !this.nearTimeout();
		}

		private update():void {
			this.$widget.find('.ui-button span').html(this.settings.buttonLabel + ' (' + this.formatMinutes(this.timeLeftClient()) + ')');
		}

		private show():void {

			if (!this.hasCreatedUI) {
				this.createUI();
			}

			this.adjust();

			this.$widget.fadeIn(250);
			this.$overlay.fadeIn(250);

			this.showWarning = true;
		}

		private hide():void {
			this.$widget.fadeOut(250);
			this.$overlay.fadeOut(250);
			this.showWarning = false;
		}

		private createUI():void {

			var warningText:JQuery = $(document.createElement('div')).addClass('row').html(this.settings.message);
			var warningButtonText:JQuery = $(document.createElement('span')).addClass('ui-button-text').html(this.settings.buttonLabel + ' (' + this.formatMinutes(this.timeLeftClient()) + ')');
			var warningButton:JQuery = $(document.createElement('a')).addClass('btn btn-success').append(warningButtonText);

			this.$widget = $(document.createElement('div')).hide().addClass('session-timeout').append(warningText).append(warningButton).appendTo(this.$el);
			this.$overlay = $(document.createElement('div')).hide().addClass('session-timeout-overlay').appendTo(this.$el);

			warningButton.click((event:JQueryEventObject) => {
				event.preventDefault();
				event.stopImmediatePropagation();
				this.hide();

				return false;
			});

			this.$widget.click(() => {
				this.hide();
			});

			this.$overlay.click(() => {
				this.hide();
			});

			this.hasCreatedUI = true;
		}

		private activeSinceLoad():boolean {
			return this.timeSinceLoad() > this.timeSincePing();
		}

		private nearTimeout():boolean {
			return Math.floor(this.timeSincePing() / 60) + this.settings.padding >= this.settings.timeout;
		}

		private formatMinutes(n:number):string {

			var minutes:any = Math.floor(n / 60);
			var seconds:any = n - (minutes * 60);

			if (minutes.toString().length == 1) {
				minutes = '0' + minutes.toString();
			}
			if (seconds.toString().length == 1) {
				seconds = '0' + seconds.toString();
			}

			return minutes + ':' + seconds;
		}

		private timeLeftClient():number {
			return (this.settings.timeout * 60) - (SessionTimeout.now() - this.lastPing);
		}

		private timeSinceLoad():number {
			return SessionTimeout.now() - this.initialized;
		}

		private timeSincePing():number {
			return SessionTimeout.now() - this.lastPing;
		}

		private timeSincePoll():number {
			return SessionTimeout.now() - this.lastPoll;
		}

		private logout():void {

			if (this.loggingOut) {
				return;
			}

			this.loggingOut = true;

			jQuery.ajax(<JQueryAjaxSettings>{
				url: "/logout",
				global: false
			}).done(()=> {
				window.location.href = '/welcome.xhtml';
			});
		}

		private ping():void {
			if (this.settings.interval * 60 <= this.timeSincePoll()) {
				this.poll();
			}
			this.lastPing = SessionTimeout.now();
			this.activitySinceLastPoll = true;
		}

		private poll():void {
//			debugger;

			this.lastPoll = SessionTimeout.now();
			this.activitySinceLastPoll = false;
			
			jQuery.ajax(<JQueryAjaxSettings>{
				url: this.settings.url + '?nc=' + (Math.random() * SessionTimeout.now()),
				global: false
			});
		}

		private adjust():void {

			var cssProperties:Object = {
				left: Math.max(0, (($(window).width() - this.$widget.outerWidth()) / 2) + $(window).scrollLeft()) + "px",
				top: Math.max(0, (($(window).height() - this.$widget.outerHeight()) / 2) + $(window).scrollTop()) + "px",
				display: 'none'
			};

			this.$widget.css(cssProperties);
		}

		public static now():number {
			return Math.floor(new Date().getTime() / 1000);
		}
	}
}