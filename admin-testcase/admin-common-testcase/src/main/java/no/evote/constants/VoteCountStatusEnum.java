package no.evote.constants;

public enum VoteCountStatusEnum {

	NONE(-1), COUNTING(0), TO_APPROVAL(1), APPROVED(2), TO_SETTLEMENT(3), REJECTED(4);

	private final int status;

	VoteCountStatusEnum(final int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public static VoteCountStatusEnum getStatus(final int status) {
		if (status == COUNTING.status) {
			return COUNTING;
		} else if (status == TO_APPROVAL.status) {
			return TO_APPROVAL;
		} else if (status == APPROVED.status) {
			return APPROVED;
		} else if (status == TO_SETTLEMENT.status) {
			return TO_SETTLEMENT;
		} else if (status == REJECTED.status) {
			return REJECTED;
		}

		return NONE;
	}
}
