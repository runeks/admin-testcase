package no.valg.eva.admin.counting.presentation.view.ballotcount;

public interface BallotCountsWithSplitModelRow extends BallotCountsModelRow {

	boolean isModifiedCountInput();

	void setModifiedCount(Integer modifiedCount);

	Integer getModifiedCount();

	boolean isUnmodifiedCountInput();

	void setUnmodifiedCount(Integer unmodifiedCount);

	Integer getUnmodifiedCount();

}
