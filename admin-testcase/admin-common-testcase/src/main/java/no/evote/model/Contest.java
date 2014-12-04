package no.evote.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
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
import no.evote.validation.StringNotNullEmptyOrBlanks;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.ElectionPath;

import static no.evote.constants.AreaLevelEnum.BOROUGH;
import static no.evote.constants.AreaLevelEnum.MUNICIPALITY;

/**
 * Entity representing Contest ("Konkurranse, delvalg"). There are normally several Contests per Election, e.g. "Oslo" or "Sør-Norge valgkrets".
 */
@Entity
@Table(name = "contest", uniqueConstraints = @UniqueConstraint(columnNames = { "election_pk", "contest_id" }))
@AttributeOverride(name = "pk", column = @Column(name = "contest_pk"))
@NamedQueries({
		@NamedQuery(name = "Contest.findById", query = "SELECT c FROM Contest c WHERE c.election.pk = :electionPk AND c.id = :id"),
		@NamedQuery(name = "Contest.findByElectionEventAndStatus", query = "select c from Contest c, ContestArea ca, "
				+ "MvArea m WHERE ca.contest.pk = c.pk and ca.mvArea.pk = m.pk and m.electionEvent.pk = "
				+ ":electionEventPk and c.contestStatus.id = :contestStatusId"),
		@NamedQuery(name = "Contest.countByElection", query = "select count(c) from Contest c where c.election.pk = :electionPk"),
		@NamedQuery(name = "Contest.findByElection", query = "select c from Contest c where c.election.pk = :electionPk order by c.name") })
@NamedNativeQueries({
		@NamedNativeQuery(name = "Contest.findBoroughContestsInMunicipality",
				query = "SELECT c.* FROM contest c "
						+ "JOIN contest_area ca ON ca.contest_pk = c.contest_pk "
						+ "JOIN mv_area mva ON ca.mv_area_pk = mva.mv_area_pk "
						+ "JOIN municipality m ON mva.municipality_pk = m.municipality_pk "
						+ "WHERE m.municipality_pk = ?1 AND mva.area_level = 4",
				resultClass = Contest.class) })
