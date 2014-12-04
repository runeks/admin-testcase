function cookieEnabled() {
	return navigator.cookieEnabled
			|| ("cookie" in document && (document.cookie.length > 0 || (document.cookie = "test").indexOf
					.call(document.cookie, "test") > -1));
}

jQuery(document).ready(function() {
	if (!cookieEnabled()) {
		jQuery('#no_cookies_msg').show();
	}
});