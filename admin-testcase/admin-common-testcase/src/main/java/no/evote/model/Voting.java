package no.evote.model;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.logging.AuditLogUtil;
import no.evote.security.ContextSecurable;

/**
 * Information on all votings per voter. Only one voting may be accepted within each election_group
 */
@Entity
@Table(name = "voting", uniqueConstraints = { @UniqueConstraint(columnNames = { "voter_pk", "election_group_pk", "approved" }),
		@UniqueConstraint(columnNames = { "voter_pk", "election_group_pk", "voting_category_pk", "voting_number" }) })
@AttributeOverride(name = "pk", column = @Column(name = "voting_pk"))
@NamedQueries({
		@NamedQuery(
				name = "Voting.findApprovedVotesForVoterByElectionGroup",
				query = "SELECT v FROM Voting v WHERE v.voter.pk = :voterPk AND v.approved = TRUE " + "AND v.electionGroup.pk =:electionGroupPk "),
		@NamedQuery(name = "Voting.findVotingsByElectionGroupAndVoter", query = "SELECT v FROM Voting v WHERE v.voter.pk = :voterPk AND v.electionGroup.pk ="
				+ ":electionGroupPk "),
		@NamedQuery(
				name = "Voting.findVotingsByElectionGroupVoterAndMunicipality",
				query = "SELECT v FROM Voting v, MvArea AS a WHERE a.pollingPlace.pk = v.pollingPlace.pk AND v.voter.pk = :voterPk AND v.electionGroup.pk ="
						+ ":electionGroupPk AND a.municipality.pk = :municipality_pk"),
		@NamedQuery(
				name = "Voting.findReceivedVotingsByElectionGroupAndVoter",
				query = "SELECT v FROM Voting v WHERE v.voter.pk = :voterPk AND v.electionGroup.pk ="
						+ ":electionGroupPk AND v.receivedTimestamp IS NOT NULL AND v.votingCategory.electronicVoting = FALSE "),
		@NamedQuery(name = "Voting.findVotingsReadyForRemovalByPollingPlace", query = "SELECT v FROM Voting v INNER JOIN FETCH v.voter vo"
				+ " WHERE v.removalRequest IS NOT NULL "
				+ "AND v.pollingPlace.pk = :pollingPlacePk AND v.electionGroup.pk = :electionGroupPk ORDER BY vo.lastName, vo.firstName, vo.middleName"),
		@NamedQuery(name = "Voting.findVotingsNotApprovedByElectionAndVoter", query = "SELECT v FROM Voting v WHERE v.voter.pk = :voterPk "
				+ "AND v.electionGroup.pk = :electionGroupPk AND v.approved = FALSE AND v.votingRejection IS NULL AND v.votingNumber > 0 "
				+ "ORDER BY v.castTimestamp DESC"),
		@NamedQuery(
				name = "Voting.findRejectedVotingsByElectionAndMunicipality",
				query = "SELECT v FROM Voting v INNER JOIN FETCH v.voter vo INNER JOIN v.mvArea a" + " WHERE a.municipalityId = :municipalityId "
						+ "AND v.electionGroup.pk = :electionGroupPk AND v.votingRejection IS NOT NULL ORDER BY vo.lastName, vo.firstName, vo.middleName"),
		@NamedQuery(
				name = "Voting.findRejectedVotingsByElectionGroupAndVoter",
				query = "SELECT v FROM Voting v WHERE v.voter.pk = :voterPk "
						+ "AND v.electionGroup.pk = :electionGroupPk AND v.votingRejection IS NOT NULL ORDER BY v.voter.lastName, v.voter.firstName, v.voter.middleName"),
		@NamedQuery(name = "Voting.findApprovedVotingExcludingCategories", query = "SELECT v FROM Voting v WHERE v.voter.pk = :voterPk AND v.approved = TRUE "
				+ "AND v.electionGroup.pk =:electionGroupPk AND " + "v.votingCategory.id NOT IN (:votingCategories)"),
		@NamedQuery(
				name = "Voting.findApprovedVotingsByPollingDistrictAndCategories",
				query = "SELECT v "
						+ "FROM Voting v "
						+ "WHERE v.approved = TRUE "
						+ "AND v.pollingPlace.pollingDistrict.pk = :pollingDistrictPk "
						+ "AND v.votingCategory.id IN (:votingCategoryIds)"
		),
		@NamedQuery(
				name = "Voting.findApprovedVotingCountByPollingDistrictAndCategoriesAndLateValidation",
				query = "SELECT count(v) "
						+ "FROM Voting v "
						+ "WHERE v.approved = TRUE "
						+ "AND v.voter.mvArea.pollingDistrict.pk = :pollingDistrictPk "
						+ "AND v.votingCategory.id IN (:votingCategoryIds) "
						+ "AND v.lateValidation = :lateValidationFilter"
		),
		@NamedQuery(
				name = "Voting.findNotRejectedVotingCountByPollingDistrictAndCategoriesAndLateValidation",
				query = "SELECT count(v) "
						+ "FROM Voting v "
						+ "WHERE v.votingRejection IS NULL "
						+ "AND v.voter.mvArea.pollingDistrict.pk = :pollingDistrictPk "
						+ "AND v.votingCategory.id IN (:votingCategoryIds) "
						+ "AND v.lateValidation = :lateValidationFilter"
		),
		@NamedQuery(
				name = "Voting.findApprovedVotingCountByMunicipalityAndCategoriesAndLateValidation",
				query = "SELECT count(v) "
						+ "FROM Voting v "
						+ "WHERE v.approved = TRUE "
						+ "AND v.voter.mvArea.municipality.pk = :municipalityPk "
						+ "AND v.votingCategory.id IN (:votingCategoryIds) "
						+ "AND v.lateValidation = :lateValidationFilter"
		),
		@NamedQuery(
				name = "Voting.findNotRejectedVotingCountByMunicipalityAndCategoriesAndLateValidation",
				query = "SELECT count(v) "
						+ "FROM Voting v "
						+ "WHERE v.votingRejection IS NULL "
						+ "AND v.voter.mvArea.municipality.pk = :municipalityPk "
						+ "AND v.votingCategory.id IN (:votingCategoryIds) "
						+ "AND v.lateValidation = :lateValidationFilter"
		),
		@NamedQuery(
				name = "Voting.findMarkOffInOtherBoroughs",
				query = "SELECT count(v) "
					+ "FROM Voting v "
					+ "WHERE v.votingRejection IS NULL "
					+ "AND v.pollingPlace.pollingDistrict.borough.municipality.pk = v.voter.mvArea.municipality.pk "
					+ "AND v.pollingPlace.pollingDistrict.borough.pk != v.voter.mvArea.borough.pk "
					+ "AND v.votingCategory.id = 'VF' "
					+ "AND v.voter.mvArea.borough.pk = :boroughPk"
		)
})
@SuppressWarnings({ "PMD.AvoidDuplicateLiterals" })
public class Voting extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private MvArea mvArea;
	private VotingCategory votingCategory;
	private VotingRejection votingRejection;
	@Deprecated
	private Voter voter;
	private PollingPlace pollingPlace;
	@Deprecated
	private ElectionGroup electionGroup;
	private ElectoralRollEntry electoralRollEntry;
	private Integer votingNumber;
	private Date castTimestamp;
	private Date receivedTimestamp;
	private Date validationTimestamp;
	private boolean approved;
	private String removalRequest;
	private boolean lateValidation;
	private String ballotBoxId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mv_area_pk", nullable = false)
	public MvArea getMvArea() {
		return mvArea;
	}

	public void setMvArea(final MvArea mvArea) {
		this.mvArea = mvArea;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "voting_category_pk", nullable = false)
	public VotingCategory getVotingCategory() {
		return votingCategory;
	}

	public void setVotingCategory(final VotingCategory votingCategory) {
		this.votingCategory = votingCategory;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "voting_rejection_pk")
	public VotingRejection getVotingRejection() {
		return votingRejection;
	}

	public void setVotingRejection(final VotingRejection votingRejection) {
		this.votingRejection = votingRejection;
	}

	@Deprecated
	@ManyToOne
	@JoinColumn(name = "voter_pk", nullable = false)
	public Voter getVoter() {
		return voter;
	}

	@Deprecated
	public void setVoter(final Voter voter) {
		this.voter = voter;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "polling_place_pk")
	public PollingPlace getPollingPlace() {
		return pollingPlace;
	}

	public void setPollingPlace(final PollingPlace pollingPlace) {
		this.pollingPlace = pollingPlace;
	}

	@Deprecated
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "election_group_pk", nullable = false)
	public ElectionGroup getElectionGroup() {
		return electionGroup;
	}

	@Deprecated
	public void setElectionGroup(final ElectionGroup electionGroup) {
		this.electionGroup = electionGroup;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "electoral_roll_entry_pk", nullable = true)     // TODO EVAADMIN-280 nullable bør etterhvert settes til false..
	public ElectoralRollEntry getElectoralRollEntry() {
		return electoralRollEntry;
	}

	public void setElectoralRollEntry(final ElectoralRollEntry electoralRollEntry) {
		this.electoralRollEntry = electoralRollEntry;
	}

	@Column(name = "voting_number", insertable = false, updatable = false)
	public Integer getVotingNumber() {
		return votingNumber;
	}

	public void setVotingNumber(final Integer votingNumber) {
		this.votingNumber = votingNumber;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cast_timestamp", nullable = false, length = 29)
	public Date getCastTimestamp() {
		Date returnDate = null;
		if (castTimestamp != null) {
			returnDate = new Date(castTimestamp.getTime());
		}
		return returnDate;
	}

	public void setCastTimestamp(final Date castTimestamp) {
		if (castTimestamp != null) {
			this.castTimestamp = new Date(castTimestamp.getTime());
		} else {
			this.castTimestamp = null;
		}
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "received_timestamp", length = 29)
	public Date getReceivedTimestamp() {
		Date returnDate = null;
		if (receivedTimestamp != null) {
			returnDate = new Date(receivedTimestamp.getTime());
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "validation_timestamp", length = 29)
	public Date getValidationTimestamp() {
		Date returnDate = null;
		if (validationTimestamp != null) {
			returnDate = new Date(validationTimestamp.getTime());
		}
		return returnDate;
	}

	public void setValidationTimestamp(final Date validationTimestamp) {
		if (validationTimestamp != null) {
			this.validationTimestamp = new Date(validationTimestamp.getTime());
		} else {
			this.validationTimestamp = null;
		}
	}

	@Column(name = "approved", nullable = false)
	public boolean isApproved() {
		return approved;
	}

	public void setApproved(final boolean approved) {
		this.approved = approved;
	}

	@Column(name = "removal_request", length = 150)
	@Size(max = 150)
	public String getRemovalRequest() {
		return removalRequest;
	}

	public void setRemovalRequest(final String removalRequest) {
		this.removalRequest = removalRequest;
	}

	@Column(name = "late_validation", nullable = false)
	public boolean isLateValidation() {
		return lateValidation;
	}

	public void setLateValidation(final boolean lateValidation) {
		this.lateValidation = lateValidation;
	}

	@Column(name = "ballot_box_id", length = 4)
	@Size(max = 4)
	public String getBallotBoxId() {
		return ballotBoxId;
	}

	public void setBallotBoxId(final String ballotBoxId) {
		this.ballotBoxId = ballotBoxId;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		if (level.equals(AreaLevelEnum.POLLING_PLACE) && pollingPlace != null) {
			return pollingPlace.getPk();
		}

		return null;
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		if (level.equals(ElectionLevelEnum.ELECTION_GROUP)) {
			return electionGroup.getPk();
		}

		return null;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(voter, electionGroup, approved, receivedTimestamp);
	}
}
