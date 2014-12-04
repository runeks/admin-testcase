String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}


function attachKeyEvent(obj, func) {
	obj.keyup(func);
	obj.blur(func);
	obj.focusout(func);
}

function dropdownFocus(id) {
	if (id == '#') {
		return;
	}
	var $ = jQuery;
	if (!ie7) {
		$(id).focus();
	}
}

function getNextFormFieldId(id) {
	var $ = jQuery;
	var fields = $(id).parents('form:eq(0),body').find('button,input,textarea,select');
	var index = fields.index( $(id) );
	if ( index > -1 && ( index + 1 ) < fields.length ) {
		return PrimeFaces.escapeClientId(fields.eq( index + 1 ).attr('id'));
	}
	return false;
}

var resetUpdateNameId = true;
function dialogHooks(fromTabIndex){
	var $ = jQuery;
	if($('.ui-dialog').find('.updateNameId').length>0) {
//		if(resetUpdateNameId) { $('.ui-dialog').find('.updateNameId select').val(''); }
		//$('.ui-dialog').find('.updateNameId').val('');
		$('.ui-dialog').find('.updateNameId select').change(function(){
			var target = $('.ui-dialog').find('.updateNameId select');
			var option = target.find('option[value='+target.val()+']');
			var values = $(option.get(0)).html().split(' - ');
			$('.ui-dialog .updateNameId_Name').val(values[1]);
			$('.ui-dialog .updateNameId_Id').val(values[0]);
			
			resetUpdateNameId = false;
		});	
	}
	
	// Skip tabIndexHook in context editor on IE7 (too slow)	
	if (!ie7 || ($('#mvElectionTable').length == 0 && $('#mvAreaTable').length == 0)) {
		tabIndexHook('.ui-dialog',fromTabIndex);
	}
	
	focusHook('.ui-dialog');
	formFixHook('.ui-dialog');
	enterKeyHook('.ui-dialog');
}
var bodyIndex = 1;
function tabIndexHook(target,from) {
	var $ = jQuery;
	$(target).find('*[tabindex], a').attr('tabIndex','-1');
	var indexCollection = $(target).find('input,select,textarea,radio,button').not('*[type=hidden]');
	var tabIndex;
	$.each(indexCollection, function(i){
		tabIndex = i+from;
		$(this).attr('tabindex',tabIndex);
	});
	if(!!!$(target).get(0)) return false;
	if($(target).get(0).tagName=='BODY') bodyIndex = indexCollection.length;
	return tabIndex+1;
}
function formFixHook(target) {
	var $ = jQuery;
	$('.checkbox input').add('.radio input').each(function(){
		var container = $('<span>').prependTo($(this).parent());
		$(this).appendTo(container);
	});
}

function focusHook(target) {
	var $ = jQuery;
	$(target).find('.focus-on-load').focus();
}

function setupAjaxLoader(){
	adjustAjaxLoader();
	jQuery('.ajax-loader').fadeIn(150);
};

function bindAjaxLoader() {
	jQuery('body').bind('ajaxStart', setupAjaxLoader);
}
function unbindAjaxLoader() {
	jQuery('body').unbind('ajaxStart', setupAjaxLoader);
}

