function changeSearchMode() {
			var electionDay = "#{cc.attrs.FormMode == 'ELECTION_DAY'}";
			var approveVoting = "#{cc.attrs.FormMode == 'APPROVE_VOTING'}";
			if (document.getElementById('numberRadio').checked == true) {
				document.getElementById('#{cc.clientId}' + ':voterNumber').disabled = false;
				document.getElementById('#{cc.clientId}' + ':voterNumber').focus();
				document.getElementById('#{cc.clientId}' + ':searchNumber').disabled = false;
				document.getElementById('#{cc.clientId}' + ':searchSSN').disabled = true;
				document.getElementById('#{cc.clientId}' + ':searchAdvance').disabled = true;
				document.getElementById('#{cc.clientId}' + ':ssn').disabled = true;
				document.getElementById('#{cc.clientId}' + ':nameLine').disabled = true;
				document.getElementById('#{cc.clientId}' + ':addressLine1').disabled = true;
				document.getElementById('#{cc.clientId}' + ':date').disabled = true;
				document.getElementById('#{cc.clientId}' + ':municipality').disabled = true;
				if (approveVoting == true || approveVoting == 'true') {
					document.getElementById('#{cc.clientId}' + ':votingNumber').disabled = true;
					document.getElementById('#{cc.clientId}' + ':searchVotingNumber').disabled = true;
					document.getElementById('#{cc.clientId}' + ':votingMode:0').disabled = true;
					document.getElementById('#{cc.clientId}' + ':votingMode:1').disabled = true;
				}
			}
			if (document.getElementById('ssnRadio').checked == true) {
				document.getElementById('#{cc.clientId}' + ':ssn').disabled = false;
				document.getElementById('#{cc.clientId}' + ':ssn').focus();
				document.getElementById('#{cc.clientId}' + ':searchSSN').disabled = false;
				document.getElementById('#{cc.clientId}' + ':voterNumber').disabled = true;
				document.getElementById('#{cc.clientId}' + ':searchNumber').disabled = true;
				document.getElementById('#{cc.clientId}' + ':searchAdvance').disabled = true;
				document.getElementById('#{cc.clientId}' + ':nameLine').disabled = true;
				document.getElementById('#{cc.clientId}' + ':addressLine1').disabled = true;
				document.getElementById('#{cc.clientId}' + ':date').disabled = true;
				document.getElementById('#{cc.clientId}' + ':municipality').disabled = true;
				if (approveVoting == true || approveVoting == 'true') {
					document.getElementById('#{cc.clientId}' + ':votingNumber').disabled = true;
					document.getElementById('#{cc.clientId}' + ':searchVotingNumber').disabled = true;
					document.getElementById('#{cc.clientId}' + ':votingMode:0').disabled = true;
					document.getElementById('#{cc.clientId}' + ':votingMode:1').disabled = true;
				}
			}
			if (document.getElementById('advanceRadio').checked == true) {
				document.getElementById('#{cc.clientId}' + ':nameLine').disabled = false;
				document.getElementById('#{cc.clientId}' + ':addressLine1').disabled = false;
				document.getElementById('#{cc.clientId}' + ':date').disabled = false;
				if (electionDay == false || electionDay == 'false') {
					document.getElementById('#{cc.clientId}' + ':municipality').disabled = false;
				}
				if (approveVoting == false || approveVoting == 'false') {
					document.getElementById('#{cc.clientId}' + ':municipality').disabled = false;
				}
				if (electionDay == true || electionDay == 'true') {
					document.getElementById('#{cc.clientId}' + ':municipality').disabled = true;
				}
				if (approveVoting == true || approveVoting == 'true') {
					document.getElementById('#{cc.clientId}' + ':municipality').disabled = true;
					document.getElementById('#{cc.clientId}' + ':votingNumber').disabled = true;
					document.getElementById('#{cc.clientId}' + ':searchVotingNumber').disabled = true;
					document.getElementById('#{cc.clientId}' + ':votingMode:0').disabled = true;
					document.getElementById('#{cc.clientId}' + ':votingMode:1').disabled = true;
				}
				document.getElementById('#{cc.clientId}' + ':nameLine').focus();
				document.getElementById('#{cc.clientId}' + ':voterNumber').disabled = true;
				document.getElementById('#{cc.clientId}' + ':ssn').disabled = true;
				document.getElementById('#{cc.clientId}' + ':searchAdvance').disabled = false;
				document.getElementById('#{cc.clientId}' + ':searchSSN').disabled = true;
				document.getElementById('#{cc.clientId}' + ':searchNumber').disabled = true;
			}
			if (approveVoting == true || approveVoting == 'true') {
				if (document.getElementById('votingNumberRadio').checked == true) {
					document.getElementById('#{cc.clientId}' + ':votingNumber').disabled = false;
					document.getElementById('#{cc.clientId}' + ':votingNumber').focus();
					document.getElementById('#{cc.clientId}' + ':searchVotingNumber').disabled = false;
					document.getElementById('#{cc.clientId}' + ':votingMode:0').disabled = false;
					document.getElementById('#{cc.clientId}' + ':votingMode:1').disabled = false;
					
					document.getElementById('#{cc.clientId}' + ':searchSSN').disabled = true;
					document.getElementById('#{cc.clientId}' + ':searchAdvance').disabled = true;
					document.getElementById('#{cc.clientId}' + ':voterNumber').disabled = true;
					document.getElementById('#{cc.clientId}' + ':searchNumber').disabled = true;
					document.getElementById('#{cc.clientId}' + ':ssn').disabled = true;
					document.getElementById('#{cc.clientId}' + ':nameLine').disabled = true;
					document.getElementById('#{cc.clientId}' + ':addressLine1').disabled = true;
					document.getElementById('#{cc.clientId}' + ':date').disabled = true;
					document.getElementById('#{cc.clientId}' + ':municipality').disabled = true;
				}
			}
		}
	
		function submitSSN(e) {
			if (enterHere(e) == true) {
				if (window.event == undefined) {
					$(searchSSN.jqId).click()
				}
			}
		}
		
		function submitNumber(e) {
			if (enterHere(e) == true) {
				if (window.event == undefined) {
					$(searchNumber.jqId).click()
				}
			}
		}
		
		function submitAdvance(e) {
			if (enterHere(e) == true) {
				if (window.event == undefined) {
					$(searchAdvance.jqId).click()
				}
			}
		}
		
		function submitVotingNumber(e) {
			if (enterHere(e) == true) {
				if (window.event == undefined) {
					document.getElementById('#{cc.clientId}' + ':searchVotingNumber').click();
				}
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
			document.getElementById('numberRadio').checked = true
			changeSearchMode();
		});