@SuppressWarnings({ "PMD.AvoidDuplicateLiterals" })
public class Contest extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private Locale locale;
	private ContestStatus contestStatus;
	private Election election;
	private String id;
	private String name;
	private String referendumQuestionText;
	private Integer minProposersOldParty;
	private Integer minProposersNewParty;
	private Integer minCandidates;
	private Integer maxCandidates;
	private Integer maxVotes;
	private Integer minVotes;
	private Integer maxWriteIn;
	private Integer maxRenumber;
	private int numberOfPositions;
	private Integer maxCandidateNameLength;
	private Date endDateOfBirth;
	private Boolean penultimateRecount;
	private Set<ContestReport> contestReports = new HashSet<>();
	private Set<Ballot> ballots = new HashSet<>();
	private Set<ContestArea> contestAreaSet = new HashSet<>();

	public Contest() {
		super();
	}

	public Contest(Contest contest) {
		super();
		this.locale = contest.getLocale();
		this.contestStatus = contest.getContestStatus();
		this.election = contest.getElection();
		this.endDateOfBirth = contest.getEndDateOfBirth();
		this.id = contest.getId();
		this.referendumQuestionText = contest.getReferendumQuestionText();
		this.maxCandidates = contest.getMaxCandidates();
		this.maxCandidateNameLength = contest.getMaxCandidateNameLength();
		this.maxVotes = contest.getMaxVotes();
		this.maxWriteIn = contest.getMaxWriteIn();
		this.minCandidates = contest.getMinCandidates();
		this.minProposersOldParty = contest.getMinProposersOldParty();
		this.minProposersNewParty = contest.getMinProposersNewParty();
		this.maxRenumber = contest.getMaxRenumber();
		this.minVotes = contest.getMinVotes();
		this.numberOfPositions = contest.getNumberOfPositions();
		this.penultimateRecount = contest.getPenultimateRecount();
		this.setName(contest.getName());
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "locale_pk", nullable = false)
	@NotNull
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(final Locale locale) {
		this.locale = locale;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "contest_status_pk", nullable = false)
	public ContestStatus getContestStatus() {
		return contestStatus;
	}

	public void setContestStatus(final ContestStatus contestStatus) {
		this.contestStatus = contestStatus;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "election_pk", nullable = false)
	@NotNull
	public Election getElection() {
		return election;
	}

	public void setElection(final Election election) {
		this.election = election;
	}

	@Column(name = "contest_id", nullable = false, length = 6)
	@ID(size = 6)
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "contest_name", nullable = false, length = 100)
	@StringNotNullEmptyOrBlanks
	@Size(max = 100)
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "referendum_question_text", length = 150)
	@Size(max = 150)
	public String getReferendumQuestionText() {
		return referendumQuestionText;
	}

	public void setReferendumQuestionText(final String referendumQuestionText) {
		this.referendumQuestionText = referendumQuestionText;
	}

	@Column(name = "min_proposers_old_party")
	public Integer getMinProposersOldParty() {
		return minProposersOldParty;
	}

	public void setMinProposersOldParty(final Integer minProposersOldParty) {
		this.minProposersOldParty = minProposersOldParty;
	}

	@Column(name = "min_proposers_new_party")
	public Integer getMinProposersNewParty() {
		return minProposersNewParty;
	}

	public void setMinProposersNewParty(final Integer minProposersNewParty) {
		this.minProposersNewParty = minProposersNewParty;
	}

	@Column(name = "min_candidates")
	@Min(0)
	@Max(9999)
	public Integer getMinCandidates() {
		return minCandidates;
	}

	public void setMinCandidates(final Integer minCandidates) {
		this.minCandidates = minCandidates;
	}

	@Column(name = "max_candidates")
	@Min(0)
	@Max(9999)
	public Integer getMaxCandidates() {
		return maxCandidates;
	}

	public void setMaxCandidates(final Integer maxCandidates) {
		this.maxCandidates = maxCandidates;
	}

	@Column(name = "max_votes")
	public Integer getMaxVotes() {
		return maxVotes;
	}

	public void setMaxVotes(final Integer maxVotes) {
		this.maxVotes = maxVotes;
	}

	@Column(name = "min_votes")
	public Integer getMinVotes() {
		return minVotes;
	}

	public void setMinVotes(final Integer minVotes) {
		this.minVotes = minVotes;
	}

	@Column(name = "max_write_in")
	@Min(0)
	@Max(9999)
	public Integer getMaxWriteIn() {
		return maxWriteIn;
	}

	public void setMaxWriteIn(final Integer maxWriteIn) {
		this.maxWriteIn = maxWriteIn;
	}

	@Column(name = "max_renumber")
	@Min(0)
	@Max(9999)
	public Integer getMaxRenumber() {
		return maxRenumber;
	}

	public void setMaxRenumber(final Integer maxRenumber) {
		this.maxRenumber = maxRenumber;
	}

	@Column(name = "number_of_positions", nullable = false)
	@Min(0)
	@Max(9999)
	public int getNumberOfPositions() {
		return numberOfPositions;
	}

	public void setNumberOfPositions(final int numberOfPositions) {
		this.numberOfPositions = numberOfPositions;
	}

	@Column(name = "max_candidate_name_length")
	public Integer getMaxCandidateNameLength() {
		return maxCandidateNameLength;
	}

	public void setMaxCandidateNameLength(final Integer maxCandidateNameLength) {
		this.maxCandidateNameLength = maxCandidateNameLength;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date_of_birth", length = 13)
	@Past
	public Date getEndDateOfBirth() {
		Date returnDate = null;
		if (endDateOfBirth != null) {
			returnDate = new Date(endDateOfBirth.getTime());
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

	@OneToMany(mappedBy = "contest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public Set<ContestReport> getContestReports() {
		return contestReports;
	}

	public void setContestReports(final Set<ContestReport> contestReports) {
		this.contestReports = contestReports;
	}

	@OneToMany(mappedBy = "contest", fetch = FetchType.LAZY)
	public Set<Ballot> getBallots() {
		return ballots;
	}

	public void setBallots(Set<Ballot> ballots) {
		this.ballots = ballots;
	}

	@Transient
	public Set<Ballot> getSortedApprovedBallots() {
		Set<Ballot> approvedBallots = new TreeSet<>();
		for (Ballot ballot : getBallots()) {
			if (ballot.isApproved()) {
				approvedBallots.add(ballot);
			}
		}
		return approvedBallots;
	}

	@OneToMany(mappedBy = "contest", fetch = FetchType.LAZY)
	public Set<ContestArea> getContestAreaSet() {
		return contestAreaSet;
	}

	public void setContestAreaSet(Set<ContestArea> contestAreaSet) {
		this.contestAreaSet = contestAreaSet;
	}

	@Transient
	public List<ContestArea> getContestAreaList() {
		Set<ContestArea> contestAreaSet = getContestAreaSet();
		if (contestAreaSet != null) {
			return new ArrayList<>(contestAreaSet);
		}
		return null;
	}

	@Transient
	public boolean isOnBoroughLevel() {
		List<ContestArea> contestAreaList = getContestAreaList();
		if (contestAreaList == null || contestAreaList.size() != 1) {
			return false;
		}
		ContestArea contestArea = contestAreaList.get(0);
		return contestArea.getMvArea().getActualAreaLevel() == AreaLevelEnum.BOROUGH;
	}

	public void addContestReport(ContestReport aContestReport) {
		getContestReports().add(aContestReport);
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		return null;
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		switch (level) {
		case ELECTION:
			return election.getPk();
		case CONTEST:
			return getPk();
		default:
			return null;
		}
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(election, id);
	}

	@Column(name = "penultimate_recount")
	public Boolean getPenultimateRecount() {
		return penultimateRecount;
	}

	public void setPenultimateRecount(final Boolean penultimateRecount) {
		this.penultimateRecount = penultimateRecount;
	}

	/**
	 * Check if municipality shall count two times (both preliminary and final) (or just preliminary) before the last reporting unit recounts the final. In sami
	 * election the municipality only counts preliminary before opptellingsvalgstyret counts final.
	 * 
	 * @return true if municipality must count both preliminary and final.
	 */
	@Transient
	public boolean isContestOrElectionPenultimateRecount() {
		if (this.getPenultimateRecount() != null) {
			return this.getPenultimateRecount();
		} else {
			return this.getElection().isPenultimateRecount();
		}
	}

	@Override
	public String toString() {
		return id + " " + name;
	}

	/**
	 * @return contest report for a reporting unit
	 */
	public ContestReport contestReportForReportingUnit(final ReportingUnit reportingUnit) {
		for (ContestReport contestReport : getContestReports()) {
			if (contestReport.isReportedBy(reportingUnit)) {
				return contestReport;
			}
		}
		return null;
	}

	public boolean hasContestAreaForAreaPath(AreaPath areaPath) {
		Set<ContestArea> contestAreaSet = getContestAreaSet();
		for (ContestArea contestArea : contestAreaSet) {
			AreaPath contestAreaPath = AreaPath.from(contestArea.getMvArea().getAreaPath());
			if (contestAreaPath.contains(areaPath)) {
				return true;
			}
			if (areaPath.getLevel() == MUNICIPALITY && contestAreaPath.getLevel() == BOROUGH && areaPath.contains(contestAreaPath)) {
				return true;
			}
		}
		return false;
	}

	public ElectionPath electionPath() {
		return getElection().electionPath().add(getId());
	}

	@Transient
	public boolean isReferendum() {
		return getElection().isReferendum();
	}

	@Transient
	public boolean isSingleArea() {
		return election.isSingleArea();
	}
}
