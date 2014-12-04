package no.evote.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.logging.AuditLogUtil;
import no.evote.security.ContextSecurable;
import no.evote.validation.ID;
import no.evote.validation.LettersOrDigits;
import no.evote.validation.StringNotNullEmptyOrBlanks;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.ElectionPath;

/**
 * Election within an election group
 */
@Entity
@Table(name = "election", uniqueConstraints = @UniqueConstraint(columnNames = { "election_group_pk", "election_id" }))
@AttributeOverride(name = "pk", column = @Column(name = "election_pk"))
@NamedQueries({
		@NamedQuery(name = "Election.findById", query = "SELECT e FROM Election e WHERE e.electionGroup.pk = :electionGroupPk AND e.id = :id"),
		@NamedQuery(name = "Election.findByElectionGroup", query = "SELECT e FROM Election e WHERE e.electionGroup.pk = :electionGroupPk"),
		@NamedQuery(name = "Election.findByEvent", query = "select e from Election e, ElectionGroup eg where e.electionGroup.pk = "
				+ "eg.pk and eg.electionEvent.pk = :electionEventPk"),
		@NamedQuery(
				name = "Election.findElectionInElectionEvent",
				query = "select e from Election e where e.electionGroup.electionEvent.id = :electionEventId " + "AND e.id = :electionId") })
