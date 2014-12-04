function changeSearchMode() {
	/*if (document.getElementById('ssnRadio').checked == true) {
		document.getElementById('form:ssn').disabled = false;
		document.getElementById('form:ssn').focus();
		document.getElementById('form:searchSSN').disabled = false;
		document.getElementById('form:searchAdvance').disabled = true;
		document.getElementById('form:nameLine').disabled = true;
		document.getElementById('form:addressLine1').disabled = true;
		document.getElementById('form:date').disabled = true;
		document.getElementById('form:municipality').disabled = true;
	}
	if (document.getElementById('advanceRadio').checked == true) {
		document.getElementById('form:searchAdvance').disabled = false;
		document.getElementById('form:nameLine').disabled = false;
		document.getElementById('form:addressLine1').disabled = false;
		document.getElementById('form:date').disabled = false;
		document.getElementById('form:municipality').disabled = false;
		document.getElementById('form:nameLine').focus();
		document.getElementById('form:ssn').disabled = true;
		document.getElementById('form:searchSSN').disabled = true;
	}*/
	

}

jQuery(document).ready(function() {
	jQuery("input[id='form:ssn']").focus();
});

function submitSSN(e) {
	if (enterHere(e) == true) {
		e.preventDefault();
		e.stopImmediatePropagation();
		$(searchSSN.jqId).click();
		return false;
	}
}

function submitAdvance(e) {
	if (enterHere(e) == true) {
		e.preventDefault();
		e.stopImmediatePropagation();
		$(searchAdvance.jqId).click()
		return false;
	}
}

function enterHere(e) {
	e = e || window.event;
	var code = e.keyCode || e.which;
	if(code == 13) {
		return true;
	}
	return false;
}

jQuery(document).ready(function() {
	//document.getElementById('ssnRadio').checked = true
	//changeSearchMode();
});