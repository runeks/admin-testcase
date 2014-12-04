function getDatePickerSettingsClone(datePickerSettings) {
var clone = new Object();
for (var key in datePickerSettings) {
	clone[key] = datePickerSettings[key]
}

return clone;
}

function addOnSelectCallbackDatePickerSettings(datePickerSettings, callback) {
	
	
	datePickerSettings.onSelect = function (dateText, inst) {
		valiDate(this);
		
		if(!jQuery(this).parent().hasClass('error')) {
			callback();
		}
	}
}

function reinitializeDatePicker(datePickerSettings, datepickerWidget) {
		datepickerWidget.datepicker("destroy");
		datepickerWidget.datepicker(datePickerSettings);
}


function fillInEndDateOnStartDateSelect(datepickerWidget) {
		var datePickerSettingsClone = getDatePickerSettingsClone(datePickerSettings);
		addOnSelectCallbackDatePickerSettings(datePickerSettingsClone, setEndDateValue);
		reinitializeDatePicker(datePickerSettingsClone, datepickerWidget);

}

function setEndDateValue() {
	jQuery("input[id$=endDateVoterAudit]")[0].value = jQuery("input[id$=startDateVoterAudit]")[0].value;
}

function registerVoterAuditDatepickerAutoFill() {
	fillInEndDateOnStartDateSelect(jQuery("input[id$=startDateVoterAudit]"));
}