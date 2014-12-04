package no.valg.eva.admin.common.counting.model.modifiedballots;

import java.util.List;

public class RegisterModifiedBallotCountStatus {
	private List<ModifiedBallotsStatus> modifiedBallotsStatusList;
	private Long voteCountPk;

	public Long getVoteCountPk() {
		return voteCountPk;
	}

	public void setVoteCountPk(final Long voteCountPk) {
		this.voteCountPk = voteCountPk;
	}

	public List<ModifiedBallotsStatus> getModifiedBallotsStatusList() {
		return modifiedBallotsStatusList;
	}

	public void setModifiedBallotsStatusList(final List<ModifiedBallotsStatus> modifiedBallotsStatusList) {
		this.modifiedBallotsStatusList = modifiedBallotsStatusList;
	}

	public boolean isRegistrationOfAllModifiedBallotsCompleted() {
		for (ModifiedBallotsStatus modifiedBallotsStatus : modifiedBallotsStatusList) {
			if (!modifiedBallotsStatus.isRegistrationComplete()) {
				return false;
			}
		}
		return true;
	}
}
