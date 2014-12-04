function fixRow() {
	jQuery('.sumRow').each(
			function() {
				index = jQuery(this).parent('td').parent('tr').index() + 1;

				jQuery('.collatedCountTable').each(
						function() {
							tr = jQuery(this).children('table').children(
									'tbody').children(
									'tr:nth-child(' + index + ')');
							tr.addClass('sumRow');
						});
			});
}

jQuery(document).ready(function() {
	fixRow();
});

jQuery('body').bind('ajaxComplete', function(event, xhr) {
	fixRow();
});