public class Election extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private Locale locale;
	private ElectionType electionType;
	private ElectionGroup electionGroup;
	private ElectionStatus electionStatus;
	private String id;
	private String name;
	private int areaLevel;
	private boolean singleArea;
	private String position;
	private boolean renumber;
	private boolean renumberLimit;
	private boolean writein;
	private boolean strikeout;
	private BigDecimal baselineVoteFactor;
	private Date endDateOfBirth;
	private boolean personal;
	private BigDecimal candidateRankVoteShareThreshold;
	private BigDecimal settlementFirstDivisor;
	private boolean penultimateRecount;
	private boolean candidatesInContestArea;
	private int levelingSeats;
	private BigDecimal levelingSeatsVoteShareThreshold;
	private Set<Contest> contests;

	public Election() {
	}

	public Election(final String id, final String name, final ElectionType electionType, final int areaLevel, final Locale locale,
			final ElectionGroup electionGroup) {
		this.id = id;
		this.name = name;
		this.electionType = electionType;
		this.areaLevel = areaLevel;
		this.locale = locale;
		this.electionGroup = electionGroup;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "locale_pk", nullable = false)
	@NotNull
	public Locale getLocale() {
		return this.locale;
	}

	public void setLocale(final Locale locale) {
		this.locale = locale;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "election_type_pk", nullable = false)
	@NotNull
	public ElectionType getElectionType() {
		return this.electionType;
	}

	public void setElectionType(final ElectionType electionType) {
		this.electionType = electionType;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "election_group_pk", nullable = false)
	@NotNull
	public ElectionGroup getElectionGroup() {
		return this.electionGroup;
	}

	public void setElectionGroup(final ElectionGroup electionGroup) {
		this.electionGroup = electionGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "election_status_pk", nullable = false)
	public ElectionStatus getElectionStatus() {
		return this.electionStatus;
	}

	public void setElectionStatus(final ElectionStatus electionStatus) {
		this.electionStatus = electionStatus;
	}

	@Column(name = "election_id", nullable = false, length = 8)
	@ID(size = 2)
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "election_name", nullable = false, length = 100)
	@LettersOrDigits
	@StringNotNullEmptyOrBlanks
	@Size(max = 100)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "area_level", nullable = false)
	@Min(0)
	@Max(7)
	public int getAreaLevel() {
		return this.areaLevel;
	}

	public void setAreaLevel(final int areaLevel) {
		this.areaLevel = areaLevel;
	}

	@Column(name = "single_area", nullable = false)
	public boolean isSingleArea() {
		return this.singleArea;
	}

	public void setSingleArea(final boolean singleArea) {
		this.singleArea = singleArea;
	}

	@Column(name = "position", length = 100)
	@LettersOrDigits
	@Size(max = 100)
	public String getPosition() {
		return this.position;
	}

	public void setPosition(final String position) {
		this.position = position;
	}

	@Column(name = "renumber", nullable = false)
	public boolean isRenumber() {
		return this.renumber;
	}

	public void setRenumber(final boolean renumber) {
		this.renumber = renumber;
	}

	@Column(name = "renumber_limit", nullable = false)
	public boolean isRenumberLimit() {
		return this.renumberLimit;
	}

	public void setRenumberLimit(final boolean renumberLimit) {
		this.renumberLimit = renumberLimit;
	}

	@Column(name = "writein", nullable = false)
	public boolean isWritein() {
		return this.writein;
	}

	public void setWritein(final boolean writein) {
		this.writein = writein;
	}

	@Column(name = "strikeout", nullable = false)
	public boolean isStrikeout() {
		return this.strikeout;
	}

	public void setStrikeout(final boolean strikeout) {
		this.strikeout = strikeout;
	}

	@Column(name = "baseline_vote_factor", precision = 3)
	@Min(0)
	@Max(1)
	public BigDecimal getBaselineVoteFactor() {
		return this.baselineVoteFactor;
	}

	public void setBaselineVoteFactor(final BigDecimal baselineVoteFactor) {
		this.baselineVoteFactor = baselineVoteFactor;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date_of_birth", nullable = false, length = 13)
	@NotNull
	@Past
	public Date getEndDateOfBirth() {
		Date returnDate = null;
		if (this.endDateOfBirth != null) {
			returnDate = new Date(this.endDateOfBirth.getTime());
		}
		return returnDate;
	}

	public void setEndDateOfBirth(final Date endDateOfBirth) {
		if (endDateOfBirth != null) {
			this.endDateOfBirth = new Date(endDateOfBirth.getTime());
		} else {
			this.endDateOfBirth = null;
		}
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		return null;
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		switch (level) {
		case ELECTION_GROUP:
			return electionGroup.getPk();
		case ELECTION:
			return getPk();
		default:
			return null;
		}
	}

	@Column(name = "personal", nullable = false)
	public boolean isPersonal() {
		return this.personal;
	}

	public void setPersonal(final boolean personal) {
		this.personal = personal;
	}

	@Column(name = "candidate_rank_vote_share_threshold", nullable = false, precision = 3)
	public BigDecimal getCandidateRankVoteShareThreshold() {
		return this.candidateRankVoteShareThreshold;
	}

	public void setCandidateRankVoteShareThreshold(final BigDecimal candidateRankVoteShareThreshold) {
		this.candidateRankVoteShareThreshold = candidateRankVoteShareThreshold;
	}

	@Column(name = "settlement_first_divisor", nullable = false, precision = 3)
	public BigDecimal getSettlementFirstDivisor() {
		return this.settlementFirstDivisor;
	}

	public void setSettlementFirstDivisor(final BigDecimal settlementFirstDivisor) {
		this.settlementFirstDivisor = settlementFirstDivisor;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(electionGroup, id);
	}

	@Column(name = "penultimate_recount", nullable = false)
	public boolean isPenultimateRecount() {
		return this.penultimateRecount;
	}

	public void setPenultimateRecount(final boolean penultimateRecount) {
		this.penultimateRecount = penultimateRecount;
	}

	@Column(name = "candidates_in_contest_area", nullable = false)
	public boolean isCandidatesInContestArea() {
		return this.candidatesInContestArea;
	}

	public void setCandidatesInContestArea(final boolean candidatesInContestArea) {
		this.candidatesInContestArea = candidatesInContestArea;
	}

	@Column(name = "leveling_seats", nullable = false)
	public int getLevelingSeats() {
		return this.levelingSeats;
	}

	public void setLevelingSeats(final int levelingSeats) {
		this.levelingSeats = levelingSeats;
	}

	@Column(name = "leveling_seats_vote_share_threshold", nullable = false, precision = 3)
	public BigDecimal getLevelingSeatsVoteShareThreshold() {
		return this.levelingSeatsVoteShareThreshold;
	}

	public void setLevelingSeatsVoteShareThreshold(final BigDecimal levelingSeatsVoteShareThreshold) {
		this.levelingSeatsVoteShareThreshold = levelingSeatsVoteShareThreshold;
	}

	/**
	 * @return true if areaLevel indicates this is a municipality election
	 */
	@Transient
	public boolean isMunicipalityElection() {
		return getAreaLevel() == AreaLevelEnum.MUNICIPALITY.getLevel();
	}

	@OneToMany(mappedBy = "election")
	public Set<Contest> getContests() {
		return contests;
	}

	public void setContests(Set<Contest> contests) {
		this.contests = contests;
	}

	public boolean hasContestsForAreaPath(AreaPath areaPath) {
		Set<Contest> contests = getContests();
		for (Contest contest : contests) {
			if (contest.hasContestAreaForAreaPath(areaPath)) {
				return true;
			}
		}
		return false;
	}

	public ElectionPath electionPath() {
		return getElectionGroup().electionPath().add(getId());
	}

	@Transient
	public boolean isReferendum() {
		return getElectionType().isReferendum();
	}

	@Transient
	public boolean isOnBoroughLevel() {
		Set<Contest> contests = getContests();
		if (contests == null || contests.isEmpty()) {
			return false;
		}
		// either every contests for this election is on borough level or none is, so it's sufficient to check the first contest
		return contests.iterator().next().isOnBoroughLevel();
	}
}
