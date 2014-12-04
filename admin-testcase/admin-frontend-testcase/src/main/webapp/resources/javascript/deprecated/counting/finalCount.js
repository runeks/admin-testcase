jQuery(document).ready(function() {
	questionableSum = questionable();
	totalFinal = total();
	totalUnmodified();
	totalModified();
	totalSum(totalFinal, questionableSum);

	attachKeyEvent(jQuery(".unmodifiedFinal"), function() {
		sumRow(jQuery(this));
		questionableSum = questionable();
		totalFinal = total();
		totalUnmodified();
		totalSum(totalFinal, questionableSum);
	});

	attachKeyEvent(jQuery(".modifiedFinal"), function() {
		sumRow(jQuery(this));
		questionableSum = questionable();
		totalFinal = total();
		totalModified();
		totalSum(totalFinal, questionableSum);
	});

	attachKeyEvent(jQuery(".questionableValue"), function() {
		questionableSum = questionable();
		totalFinal = total();
		totalSum(totalFinal, questionableSum);
	});
	
	attachKeyEvent(jQuery(".blankFinal"), function() {
		questionableSum = questionable();
		totalFinal = total();
		totalSum(totalFinal, questionableSum);
	});

	function sumRow(updated) {
		var value1 = jQuery(updated);
		var td = jQuery(updated).parent().siblings();
		var value2
		if (updated.hasClass("modifiedFinal")) {
			value2 = td.children(".unmodifiedFinal");
		} else {
			value2 = td.children(".modifiedFinal");
		}
		
		if (isNaN(parseInt(value1.val())) && value1.val() != '') {
			value1.val(0);
		}
		if (isNaN(parseInt(value2.val())) && value2.val() != '') {
			value2.val(0);
		}

		var sum = 0;
		if (!isNaN(parseInt(value1.val()))) {
			sum += parseInt(value1.val());
		}
		if (!isNaN(parseInt(value2.val()))) {
			sum += parseInt(value2.val());
		}

		td.children(".sumFinal").text(sum);
	}

	function questionable() {
		var sum = 0;
		jQuery('.questionableValue').each(function() {
			value = jQuery(this).val();
			if (value != '' && !isNaN(value)) {
				sum += parseInt(value);
			}
		});
		jQuery('.questionableSum').text(sum);
		return sum;
	}

	function total() {
		var sum = 0;
		if (jQuery('.sumFinal').length != 0) {
			jQuery('.sumFinal').each(function() {
				value = jQuery(this).text();
				if (value != '' && !isNaN(value)) {
					sum += parseInt(value);
				}
			});
		} else {
			jQuery('.unmodifiedFinal').each(function() {
				value = jQuery(this).val();
				if (!isNaN(value) && value != '') {
					sum += parseInt(value);
				}
			});
		}

		jQuery('.totalFinal').text(sum);
		return sum;
	}

	function totalModified() {
		var sum = 0;
		jQuery('.modifiedFinal').each(function() {
			value = jQuery(this).val();
			if (value != '' && !isNaN(value)) {
				sum += parseInt(value);
			}
		});
		jQuery('.totalFinalModified').text(sum);
	}

	function totalUnmodified() {
		var sum = 0;
		jQuery('.unmodifiedFinal').each(function() {
			value = jQuery(this).val();
			if (value != '' && !isNaN(value)) {
				sum += parseInt(value);
			}
		});
		jQuery('.totalFinalUnmodified').text(sum);
	}

	function totalSum(totalFinal, questionableSum) {
		blankFinal = parseInt(jQuery('.blankFinal').val());
		var sum = questionableSum + totalFinal;
		if (!isNaN(blankFinal)) {
			sum += blankFinal;
		}
		jQuery('.totalSum').text(sum);
	}

});