package no.evote.presentation.util;

import no.evote.constants.EvoteConstants;

public final class CountingUtil {

	private CountingUtil() {

	}

	public static String[] getVoteCategory(final String voteCountCategory) {
		if (EvoteConstants.VOTE_COUNT_CATEGORY_EO.equals(voteCountCategory)) {
			return new String[] { EvoteConstants.VOTING_CATEGORY_EO };
		} else if (EvoteConstants.VOTE_COUNT_CATEGORY_EK.equals(voteCountCategory)) {
			return new String[] { EvoteConstants.VOTING_CATEGORY_EK };
		} else if (EvoteConstants.VOTE_COUNT_CATEGORY_FS.equals(voteCountCategory)) {
			return new String[] { EvoteConstants.VOTING_CATEGORY_FI, EvoteConstants.VOTING_CATEGORY_FU, EvoteConstants.VOTING_CATEGORY_FB,
					EvoteConstants.VOTING_CATEGORY_FE };
		} else if (EvoteConstants.VOTE_COUNT_CATEGORY_FO.equals(voteCountCategory)) {
			return new String[] { EvoteConstants.VOTING_CATEGORY_FI, EvoteConstants.VOTING_CATEGORY_FU, EvoteConstants.VOTING_CATEGORY_FB,
					EvoteConstants.VOTING_CATEGORY_FE };
		} else if (EvoteConstants.VOTE_COUNT_CATEGORY_VO.equals(voteCountCategory)) {
			return new String[] { EvoteConstants.VOTING_CATEGORY_VO };
		} else if (EvoteConstants.VOTE_COUNT_CATEGORY_VF.equals(voteCountCategory)) {
			return new String[] { EvoteConstants.VOTING_CATEGORY_VF };
		} else if (EvoteConstants.VOTE_COUNT_CATEGORY_VS.equals(voteCountCategory)) {
			return new String[] { EvoteConstants.VOTING_CATEGORY_VS };
		} else if (EvoteConstants.VOTE_COUNT_CATEGORY_VB.equals(voteCountCategory)) {
			return new String[] { EvoteConstants.VOTING_CATEGORY_VB };
		}

		return new String[] {};
	}

	public static boolean isLate(final String voteCountCategory) {
		if (EvoteConstants.VOTE_COUNT_CATEGORY_FS.equals(voteCountCategory)) {
			return true;
		}
		return false;
	}

}
