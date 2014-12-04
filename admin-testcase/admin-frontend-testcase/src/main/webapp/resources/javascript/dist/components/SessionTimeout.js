var __extends = this.__extends || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    __.prototype = b.prototype;
    d.prototype = new __();
};
var EVA;
(function (EVA) {
    (function (Components) {
        var SessionTimeout = (function (_super) {
            __extends(SessionTimeout, _super);
            function SessionTimeout(settings) {
                _super.call(this, { el: 'body' });
                this.settings = settings;
            }
            SessionTimeout.prototype.initialize = function () {
                var _this = this;
                this.checkInterval = setInterval(function () {
                    _this.onCheck();
                }, 1000);

                this.initialized = SessionTimeout.now();
                this.lastPoll = SessionTimeout.now();
                this.lastPing = SessionTimeout.now();

                this.showWarning = false;
                this.hasCreatedUI = false;
                this.activitySinceLastPoll = false;
                this.loggingOut = false;

                this.$el.on('click keypress scroll resize mousemove', function () {
                    _this.ping();

                    if (_this.hasCreatedUI && _this.showWarning) {
                        _this.hide();
                    }
                });
            };

            SessionTimeout.prototype.onCheck = function () {
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
            };

            SessionTimeout.prototype.shouldPoll = function () {
                return this.activitySinceLastPoll && this.activeSinceLoad() && this.timeLeftClient() > 0 && this.timeSincePoll() >= (this.settings.interval * 60) && !this.nearTimeout();
            };

            SessionTimeout.prototype.update = function () {
                this.$widget.find('.ui-button span').html(this.settings.buttonLabel + ' (' + this.formatMinutes(this.timeLeftClient()) + ')');
            };

            SessionTimeout.prototype.show = function () {
                if (!this.hasCreatedUI) {
                    this.createUI();
                }

                this.adjust();

                this.$widget.fadeIn(250);
                this.$overlay.fadeIn(250);

                this.showWarning = true;
            };

            SessionTimeout.prototype.hide = function () {
                this.$widget.fadeOut(250);
                this.$overlay.fadeOut(250);
                this.showWarning = false;
            };

            SessionTimeout.prototype.createUI = function () {
                var _this = this;
                var warningText = $(document.createElement('div')).addClass('row').html(this.settings.message);
                var warningButtonText = $(document.createElement('span')).addClass('ui-button-text').html(this.settings.buttonLabel + ' (' + this.formatMinutes(this.timeLeftClient()) + ')');
                var warningButton = $(document.createElement('a')).addClass('btn btn-success').append(warningButtonText);

                this.$widget = $(document.createElement('div')).hide().addClass('session-timeout').append(warningText).append(warningButton).appendTo(this.$el);
                this.$overlay = $(document.createElement('div')).hide().addClass('session-timeout-overlay').appendTo(this.$el);

                warningButton.click(function (event) {
                    event.preventDefault();
                    event.stopImmediatePropagation();
                    _this.hide();

                    return false;
                });

                this.$widget.click(function () {
                    _this.hide();
                });

                this.$overlay.click(function () {
                    _this.hide();
                });

                this.hasCreatedUI = true;
            };

            SessionTimeout.prototype.activeSinceLoad = function () {
                return this.timeSinceLoad() > this.timeSincePing();
            };

            SessionTimeout.prototype.nearTimeout = function () {
                return Math.floor(this.timeSincePing() / 60) + this.settings.padding >= this.settings.timeout;
            };

            SessionTimeout.prototype.formatMinutes = function (n) {
                var minutes = Math.floor(n / 60);
                var seconds = n - (minutes * 60);

                if (minutes.toString().length == 1) {
                    minutes = '0' + minutes.toString();
                }
                if (seconds.toString().length == 1) {
                    seconds = '0' + seconds.toString();
                }

                return minutes + ':' + seconds;
            };

            SessionTimeout.prototype.timeLeftClient = function () {
                return (this.settings.timeout * 60) - (SessionTimeout.now() - this.lastPing);
            };

            SessionTimeout.prototype.timeSinceLoad = function () {
                return SessionTimeout.now() - this.initialized;
            };

            SessionTimeout.prototype.timeSincePing = function () {
                return SessionTimeout.now() - this.lastPing;
            };

            SessionTimeout.prototype.timeSincePoll = function () {
                return SessionTimeout.now() - this.lastPoll;
            };

            SessionTimeout.prototype.logout = function () {
                if (this.loggingOut) {
                    return;
                }

                this.loggingOut = true;

                jQuery.ajax({
                    url: "/logout",
                    global: false
                }).done(function () {
                    window.location.href = '/welcome.xhtml';
                });
            };

            SessionTimeout.prototype.ping = function () {
                if (this.settings.interval * 60 <= this.timeSincePoll()) {
                    this.poll();
                }
                this.lastPing = SessionTimeout.now();
                this.activitySinceLastPoll = true;
            };

            SessionTimeout.prototype.poll = function () {
                this.lastPoll = SessionTimeout.now();
                this.activitySinceLastPoll = false;

                jQuery.ajax({
                    url: this.settings.url + '?nc=' + (Math.random() * SessionTimeout.now()),
                    global: false
                });
            };

            SessionTimeout.prototype.adjust = function () {
                var cssProperties = {
                    left: Math.max(0, (($(window).width() - this.$widget.outerWidth()) / 2) + $(window).scrollLeft()) + "px",
                    top: Math.max(0, (($(window).height() - this.$widget.outerHeight()) / 2) + $(window).scrollTop()) + "px",
                    display: 'none'
                };

                this.$widget.css(cssProperties);
            };

            SessionTimeout.now = function () {
                return Math.floor(new Date().getTime() / 1000);
            };
            return SessionTimeout;
        })(EVA.Components.BaseView);
        Components.SessionTimeout = SessionTimeout;
    })(EVA.Components || (EVA.Components = {}));
    var Components = EVA.Components;
})(EVA || (EVA = {}));
