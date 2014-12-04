jQuery(document).ready(function() {
	var $ = jQuery;

	bindBottomButtons($);
	
	var originalPrimeFacesAjaxRequestSend = PrimeFaces.ajax.Request.send;
	PrimeFaces.ajax.Request.send = function(cfg) {
	    var originalOncomplete = cfg.oncomplete;
	    cfg.oncomplete = function() {
	        ajaxStatusOncomplete.apply(this, arguments);

	        if (originalOncomplete) {
	            originalOncomplete.apply(this, arguments);
	        }
	    };
        originalPrimeFacesAjaxRequestSend.apply(this, arguments);
	};
});


function ajaxStatusOncomplete(xhr, status, args) {
	if (xhr.responseXML) {
		$('.bottom-buttons button.icon-please-wait').each(function(){
			$(this).removeClass('icon-please-wait');
			if (!$.browser.msie) return true;
		});
		$('.bottom-buttons a.icon-please-wait').each(function(){
			$(this).removeClass('icon-please-wait');
			if (!$.browser.msie) return true;
		});
		$('.clickpreventer').remove();
		rebindBottomButtons();
	}
}

function rebindBottomButtons(){
	unbindBottomButtons(jQuery);
	bindBottomButtons(jQuery);
}

function unbindBottomButtons($){
	$('.bottom-buttons button').not('a, .icon-step-next, .icon-step-prev, .non-ajax').unbind('click');
	$('.bottom-buttons a').not('.faux-button').unbind('click');
}

function bindBottomButtons($){
	$('.bottom-buttons button').not('a, .icon-step-next, .icon-step-prev, .non-ajax').click(function(){
		$(this).addClass('icon-button icon-please-wait');
		clickPreventer(this);
		if (!$.browser.msie) return true;
	});
	$('.bottom-buttons a').not('.faux-button').click(function(){
		$(this).addClass('icon-link icon-please-wait');
		clickPreventer(this);
		if (!$.browser.msie) return true;
	});
}

function clickPreventer(target) {
	var $ = jQuery;
	target = $(target);
	var offset = target.offset();
	$('<div>').addClass('clickpreventer').css({
		width: target.width(),
		height: target.height(),
		left: offset.left,
		top: offset.top,
		opacity: 0.5
	}).appendTo('body');
	return true;
}