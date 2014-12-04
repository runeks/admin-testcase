jQuery(document).ready(function() {
	sumOrdinaryVotes();
	preliminaryTotalVotes();
	diffRejectedVotes();
	diffBlank();
	
	attachKeyEvent(jQuery(".unmodified"), function() {
		sumRow(jQuery(this));
		sumOrdinaryVotes();
		preliminaryTotalVotes();
	});

	attachKeyEvent(jQuery(".modified"), function() {
		sumRow(jQuery(this));
		sumOrdinaryVotes();
		preliminaryTotalVotes();
	});
	
	attachKeyEvent(jQuery(".blankPreliminary"), function() {
		preliminaryTotalVotes();
		diffBlank();
	});
	
	attachKeyEvent(jQuery(".preliminaryRejectedVotes"), function() {
		preliminaryTotalVotes();
		diffRejectedVotes();
	});
	
	attachKeyEvent(jQuery(".lateValidationCovers"), function() {
		sumPolls();
		diffTotal();
	});
	
	attachKeyEvent(jQuery(".manualContestVoting"), function() {
		diffTotal();
	});
	
	attachKeyEvent(jQuery(".technicalVotings"), function() {
		var technicalVotingsdb = intOrZerro(jQuery(".technicalVotingsDb").text());
		var technicalVotings = intOrZerro(jQuery(".technicalVotings").val());
		var totalTechnicalVotings = intOrZerro(jQuery(".totalTechnicalVotingsDb").text());
		
		jQuery(".totalTechnicalVotings").text(totalTechnicalVotings + (technicalVotings - technicalVotingsdb));
		diffTotal();
	});
	
	function sumPolls() {
		var lateValidationCovers = intOrZerro(jQuery(".lateValidationCovers").val());
		var polls = intOrZerro(jQuery(".polls").text());
		var sum = polls - lateValidationCovers
		jQuery(".protocolTotalVotes").text(sum);
	}
	
	function intOrZerro(value) {
		return isNaN(value) || value == '' ? 0 : parseInt(value);
	}

	function sumRow(updated) {
		var value1 = parseInt(jQuery(updated).val());
		var td = jQuery(updated).parent().siblings();
		var value2 = 0;
		if (updated.hasClass("modified")) {
			value2 = parseInt(td.children(".unmodified").val());
		} else {
			value2 = parseInt(td.children(".modified").val());
		}

		if (isNaN(value1)) {
			value1 = 0;
		}
		if (isNaN(value2)) {
			value2 = 0;
		}
		
		var sum = value1 + value2;
		td.children(".sumPreliminary").text(sum);
	}
	
	function sumOrdinaryVotes() {
		if (jQuery('.sumPreliminary').length != 0) {
			sumOrdinaryVotesSplited();
		} else {
			sumOrdinaryVotesNonSplited()
		}
	}
	
	function sumOrdinaryVotesSplited() {
		var sum = 0;

		jQuery('.sumPreliminary').each(function() {
			value = jQuery(this).text();
			if (value != '' && !isNaN(value)) {
				sum += parseInt(value);
			}
		});
		diffOrdinaryVotes(sum);
	}
	
	function sumOrdinaryVotesNonSplited() {
		var sum = 0;
		jQuery('.unmodified').each(function() {
			value = jQuery(this).val();
			if (value != '' && !isNaN(value)) {
				sum += parseInt(value);
			}
		});
		diffOrdinaryVotes(sum);
	}
	
	function diffOrdinaryVotes(sum){
		jQuery('.preliminaryOrdinaryVotes').text(sum);
		var protocolOrdinaryVotes = jQuery('.protocolapprovedVotes').text();
		if (protocolOrdinaryVotes != '') {
			var ordinaryVoteDiff = jQuery('.ordinaryVoteDiff');
			if (protocolOrdinaryVotes == '') {
				protocolOrdinaryVotes = 0;
			}
			var diff = sum - protocolOrdinaryVotes;
			ordinaryVoteDiff.text(diff);
			
			if (diff < 0) {
				ordinaryVoteDiff.addClass("indicate-down");
			} else if (diff > 0) {
				ordinaryVoteDiff.addClass("indicate-up");
			} else {
				ordinaryVoteDiff.removeClass("indicate-up");
				ordinaryVoteDiff.removeClass("indicate-down");
			}
			diffTotal();
		}
	}
	function preliminaryTotalVotes() {
		var preliminaryRejectedVotes = parseInt(jQuery('.preliminaryOrdinaryVotes').text());
		var preliminaryOrdinaryVotes = parseInt(jQuery('.preliminaryRejectedVotes').val());
		
		if (isNaN(preliminaryRejectedVotes)) {
			preliminaryRejectedVotes = 0;
		}
		if (isNaN(preliminaryOrdinaryVotes)) {
			preliminaryOrdinaryVotes = 0;
		}
		
		
		var blankPreliminary = parseInt(jQuery('.blankPreliminary').val());
		if (isNaN(blankPreliminary)) {
			blankPreliminary = 0;
		}
		
		var sum = preliminaryOrdinaryVotes + preliminaryRejectedVotes;
		if (!isNaN(blankPreliminary)) {
			sum += blankPreliminary;
		}
		jQuery('.preliminaryTotalVotes').text(sum);
		diffTotal();
	}
	
	function diffRejectedVotes(){
		var protocolRejectedVotes = jQuery('.protocolrejectedVotes').text();
		if (protocolRejectedVotes == '') {
			// protocolRejectedVotes = 0; Bug #1868
			return;
		}
		var preliminaryRejectedVotes = parseInt(jQuery('.preliminaryRejectedVotes').val());
		if (isNaN(preliminaryRejectedVotes)) {
			preliminaryRejectedVotes = 0;
		}
		
		var diff = preliminaryRejectedVotes - parseInt(protocolRejectedVotes);
		
		var rejectedVotesDiff = jQuery('.rejectedVotesDiff');
		
		rejectedVotesDiff.text(diff);
		
		if (diff < 0) {
			rejectedVotesDiff.addClass("indicate-down");
		} else if (diff > 0) {
			rejectedVotesDiff.addClass("indicate-up");
		} else {
			rejectedVotesDiff.removeClass("indicate-up");
			rejectedVotesDiff.removeClass("indicate-down");
		}
		diffTotal();
	}
	
	function diffBlank(){
		var protocolBlank = jQuery('.protocolBlank').text();
		if (protocolBlank == '') {
			// protocolBlank = 0; Bug #1868
			return;
		}
		var preliminaryRejectedVotes = parseInt(jQuery('.blankPreliminary').val());
		if (isNaN(preliminaryRejectedVotes)) {
			preliminaryRejectedVotes = 0;
		}
		var diff = preliminaryRejectedVotes - parseInt(protocolBlank);
		
		var preliminaryBlankDiff = jQuery('.preliminaryBlankDiff');
		
		preliminaryBlankDiff.text(diff);
		
		if (diff < 0) {
			preliminaryBlankDiff.addClass("indicate-down");
		} else if (diff > 0) {
			preliminaryBlankDiff.addClass("indicate-up");
		} else {
			preliminaryBlankDiff.removeClass("indicate-up");
			preliminaryBlankDiff.removeClass("indicate-down");
		}
		diffTotal();
	}
	
	function diffTotal() {
		var protocolTotalVotes = intOrZerro(jQuery('.protocolTotalVotes').text());
		var preliminaryTotalVotes = intOrZerro(jQuery('.preliminaryTotalVotes').text());
		
		if (jQuery('.technicalVotings').length > 0) { // on technical polling district use technicalVotings
			protocolTotalVotes = intOrZerro(jQuery('.technicalVotings').val());
		}
		
		var manualContestVoting = 0;
			
		jQuery('.manualContestVoting').each(function() {
			manualContestVoting += intOrZerro(jQuery(this).val());	
		});
		
		var sum = preliminaryTotalVotes - (protocolTotalVotes + manualContestVoting);

		var totalDiff = jQuery('.totalDiff');
		
		totalDiff.text(sum);
		
		if (sum < 0) {
			totalDiff.addClass("indicate-down");
		} else if (sum > 0) {
			totalDiff.addClass("indicate-up");
		} else {
			totalDiff.removeClass("indicate-up");
			totalDiff.removeClass("indicate-down");
		}
	}

});