function setAutoCompleteFocus() {
	jQuery("input[id='ballotApproveRejectedForm:createManualCastVote:autoComplete_input']").focus();
}

jQuery(document).ready(function() {
	applyChangeHandlerAndInit();
});

function applyChangeHandlerAndInit() {
	totalRows();
	totalRejected();
	totalRegistred();
	attachKeyEvent(jQuery(".rejectionCount"), function() {
		totalRejected();
	});
	attachKeyEvent(jQuery(".modifiedCount"), function() {
		totalRegistred();
		totalRow(jQuery(this));
	});
	attachKeyEvent(jQuery(".unmodifiedCount"), function() {
		totalRegistred();
		totalRow(jQuery(this));
	});
	attachKeyEvent(jQuery(".totalBlank"), function() {
		totalRegistred();
	});
}
function totalRejected() {
	var sum = 0;
	jQuery('.rejectionCount').each(function() {
		value = jQuery(this).val();
		if (value != '' && !isNaN(value)) {
			sum += parseInt(value);
		}
	});
	jQuery('.rejectedSum').text(sum);
}

function totalRegistred() {
	var sum = 0;
	jQuery('.modifiedCount').each(function() {
		value = jQuery(this).val();
		if (value != '' && !isNaN(value)) {
			sum += parseInt(value);
		}
	});
	jQuery('.unmodifiedCount').each(function() {
		value = jQuery(this).val();
		if (value != '' && !isNaN(value)) {
			sum += parseInt(value);
		}
	});
	jQuery('.totalBlank').each(function() {
		value = jQuery(this).val();
		if (value != '' && !isNaN(value)) {
			sum += parseInt(value);
		}
	});
	jQuery('.registredSum').text(sum);
}

function totalRows() {
	jQuery('.modifiedCount').each(function() {
		totalRow(jQuery(this));
	});
}

function totalRow(updated) {
	var value1 = jQuery(updated);
	var td = jQuery(updated).parent().siblings();
	var value2
	if (updated.hasClass("modifiedCount")) {
		value2 = td.children(".unmodifiedCount");
	} else {
		value2 = td.children(".modifiedCount");
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

	td.children(".sumRow").text(sum);
}
