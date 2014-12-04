package no.evote.dto;

import java.util.HashSet;
import java.util.Set;

import static no.evote.constants.EvoteConstants.VOTING_CATEGORY_FB;
import static no.evote.constants.EvoteConstants.VOTING_CATEGORY_FE;
import static no.evote.constants.EvoteConstants.VOTING_CATEGORY_FI;
import static no.evote.constants.EvoteConstants.VOTING_CATEGORY_FU;

/**
 * Item for pick list on the "samlet prøving valgting" page
 */
public class ElectionDayPickListItem extends PickListItem {

	public static final String ADVANCE_VOTE_TEXT_ID = "@voting.approveVoting.advanceVote";
	private static Set<String> advanceCats = new HashSet<>();
	{
		advanceCats.add(VOTING_CATEGORY_FI);
		advanceCats.add(VOTING_CATEGORY_FU);
		advanceCats.add(VOTING_CATEGORY_FB);
		advanceCats.add(VOTING_CATEGORY_FE);
	}
	
	private final String updatedVotingCategoryName;

	/**
	 * Creates instance
	 *
	 * @param object result of native queries in VotingServiceImpl
	 */
	public ElectionDayPickListItem(final Object object) {
		super(object);
		updatedVotingCategoryName = advanceCats.contains(votingCategoryId) ? ADVANCE_VOTE_TEXT_ID : votingCategoryName;
	}

	@Override
	public String getVotingCategoryName() {
		return updatedVotingCategoryName;
	}
}
