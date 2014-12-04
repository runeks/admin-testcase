function changeCopyMode() {
	if (document.getElementById('allowCopying').checked == true) {
		document.getElementById('electionEventForm:electionEventSelect').disabled = false;
		document.getElementById('electionEventForm:copyAreas').disabled = false;
		document.getElementById('electionEventForm:copyRoles').disabled = false;
	} else {
		document.getElementById('electionEventForm:electionEventSelect').disabled = true;
		document.getElementById('electionEventForm:copyAreas').disabled = true;
		document.getElementById('electionEventForm:copyRoles').disabled = true;
		document.getElementById('electionEventForm:copyElections').disabled = true;
		document.getElementById('electionEventForm:copyPostalCodes').disabled = true;
		document.getElementById('electionEventForm:copyElectoralRoll').disabled = true;
        document.getElementById('electionEventForm:copyElectionReportCountCategories').disabled = true;
		document.getElementById('electionEventForm:copyReportCountCategories').disabled = true;
		document.getElementById('electionEventForm:copyReportingUnits').disabled = true;
		document.getElementById('electionEventForm:copyProposerList').disabled = true;		
		document.getElementById('electionEventForm:copyVotings').disabled = true;
		document.getElementById('electionEventForm:copyCountings').disabled = true;

		document.getElementById('electionEventForm:electionEventSelect').checked = false;
		document.getElementById('electionEventForm:copyAreas').checked = false;
		document.getElementById('electionEventForm:copyRoles').checked = false;
		document.getElementById('electionEventForm:copyElections').checked = false;
		document.getElementById('electionEventForm:copyPostalCodes').checked = false;
		document.getElementById('electionEventForm:copyElectoralRoll').checked = false;
        document.getElementById('electionEventForm:copyElectionReportCountCategories').checked = false;
		document.getElementById('electionEventForm:copyReportCountCategories').checked = false;
		document.getElementById('electionEventForm:copyReportingUnits').checked = false;
		document.getElementById('electionEventForm:copyProposerList').checked = false;		
		document.getElementById('electionEventForm:copyVotings').checked = false;
		document.getElementById('electionEventForm:copyCountings').checked = false;
	}
}

function enableDisableArea() {
	if (document.getElementById('electionEventForm:copyAreas').checked == true) {
		document.getElementById('electionEventForm:copyElections').disabled = false;
		document.getElementById('electionEventForm:copyPostalCodes').disabled = false;
	} else {
		document.getElementById('electionEventForm:copyElections').disabled = true;
		document.getElementById('electionEventForm:copyPostalCodes').disabled = true;
		document.getElementById('electionEventForm:copyElectoralRoll').disabled = true;
		document.getElementById('electionEventForm:copyElectionReportCountCategories').disabled = true;
		document.getElementById('electionEventForm:copyReportCountCategories').disabled = true;
		document.getElementById('electionEventForm:copyReportingUnits').disabled = true;
		document.getElementById('electionEventForm:copyProposerList').disabled = true;
		document.getElementById('electionEventForm:copyVotings').disabled = true;
		document.getElementById('electionEventForm:copyCountings').disabled = true;

		document.getElementById('electionEventForm:copyElections').checked = false;
		document.getElementById('electionEventForm:copyPostalCodes').checked = false;
		document.getElementById('electionEventForm:copyElectoralRoll').checked = false;
		document.getElementById('electionEventForm:copyElectionReportCountCategories').checked = false;
		document.getElementById('electionEventForm:copyReportCountCategories').checked = false;
		document.getElementById('electionEventForm:copyReportingUnits').checked = false;
		document.getElementById('electionEventForm:copyProposerList').checked = false;
		document.getElementById('electionEventForm:copyVotings').checked = false;
		document.getElementById('electionEventForm:copyCountings').checked = false;
	}
}

function enableDisableElection() {
    if (document.getElementById('electionEventForm:copyElections').checked == true) {
        document.getElementById('electionEventForm:copyElectoralRoll').disabled = false;
        document.getElementById('electionEventForm:copyElectionReportCountCategories').disabled = false;
		document.getElementById('electionEventForm:copyProposerList').disabled = false;
	} else {
		document.getElementById('electionEventForm:copyElectoralRoll').disabled = true;
		document.getElementById('electionEventForm:copyElectionReportCountCategories').disabled = true;
		document.getElementById('electionEventForm:copyReportCountCategories').disabled = true;
		document.getElementById('electionEventForm:copyReportingUnits').disabled = true;
		document.getElementById('electionEventForm:copyProposerList').disabled = true;
		document.getElementById('electionEventForm:copyVotings').disabled = true;
		document.getElementById('electionEventForm:copyCountings').disabled = true;

		document.getElementById('electionEventForm:copyElectoralRoll').checked = false;
		document.getElementById('electionEventForm:copyElectionReportCountCategories').checked = false;
		document.getElementById('electionEventForm:copyReportCountCategories').checked = false;
		document.getElementById('electionEventForm:copyReportingUnits').checked = false;
		document.getElementById('electionEventForm:copyProposerList').checked = false;
		document.getElementById('electionEventForm:copyVotings').checked = false;
		document.getElementById('electionEventForm:copyCountings').checked = false;
	}
}

function enableDisableElectoralRoll() {
	if (document.getElementById('electionEventForm:copyElectoralRoll').checked == true) {
		document.getElementById('electionEventForm:copyVotings').disabled = false;
	} else {
		document.getElementById('electionEventForm:copyVotings').disabled = true;
		document.getElementById('electionEventForm:copyVotings').checked = false;
	}
}

function enableDisableElectionReportCountCategoriesAndProposerList() {
	if (document.getElementById('electionEventForm:copyElectionReportCountCategories').checked == true && document.getElementById('electionEventForm:copyProposerList').checked == true) {
		document.getElementById('electionEventForm:copyReportCountCategories').disabled = false;
	} else {
		document.getElementById('electionEventForm:copyReportCountCategories').disabled = true;
		document.getElementById('electionEventForm:copyReportingUnits').disabled = true;
		document.getElementById('electionEventForm:copyCountings').disabled = true;

		document.getElementById('electionEventForm:copyReportCountCategories').checked = false;
		document.getElementById('electionEventForm:copyReportingUnits').checked = false;
		document.getElementById('electionEventForm:copyCountings').checked = false;
	}
}

function enableDisableReportCountCategories() {
	if (document.getElementById('electionEventForm:copyReportCountCategories').checked == true) {
		document.getElementById('electionEventForm:copyReportingUnits').disabled = false;
	} else {
		document.getElementById('electionEventForm:copyReportingUnits').disabled = true;
		document.getElementById('electionEventForm:copyCountings').disabled = true;

		document.getElementById('electionEventForm:copyReportingUnits').checked = false;
		document.getElementById('electionEventForm:copyCountings').checked = false;
	}
}

function enableDisableReportingUnit() {
	if (document.getElementById('electionEventForm:copyReportingUnits').checked == true) {
		document.getElementById('electionEventForm:copyCountings').disabled = false;
	} else {
		document.getElementById('electionEventForm:copyCountings').disabled = true;
		document.getElementById('electionEventForm:copyCountings').checked = false;
	}
}

jQuery(document).ready(function() {
	document.getElementById('allowCopying').checked = false;
	changeCopyMode();
});

function setFocus() {
	document.getElementById('allowCopying').checked = false;
	jQuery("input[id='electionEventForm:id']").focus();
	changeCopyMode();
}

function createElectionDialogBox(xhr, status, args) {
	if (args.createElectionHideDialog) {
		createElectionEventWidget.hide();
	} else {

	}
}