var ie7 = false;
jQuery(document).ready(function() {
	var $ = jQuery;

	if($.browser.msie && navigator.userAgent.indexOf('MSIE 7')!=-1) {
		$('body').addClass('ie7');
		ie7 = true;
	}
	
	
	$('.steps').each(function(){
		var zIndex = 20;
		$(this).find('.progress li').each(function(i){
			$(this).css('z-index',zIndex-i);
		});
	});
	
	var fromTabIndex = tabIndexHook('body',1);
	focusHook('body');
	
	if($('div.menu').length==0 && $('ul#pagemenu, ul#pageMenu').length==0) $('div.content-frame div.help').after( $('<div>').addClass('menu none') );
	else if($('div.menu').length==0 && $('ul#pagemenu, ul#pageMenu').length>0) {
		$('ul#pagemenu, ul#pageMenu').before( menuContainer = $('<div>').addClass('menu') ).appendTo(menuContainer);
		if($('ul#submenu').length>0) {
			if($('ul#pagemenu, ul#pageMenu').find('ul#submenu, ul#subMenu').length==0) $('ul#submenu').appendTo( menuContainer.find('ul > li.selected') );
		}
		
		menuContainer.find('ul li a').not('ul ul li a').each(function(){
			if($(this).html()=='' || $(this).attr('href')=='') $(this).remove();
			else if($(this).parent().get(0).tagName.toLowerCase()!='span') $(this).before( itemContainer = $('<span>') ).appendTo(itemContainer);
		});
		if(menuContainer.find('ul ul li a').length>0) menuContainer.addClass('sub');
	} else if($('div.menu').length>0 && $('div.menu ul').length==0) $('div.menu').addClass('none');
	
	
	$('.main-help-link').click(function(){
		setTimeout(function(){
			$('.ui-widget-overlay').click(function(){
				$('.ui-dialog .ui-dialog-titlebar-close').click();
			});
		}, 150);
	});

	$('.rollover tr').hover(function(){
		$(this).addClass('ui-state-hover');
	},function(){
		$(this).removeClass('ui-state-hover');
	});
	/*
	var blockMessageTimeout = false;
	setInterval(function(){
		if($('.ui-messages-info').length>0 && !blockMessageTimeout && !$('.ui-messages-info').hasClass('blockMessageTimeout')) {
			blockMessageTimeout = true;
			setTimeout(function(){
				$('.ui-messages-info').animate({
						opacity: 0
					},{
						queue: false,
						duration: 500,
						complete: function(){
							$(this).remove();
						} 
					}).slideUp(500);
				blockMessageTimeout = false;
			},4000);
		}
	},1000);
	*/

	adjustAjaxLoader();
	bindAjaxLoader();
	$('body').bind('ajaxComplete', function(){
		$('.ajax-loader').fadeOut(150, 'swing',function(){ });
		$('.ui-dialog .ui-datepicker-trigger').remove();
		$('.ui-dialog .datepicker').datepicker(datePickerSettings);
		$('.ui-dialog .info-text').each(function(){ new infoBubble($(this)) });
		dialogHooks(fromTabIndex);
		contextPickerHook();
		mergeTables();
	});
	contextPickerHook();
	
	$('.go-back').click(function(){
		window.history.go(-1);
		return false;
	});
	
	$('div.page-footer div.content div.right').click(function(){
		alert($(this).html().trim());
	})
	

	$('.info-text').each(function(){ new infoBubble($(this)) });
	$('span.inline-error').each(function(){ 
		$(this).parents('.inputs').addClass('error');
	});
	
	datePickerSettings.showOn = 'button';
	datePickerSettings.buttonImageOnly = true;
	datePickerSettings.onSelect = function(dateText, inst){
		valiDate(this);
	}
	
	if($('div.welcome').length==0) {
		$('.datepicker').not('.ui-dialog .datepicker').datepicker(datePickerSettings).unbind('blur').unbind('focus').blur(function(){
			valiDate(this);
		}).focus(function(){
			valiDate(this);
		}).parent().find('.ui-datepicker-trigger');
		sessionTimeoutHandler.init(sessionTimeoutSettings);
	}

	if (jQuery("input[id$=startDateVoterAudit]").length != 0) {
		registerVoterAuditDatepickerAutoFill();
	}
	
	equalizePanels();
	
	mergeTables();

	formFixHook('body');
	stepNavigator.init();
});

function equalizePanels() {
	var $ = jQuery;
	$('.equalize-panels:visible').each(function(){
		var height = 0;
		$(this).find('.ui-panel').each(function(){
			if($(this).height()>height) height = $(this).height();
		});
		$(this).find('.ui-panel').css('height',height);
	});
}

function valiDate(target) {
	var $ = jQuery;
	if($(target).val()!='__.__.____' && $(target).val()!='') {
		$(target).parent().removeClass('error').find('.inline-error').remove();
		try {
			$.datepicker.parseDate(datePickerSettings['dateFormat'], $(target).val());
		}
		catch (ex) {
			$(target).parent().addClass('error').append('<span class="inline-error">'+ valiDateMessage +'</span>');
		}
	}
}

function mergeTables() {
	var $ = jQuery;
	$('.table-merge-container').filter(function(){
		// Allow for nesting infinite .table-merge-container, only return the innermost
		if($(this).find('.table-merge-container').length>0) return false;
		return true;
	}).each(function(){
		var parent = this;
		var mergeCollection = $(parent).find('.merge-with-prev');
		$(parent).find('table tbody tr').each(function(){
			var allEmpty = true;
			$(this).find('td').each(function(){
				if($(this).html()!='') allEmpty = false;
			});
			if(allEmpty) $(this).remove();
		});
		for(var i=mergeCollection.length-1;i>0;i--) {
			$(mergeCollection[i]).find('thead').remove();
			if($($(mergeCollection[i]).find('tbody tr').get(0)).parents('.formTable').hasClass('has-divider')) $($(mergeCollection[i]).find('tbody tr').get(0)).addClass('has-divider');
			if(!$($(mergeCollection[i]).find('tbody tr').get(0)).parents('.formTable').hasClass('no-divider')) $($(mergeCollection[i]).find('tbody tr').get(0)).addClass('divider');
			$(mergeCollection[i]).find('tbody tr').appendTo( $(mergeCollection[i-1]).find('tbody') );
			$(mergeCollection[i]).remove();
		}
	});
}

