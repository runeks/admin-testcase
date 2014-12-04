
jQuery(document).ready(function() {

	// Both keyup and focusout to handle fast tabbing out of input fields (#9914)
	jQuery(".value").keyup(function() {
		sumProtocol();
	});
	jQuery(".value").focusout(function() {
		sumProtocol();
	});

	function sumProtocol() {
		var error = false;
		var sum = 0;

		jQuery('.value:not(.electionDayMarkoffs)').each(function() {
			var value = jQuery(this).val();
			if(value != '' && !isNaN(value)) {
				sum += parseInt(value);
			} else {
				error = true;
			}
		});
		jQuery("#protocolCount\\:totalInPolls").text(sum);

		// Update total markoffs if necessary
		if (jQuery('.electionDayMarkoffs').length > 0) {
			var totalMarkoffs = 0;
			jQuery('.electionDayMarkoffs').each(function() {
				var value = jQuery(this).val();
				if(value != '' && !isNaN(value)) {
					totalMarkoffs += parseInt(value);
				}
			});
			jQuery('.contestVotingsSum').text(totalMarkoffs);
		}
	}
});
