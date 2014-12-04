package no.valg.eva.admin.common.counting.model;

import static java.util.Collections.emptyList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.evote.exception.ValidateException;
import no.valg.eva.admin.common.AreaPath;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PreliminaryCount extends AbstractCount {
	public static final String COUNT_ERROR_VALIDATION_NEGATIVE_QUESTIONABLE_BALLOT_COUNT = "@count.error.validation.negative.questionable_ballot_count";
	public static final String COUNT_ERROR_VALIDATION_MISSING_COMMENT = "@count.error.validation.missing_comment.preliminary_count";

	protected Integer markOffCount;
	protected List<BallotCount> ballotCounts;
	protected int questionableBallotCount;
	protected boolean splitPreliminaryCount;
	protected Integer lateValidationCovers;

	public PreliminaryCount(String id, AreaPath areaPath, CountCategory category, String areaName, String reportingUnitAreaName, boolean manualCount) {
		super(id, areaPath, CountQualifier.PRELIMINARY, category, areaName, reportingUnitAreaName, manualCount, null);
	}

	public PreliminaryCount(
			String id,
			AreaPath areaPath,
			CountCategory category,
			String areaName,
			String reportingUnitAreaName,
			boolean manualCount,
			Integer blankBallotCount) {
		super(id, areaPath, CountQualifier.PRELIMINARY, category, areaName, reportingUnitAreaName, manualCount, blankBallotCount);
	}

	public Integer getMarkOffCount() {
		return markOffCount;
	}

	public void setMarkOffCount(Integer markOffCount) {
		this.markOffCount = markOffCount;
	}

	public List<BallotCount> getBallotCounts() {
		return ballotCounts;
	}

	public void setBallotCounts(List<BallotCount> ballotCounts) {
		this.ballotCounts = ballotCounts;
	}

	public Integer getQuestionableBallotCount() {
		return questionableBallotCount;
	}

	public void setQuestionableBallotCount(Integer questionableBallotCount) {
		this.questionableBallotCount = questionableBallotCount;
	}

	@Override
	public boolean hasRejectedBallotCounts() {
		return false;
	}

	@Override
	public List<RejectedBallotCount> getRejectedBallotCounts() {
		return emptyList();
	}

	@Override
	public int getTotalRejectedBallotCount() {
		return 0;
	}

	public boolean isSplitPreliminaryCount() {
		return splitPreliminaryCount;
	}

	public void setSplitPreliminaryCount(boolean splitPreliminaryCount) {
		this.splitPreliminaryCount = splitPreliminaryCount;
	}

	@Override
	public Integer getLateValidationCovers() {
		return lateValidationCovers;
	}

	public void setLateValidationCovers(Integer lateValidationCovers) {
		this.lateValidationCovers = lateValidationCovers;
	}

	@Override
	public void validate() {
		super.validate();

		if (questionableBallotCount < 0) {
			throw new ValidateException(COUNT_ERROR_VALIDATION_NEGATIVE_QUESTIONABLE_BALLOT_COUNT);
		}
		if (blankBallotCount == null) {
			throw new IllegalArgumentException("ILLEGAL_ARGUMENT_BLANK_IS_NULL");
		}
		if (ballotCounts == null) {
			throw new IllegalArgumentException("ILLEGAL_ARGUMENT_BALLOT_COUNTS_IS_NULL");
		}
		if (ballotCounts.isEmpty()) {
			throw new IllegalArgumentException("ILLEGAL_ARGUMENT_BALLOT_COUNTS_IS_EMPTY");
		}
	}

	@Override
	public void validateForApproval() {
		validate();
		if (!hasComment() && isCommentRequired()) {
			throw new ValidateException(COUNT_ERROR_VALIDATION_MISSING_COMMENT);
		}
	}

	public boolean isCommentRequired() {
		return getMarkOffCountDifferenceWithTotalBallotCount() != 0;
	}

	@Override
	public int getTotalBallotCount() {
		return getOrdinaryBallotCount() + blankBallotCount + questionableBallotCount;
	}

	/**
	 * @throws java.lang.NullPointerException
	 *             when mark off count is missing
	 */
	public int getMarkOffCountDifferenceWithTotalBallotCount() {
		Integer markOffCount;
		switch (category) {
		case FO:
			markOffCount = this.markOffCount - lateValidationCovers;
			break;
		case FS:
			markOffCount = this.markOffCount + lateValidationCovers;
			break;
		default:
			markOffCount = this.markOffCount;
		}
		return getTotalBallotCount() - (markOffCount != null ? markOffCount : 0);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		PreliminaryCount rhs = (PreliminaryCount) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(this.markOffCount, rhs.markOffCount)
				.append(this.ballotCounts, rhs.ballotCounts)
				.append(this.questionableBallotCount, rhs.questionableBallotCount)
				.append(this.splitPreliminaryCount, rhs.splitPreliminaryCount)
				.append(this.lateValidationCovers, rhs.lateValidationCovers)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(markOffCount)
				.append(ballotCounts)
				.append(questionableBallotCount)
				.append(splitPreliminaryCount)
				.append(lateValidationCovers)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.appendSuper(super.toString())
				.append("markOffCount", markOffCount)
				.append("ballotCounts", ballotCounts)
				.append("questionableBallotCount", questionableBallotCount)
				.append("splitPreliminaryCount", splitPreliminaryCount)
				.append("lateValidationCovers", lateValidationCovers)
				.toString();
	}

	public Map<String, BallotCount> getBallotCountMap() {
		Map<String, BallotCount> ballotCountMap = new HashMap<>();
		for (final BallotCount ballotCount : ballotCounts) {
			ballotCountMap.put(ballotCount.getId(), ballotCount);
		}
		return ballotCountMap;
	}
}