function contextPickerHook() {
	var $ = jQuery;
	$('.contextPicker table td').each(function(){
		$(this).click(function(){
			var selected = $(this).parent().hasClass('ui-selected');
			if(selected) {
				return false;
			};
			return true;
		});
	});
};
var stepNavigator = {
	init: function(){
		var self = this;
		var $ = jQuery;
		self.container = $('.steps');
		if(self.container.length==0) return false;
		self.progress = self.container.find('.progress');
		self.steps = self.container.find('.step');
		self.current = self.container.find('.progress .current');
		if(self.current.length==0) self.current = $(self.container.find('.progress ol li').get(0)).addClass('current');
		self.setFromHash();
		return true;
	},
	next: function(){
		var self = this;
		var $ = jQuery;
		var index = self.getCurrent();
		self.current = $(self.progress.find('li').removeClass('current').get(index+1)).addClass('current');
		$(self.steps.removeClass('current').get(index+1)).addClass('current');
		window.location.hash = 'step=' + (index+2);
		return false;
	},
	previous: function(){
		var self = this;
		var $ = jQuery;
		var index = self.getCurrent();
		self.current = $(self.progress.find('li').removeClass('current').get(index-1)).addClass('current');
		$(self.steps.removeClass('current').get(index-1)).addClass('current');
		window.location.hash = 'step=' + (index);
		return false;
	},
	getCurrent: function(){
		var self = this;
		var $ = jQuery;
		var index = -1;
		self.progress.find('li').each(function(i){
			if($(this).hasClass('current')) index = i;
		});
		return index;
	},
	setFromHash: function(){
		var self = this;
		var $ = jQuery;
		var hash = window.location.hash.replace('#','').split('&');
		$.each(hash, function(){
			var parts = this.split('=');
			if(parts[0]=='step') {
				var selectedStep = $($(self.steps).get(parts[1]-1));
				if(selectedStep.length>0) {
					self.current = $(self.progress.find('li').removeClass('current').get(parts[1]-1)).addClass('current');
					$(self.steps.removeClass('current').get(parts[1]-1)).addClass('current');
				}
			}
		});
	}
}

var infoBubble = function(target){
	var self = this;
	var $ = jQuery;
	self.target = $(target);
	self.init();
	return $(self.target);
}
infoBubble.prototype = {
	init: function(){
		var self = this;
		var $ = jQuery;
		self.target.mouseover(function(){self.show();}).mouseout(function(){self.hide();});
	},
	show: function(){
		var self = this;
		var $ = jQuery;
		if($('.info-bubble').length==0) {
			self.container = $('<div>').addClass('info-bubble').hide().appendTo('body'); 
		} else {
			self.container = $('.info-bubble').hide();
		}
		var offset = self.target.offset();
		var browser = $.clientCoords();
		self.container.html(self.target.html()).css({
			display: 'block',
			visibility: 'hidden'
		});
		if(offset.left>browser.width/2) {
			self.container.removeClass('right').addClass('left').css({
				top: offset.top - parseInt(self.container.css('padding-top'))-self.container.height(),
				left: offset.left - self.container.width() - parseInt(self.container.css('padding-left')) - parseInt(self.container.css('padding-right')) + 8
			});
		} else {
			self.container.removeClass('left').addClass('right').css({
				top: offset.top - parseInt(self.container.css('padding-top'))-self.container.height(),
				left: offset.left + 10
			});
		}
		self.container.css({
			display: 'none',
			visibility: 'visible'
		}).fadeIn(250);
	},
	hide: function(){
		var self = this;
		var $ = jQuery;
		self.container.fadeOut(250);
	}
}



