package no.evote.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import no.evote.constants.EvoteConstants;
import no.evote.model.Voting;

public class VotingPollingPlaceValidator implements ConstraintValidator<VotingPollingPlace, Voting> {

	@Override
	public void initialize(final VotingPollingPlace constraintAnnotation) {
		// No initialization needed
	}

	/**
	 * Polling place can be null only if the voting is an evote (i.e. EK or EO)
	 */
	@Override
	public boolean isValid(final Voting voting, final ConstraintValidatorContext context) {
		String id = voting.getVotingCategory().getId();
		if (voting.getPollingPlace() == null && !(id.equals(EvoteConstants.VOTE_COUNT_CATEGORY_EK) || id.equals(EvoteConstants.VOTE_COUNT_CATEGORY_EO))) {
			return false;
		}

		return true;
	}

}
