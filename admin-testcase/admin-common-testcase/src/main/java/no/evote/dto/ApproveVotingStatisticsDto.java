package no.evote.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.evote.constants.EvoteConstants;

import static no.evote.constants.EvoteConstants.DEAD_VOTER;
import static no.evote.constants.EvoteConstants.MULTIPLE_VOTES;
import static no.evote.constants.EvoteConstants.NOT_IN_ELECTORAL_ROLL;
import static no.evote.constants.EvoteConstants.VOTING_CATEGORY_FI;

public class ApproveVotingStatisticsDto implements Serializable {

	private int totalVotes;
	private int totalApproved;
	private int totalNotInElectoralRoll;
	private int totalDead;
	private int totalMultipleVotes;

	private final Map<String, Integer> votingCategoryCounts = new HashMap<>();

	/**
	 * Creates instance populated from parameter lists
	 * @param advanceVoting list of votingDto
	 * @param pickList
	 */
	public ApproveVotingStatisticsDto(final List<VotingDto> advanceVoting, final List<PickListItem> pickList) {
		for (VotingDto av : advanceVoting) {

			String votingCategoryId = av.getVotingCategoryId();
			if (!votingCategoryCounts.containsKey(votingCategoryId)) {
				votingCategoryCounts.put(votingCategoryId, av.getNumberOfVotings().intValue());
			} else {
				votingCategoryCounts.put(votingCategoryId, votingCategoryCounts.get(votingCategoryId) + av.getNumberOfVotings().intValue());
			}

			if (av.isApproved()) {
				totalApproved = totalApproved + av.getNumberOfVotings().intValue();
			}

			totalVotes += av.getNumberOfVotings().intValue();

		}

		for (PickListItem pl : pickList) {
			if (pl.getStatus().equals(NOT_IN_ELECTORAL_ROLL)) {
				totalNotInElectoralRoll = totalNotInElectoralRoll + 1;
			}
			if (pl.getStatus().equals(DEAD_VOTER)) {
				totalDead = totalDead + 1;
			}
			if (pl.getStatus().equals(MULTIPLE_VOTES)) {
				totalMultipleVotes = totalMultipleVotes + 1;
			}
		}
	}

	public boolean isTotalEqualToApproved() {
		return totalVotes == totalApproved;
	}

	public int getTotalVotings() {
		return totalVotes;
	}

	public int getTotalApproved() {
		return totalApproved;
	}

	public int getTotalInCategory(final String category) {
		if (votingCategoryCounts.get(category) == null) {
			return 0;
		} else {
			return votingCategoryCounts.get(category);
		}

	}

	public int getTotalNotInElectoralRoll() {
		return totalNotInElectoralRoll;
	}

	public int getTotalDead() {
		return totalDead;
	}

	public int getTotalMultipleVotes() {
		return totalMultipleVotes;
	}

	public int getTotalAdvanceVotes() {
		return getTotalInCategory(VOTING_CATEGORY_FI) + getTotalInCategory(EvoteConstants.VOTING_CATEGORY_FU)
                + getTotalInCategory(EvoteConstants.VOTING_CATEGORY_FB) + getTotalInCategory(EvoteConstants.VOTING_CATEGORY_FE);
	}
}
