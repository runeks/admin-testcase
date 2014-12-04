jQuery(document).ready(function() {
	jQuery('.diff').each(function() {
		value = jQuery(this).text();

		if (value != '' && !isNaN(value)) {
			num = parseInt(value);
			if (num == 0) {
				jQuery(this).text("");
			}
		}
	});
});