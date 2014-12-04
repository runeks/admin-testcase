package no.evote.dto;

import static no.evote.constants.EvoteConstants.DEAD_VOTER;
import static no.evote.constants.EvoteConstants.MULTIPLE_VOTES;
import static no.evote.constants.EvoteConstants.NOT_IN_ELECTORAL_ROLL;

import java.io.Serializable;
import java.util.Date;

public class PickListItem implements Serializable {

	protected String status;
	protected String nameLine;
	protected int votingNumber;
	protected String voterId;
	protected Date receivedTimestamp;
	protected String votingCategoryId;
	protected String votingCategoryName;
	protected boolean fictitious;

	/**
	 * Creates instance
	 * 
	 * @param object
	 *            result of native queries in VotingServiceImpl
	 */
	public PickListItem(final Object object) {
		// CSOFF: MagicNumber
		Object[] objectList = (Object[]) object;
		nameLine = (String) objectList[0];
		votingNumber = (Integer) objectList[1];
		status = (String) objectList[2];
		voterId = (String) objectList[3];
		receivedTimestamp = (Date) objectList[4];
		votingCategoryId = (String) objectList[5];
		votingCategoryName = (String) objectList[6];
		fictitious = (Boolean) objectList[7];
		// CSON: MagicNumber
	}

	/**
	 * @return id for row in JSF datatable based on hash of receivedTimestamp and voterId
	 */
	public String rowKey() {
		return Integer.toString(receivedTimestamp.hashCode() + voterId.hashCode());
	}

	public String getStatus() {
		return status;
	}

	public String getNameLine() {
		return nameLine;
	}

	public void setNameLine(final String nameLine) {
		this.nameLine = nameLine;
	}

	public String getRejectionReason() {
		String rejectionReason = null;
		if (NOT_IN_ELECTORAL_ROLL.equals(status)) {
			rejectionReason = "@voting.approveVoting.suggestedRejection.notInElectoralRoll";
		} else if (DEAD_VOTER.equals(status)) {
			rejectionReason = "@voting.approveVoting.suggestedRejection.dead";
		} else if (MULTIPLE_VOTES.equals(status)) {
			rejectionReason = "@voting.approveVoting.suggestedRejection.duplicateVoting";
		}
		return rejectionReason;
	}

	public int getVotingNumber() {
		return votingNumber;
	}

	public void setVotingNumber(final int votingNumber) {
		this.votingNumber = votingNumber;
	}

	public String getVoterId() {
		return voterId;
	}

	public void setVoterId(final String voterId) {
		this.voterId = voterId;
	}

	public Date getReceivedTimestamp() {
		Date returnDate = null;
		if (this.receivedTimestamp != null) {
			returnDate = new Date(this.receivedTimestamp.getTime());
		}
		return returnDate;
	}

	public void setReceivedTimestamp(final Date receivedTimestamp) {
		if (receivedTimestamp != null) {
			this.receivedTimestamp = new Date(receivedTimestamp.getTime());
		} else {
			this.receivedTimestamp = null;
		}
	}

	public String getVotingCategoryName() {
		return votingCategoryName;
	}

	public void setVotingCategoryName(final String votingCategoryName) {
		this.votingCategoryName = votingCategoryName;
	}

	public boolean isFictitious() {
		return fictitious;
	}

}