var sessionTimeoutHandler = {
	init: function(settings) {
		var self = this;
		self.settings = settings;
		self.settings.timer = setInterval(function(){ self.timer(); }, 1000);
		self.settings.loaded = self.now();
		self.settings.polled = self.now();
		self.settings.pinged = self.now();
		self.settings.warning = false;
		self.settings.activeSincePoll = false;
		self.settings.loggingOut = false;
		jQuery('body').not('#logout a').bind('click keypress ajaxSend', function(){ self.ping(); });
		jQuery(window).bind('scroll resize', function(){ self.ping(); });
		jQuery(window).bind('click keypress ajaxSend scroll resize', function(){ self.hide(); });
		return true;
	},
	timer: function(){
		var self = this;
		var $ = jQuery;
		$('.session-timeout .ui-button span').html(self.settings.button + ' (' + self.formatMinutes(self.timeLeftClient()) + ')');
		if(self.timeLeftClient()<=0) self.logout();
		if(self.nearTimeout() && !self.settings.warning) self.show();
		if(self.settings.activeSincePoll &&
				self.activeSinceLoad() &&
				self.timeLeftClient()>0 &&
				self.timeSincePoll()>=(self.settings.interval*60) &&
				!self.nearTimeout()
			) self.poll();
	},
	show: function(){
		var self = this;
		var $ = jQuery;
		if($('.session-timeout').length==0) {
			
			var warningText = $('<div>').addClass('row').html(self.settings.message);
			var warningButtonText = $('<span>').addClass('ui-button-tex').html(self.settings.button + ' (' + self.formatMinutes(self.timeLeftClient()) + ')');
			var warningButton = $('<a>').addClass('button ui-button').append(warningButtonText);
			
			var warningContainer = $('<div>').hide().addClass('session-timeout').append(warningText).append( warningButton ).appendTo($('body'));
			var warningOverlay = $('<div>').hide().addClass('session-timeout-overlay').appendTo($('body'));
			self.adjust();
			warningContainer.fadeIn(250);
			warningOverlay.fadeIn(250);
			warningButton.click(function(){ self.hide(); });
			warningContainer.click(function(){ self.hide(); })
			warningOverlay.click(function(){ self.hide(); });
		} else {
			self.adjust();
			$('.session-timeout, .session-timeout-overlay').fadeIn(250);
		}
		self.settings.warning = true;
	},
	hide: function(){
		var self = this;
		var $ = jQuery;
		$('.session-timeout, .session-timeout-overlay').fadeOut(250);
		self.settings.warning = false;
	},
	nearTimeout: function(){
		var self = this;
		return Math.floor(self.timeSincePing()/60)+self.settings.padding >= self.settings.timeout;
	},
	activeSinceLoad: function(){
		var self = this;
		return self.timeSinceLoad() > self.timeSincePing();
	},
	activeSincePoll: function(){
		var self = this;
		return self.settings.activeSincePoll;
	},
	formatMinutes: function(n){
		var self = this;
		var minutes = Math.floor(n/60);
		var seconds = n - (minutes*60);
		if(minutes.toString().length==1) minutes = '0'+minutes;
		if(seconds.toString().length==1) seconds = '0'+seconds;
		return minutes+':'+seconds;
	},
	now: function(){
		var self = this;
		return Math.floor(new Date().getTime()/1000);
	},
	timeLeftServer: function(){
		var self = this;
		return (self.settings.timeout*60) - (self.now() - self.settings.polled);
	},
	timeLeftClient: function(){
		var self = this;
		return (self.settings.timeout*60) - (self.now() - self.settings.pinged);
	},
	timeSinceLoad: function(){
		var self = this;
		return self.now() - self.settings.loaded;
	},
	timeSincePing: function(){
		var self = this;
		return self.now() - self.settings.pinged;
	},
	timeSincePoll: function(){
		var self = this;
		return self.now() - self.settings.polled;
	},
	logout: function(){
		var self = this;
		// prevent logout from being called every second
		if (!self.settings.loggingOut) {
			self.settings.loggingOut = true;
			window.location.href = "/logout";
		}
		return false;
	},
	ping: function(){
		var self = this;
		if(self.settings.interval*60<=self.timeSincePoll()) self.poll();
		self.settings.pinged = self.now();
		self.settings.activeSincePoll = true;
		return true;
	},
	poll: function(){
		var self = this;
		jQuery.ajax({
			url: self.settings.url + '?nc=' + (Math.random()*self.now()),
			global: false
		});
		self.settings.polled = self.now();
		self.settings.activeSincePoll = false;
		return true;
	},
	adjust: function(){
		var $ = jQuery;
		var clientCoords = $.clientCoords();
		$('.session-timeout').css({
			display: 'block',
			visibility: 'hidden'
		}).css({
			left: (clientCoords.width/2) - ($('.session-timeout').width()/2),
			top: (clientCoords.height/2) - ($('.session-timeout').height()/2)
		}).css({
			display: 'none',
			visibility: 'visible'
		});
	}
};



