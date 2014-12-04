package no.evote.model;


import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * Base class for count categories
 */
@MappedSuperclass
public abstract class CountCategory extends VersionedEntity {

	protected VoteCountCategory voteCountCategory;
	protected ElectionGroup electionGroup;
	protected boolean centralPreliminaryCount = true;
	protected boolean pollingDistrictCount;
	protected boolean specialCover;
	protected boolean splitPreliminaryCount;

	protected boolean countCategoryEnabled;
	protected boolean countCategoryEditable;
	protected boolean technicalPollingDistrictCountConfigurable;

	/**
	 * @return id of VoteCountCategory
	 */
	public String key() {
		return voteCountCategory.getId();
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vote_count_category_pk", nullable = false)
	public VoteCountCategory getVoteCountCategory() {
		return voteCountCategory;
	}

	public void setVoteCountCategory(final VoteCountCategory voteCountCategory) {
		this.voteCountCategory = voteCountCategory;
	}

	@Column(name = "special_cover", nullable = false)
	public boolean isSpecialCover() {
		return specialCover;
	}

	public void setSpecialCover(final boolean specialCover) {
		this.specialCover = specialCover;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "election_group_pk", nullable = false)
	public ElectionGroup getElectionGroup() {
		return electionGroup;
	}

	public void setElectionGroup(final ElectionGroup electionGroup) {
		this.electionGroup = electionGroup;
	}

	@Column(name = "split_preliminary_count", nullable = false)
	public boolean isSplitPreliminaryCount() {
		return splitPreliminaryCount;
	}

	public void setSplitPreliminaryCount(final boolean splitPreliminaryCount) {
		this.splitPreliminaryCount = splitPreliminaryCount;
	}

	@Column(name = "central_preliminary_count", nullable = false)
	public boolean isCentralPreliminaryCount() {
		return this.centralPreliminaryCount;
	}

	public void setCentralPreliminaryCount(final boolean centralPreliminaryCount) {
		this.centralPreliminaryCount = centralPreliminaryCount;
	}

	@Column(name = "polling_district_count", nullable = false)
	public boolean isPollingDistrictCount() {
		return this.pollingDistrictCount;
	}

	public void setPollingDistrictCount(final boolean pollingDistrictCount) {
		this.pollingDistrictCount = pollingDistrictCount;
	}

}
