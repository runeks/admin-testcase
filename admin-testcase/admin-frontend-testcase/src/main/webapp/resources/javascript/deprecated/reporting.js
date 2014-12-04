var disabledClass = "ui-button ui-widget ui-state-default ui-corner-all ui-button-disabled ui-state-disabled ui-button-text-only";
var enabledClass = "ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only";
var prevDisabledStatus = false;	

function hidePathLoadingBars(ids) {
	var idsArray = ids.split(" ");
	for (i = 0; i < idsArray.length; i++) {
		document.getElementById('loading' + idsArray[i] + 'Div').style.display = 'none';
		document.getElementById('select' + idsArray[i] + 'Div').style.display = 'block';
	}

	// Restores the buttons disabled statuses

//	var searchButton = document.getElementById("searchButton");
//	if (searchButton != null) {
//		searchButton.disabled = false;
//		searchButton.setAttribute("class", enabledClass);
//	}
//
//	var actionButton = document.getElementById("actionButton");
//	if (actionButton != null) {
//		actionButton.disabled = prevDisabledStatus;
//		if (!prevDisabledStatus) {
//			actionButton.setAttribute("class", enabledClass);
//		}
//	}
}

function showPathLoadingBars(ids) {
	var idsArray = ids.split(" ");
	for (i = 0; i < idsArray.length; i++) {
		document.getElementById('select' + idsArray[i] + 'Div').style.display = 'none';
		document.getElementById('loading' + idsArray[i] + 'Div').style.display = 'block';
	}

	// Disables the buttons

//	var searchButton = document.getElementById("searchButton");
//	if (searchButton != null) {
//		searchButton.disabled = true;
//		searchButton.setAttribute("class", disabledClass);
//	}
//
//	var actionButton = document.getElementById("actionButton");
//	if (actionButton != null) {
//		prevDisabledStatus = actionButton.disabled;
//		actionButton.disabled = true;
//		actionButton.setAttribute("class", disabledClass);		
//	}
}