function adjustAjaxLoader(){
	var $ = jQuery;
	var clientCoords = $.clientCoords();
	$('.ajax-loader').css({
		display: 'block',
		visibility: 'hidden'
	}).css({
		left: (clientCoords.width/2) - ($('.ajax-loader').width()/2),
		top: (clientCoords.height/2) - ($('.ajax-loader').height()/2)
	}).css({
		display: 'none',
		visibility: 'visible'
	});
}
jQuery.clientCoords = function() {
	var dimensions = {width: 0, height: 0};
	if (jQuery.browser.msie) {
		dimensions.width = document.documentElement.offsetWidth;
		dimensions.height = document.documentElement.offsetHeight;
	} else if (window.innerWidth && window.innerHeight) {
		dimensions.width = window.innerWidth;
		dimensions.height = window.innerHeight;
	}
	return dimensions;
}



function enterKeyHook(container) {
	var $ = jQuery;
	var targets = $(container).find('input,radio,select').not('input[type=submit],input[type=button],button');
	var eventType = ($.browser.mozilla? 'keydown': 'keypress');
	targets.bind(eventType,function(e){
		var pressed = new KeyEvent(e);
		if(pressed.char=='ENTER') {
			var cc = $(pressed.target).parents(container);
			var button = $(cc.find('.bottom-buttons .call-to-action button').get(0));
			button.click();
			return false;
		} else return true;
	});
}

var KeyEvent = function(e) {
	var keys = {
		8: 'BACKSPACE',
		9: 'TAB',
		13: 'ENTER',
		16: 'SHIFT',
		17: 'CTRL',
		18: 'ALT',
		19: 'PAUSE/BREAK',
		20: 'CAPS_LOCK',
		27: 'ESCAPE',
		33: 'PAGE_UP',
		34: 'PAGE_DOWN',
		35: 'END',
		36: 'HOME',
		37: 'LEFT_ARROW',
		38: 'UP_ARROW',
		39: 'RIGHT_ARROW',
		40: 'DOWN_ARROW',
		45: 'INSERT',
		46: 'DELETE',
		48: '0',
		49: '1',
		50: '2',
		51: '3',
		52: '4',
		53: '5',
		54: '6',
		55: '7',
		56: '8',
		57: '9',
		65: 'A',
		66: 'B',
		67: 'C',
		68: 'D',
		69: 'E',
		70: 'F',
		71: 'G',
		72: 'H',
		73: 'I',
		74: 'J',
		75: 'K',
		76: 'L',
		77: 'M',
		78: 'N',
		79: 'O',
		80: 'P',
		81: 'Q',
		82: 'R',
		83: 'S',
		84: 'T',
		85: 'U',
		86: 'V',
		87: 'W',
		88: 'X',
		89: 'Y',
		90: 'Z',
		91: 'LEFT_WINDOW_KEY',
		92: 'RIGHT_WINDOW_KEY',
		93: 'SELECT_KEY',
		96: 'NUMPAD_0',
		97: 'NUMPAD_1',
		98: 'NUMPAD_2',
		99: 'NUMPAD_3',
		100: 'NUMPAD_4',
		101: 'NUMPAD_5',
		102: 'NUMPAD_6',
		103: 'NUMPAD_7',
		104: 'NUMPAD_8',
		105: 'NUMPAD_9',
		106: 'MULTIPLY',
		107: 'ADD',
		109: 'SUBTRACT',
		110: 'DECIMAL_POINT',
		111: 'DIVIDE',
		112: 'F1',
		113: 'F2',
		114: 'F3',
		115: 'F4',
		116: 'F5',
		117: 'F6',
		118: 'F7',
		119: 'F8',
		120: 'F9',
		121: 'F10',
		122: 'F11',
		123: 'F12',
		144: 'NUM_LOCK',
		145: 'SCROLL_LOCK',
		186: 'SEMI-COLON',
		187: 'EQUAL_SIGN',
		188: 'COMMA',
		189: 'DASH',
		190: 'PERIOD',
		191: 'FORWARD_SLASH',
		192: 'GRAVE_ACCENT',
		219: 'OPEN_BRACKET',
		220: 'BACK_SLASH',
		221: 'CLOSE_BRAKET',
		222: 'SINGLE_QUOTE'                     
	};
	return {
		modifiers: {
			ctrl: e.ctrlKey,
			alt: e.altKey,
			shift: e.shiftKey,
			meta: e.metaKey
		},
		key: (window.event?event.keyCode:e.keyCode),
		char: keys[(window.event?event.keyCode:e.keyCode)],
		target: e.target
	};
};
KeyEvent.prototype = {};
