insertCSRFToken = function() {
	jQuery("form").each(function() {

		if (jQuery('input[name="CSRFToken"]', this).size() < 1) {

			var el = jQuery(document.createElement('input'));

			el.attr('id', 'CSRFToken');
			el.attr('type', 'hidden');
			el.attr('name', 'CSRFToken');
			el.val(CSRFToken);
			jQuery(this).append(el);
		}
	});
}

jQuery(document).ready(function() {
	insertCSRFToken();

	// Run insertCSRFToken on AjaxOncomplete
	var originalPrimeFacesAjaxRequestSend = PrimeFaces.ajax.Request.send;
    PrimeFaces.ajax.Request.send = function(cfg) {
	    var originalOncomplete = cfg.oncomplete;
	    cfg.oncomplete = function() {
	    	insertCSRFToken();

	        if (originalOncomplete) {
	            originalOncomplete.apply(this, arguments);
	        }
	    };
        originalPrimeFacesAjaxRequestSend.apply(this, arguments);
	};
	
});
