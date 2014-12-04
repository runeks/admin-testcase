package no.evote.model;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
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
import no.evote.constants.SQLConstants;
import no.evote.security.ContextSecurableDynamicElection;
import no.evote.util.EqualsHashCodeUtil;
import no.evote.util.Treeable;
import no.evote.validation.StringNotNullEmptyOrBlanks;

/**
 * Materialized view containing all nodes in the hierarchy formed by the tables election_group, election, and contest, facilitating RBAC access to any level in
 * the hierarchy via a single field/pointer
 */
@Entity
@Table(name = "mv_election", uniqueConstraints = { @UniqueConstraint(columnNames = { SQLConstants.ELECTION_EVENT_PK, "election_group_pk", "election_pk",
		"contest_pk" }) })
@AttributeOverride(name = "pk", column = @Column(name = "mv_election_pk"))
@NamedQueries({
		@NamedQuery(name = "MvElection.findRoot", query = "SELECT mve FROM MvElection mve WHERE mve.electionGroup IS NULL AND "
				+ "mve.electionEvent.pk = :eepk"),
		@NamedQuery(name = "MvElection.findByContest", query = "SELECT mve FROM MvElection mve WHERE mve.contest.pk = :contestPk"),
		@NamedQuery(name = "MvElection.findByPath", query = "SELECT mve FROM MvElection mve WHERE mve.electionPath = :path"),
		@NamedQuery(name = "MvElection.findByContestPk", query = "SELECT mve FROM MvElection mve WHERE mve.contest.pk = :contestPk") })
@NamedNativeQueries({
		@NamedNativeQuery(
				name = MvElection.NAMED_NATIVE_QUERY_FIND_FIRST_BY_PATH_AND_LEVEL,
				query = "SELECT * FROM mv_election mve WHERE text2ltree(mve.election_path)"
						+ " <@ text2ltree(?1) AND mve.election_level = ?2 ORDER BY mve.election_path LIMIT 1",
				resultClass = MvElection.class),
		@NamedNativeQuery(
				name = MvElection.NAMED_NATIVE_QUERY_FIND_BY_PATH_AND_LEVEL,
				query = "SELECT * FROM mv_election mve WHERE text2ltree(mve.election_path)"
						+ " <@ text2ltree(?1) AND mve.election_level = ?2 ORDER BY mve.election_path",
				resultClass = MvElection.class),
		@NamedNativeQuery(name = "MvElection.findElectionsByElectionType", query = "SELECT * FROM admin.mv_election mve LEFT JOIN admin.election e "
				+ "on mve.election_pk = e.election_pk WHERE text2ltree(mve.election_path) <@ text2ltree(?1) "
				+ "AND e.election_type_pk = ?2 AND mve.election_level = 2", resultClass = MvElection.class),
		@NamedNativeQuery(name = MvElection.FIND_BY_PK_AND_LEVEL, query = "select re.* from mv_election re join mv_election oe"
				+ " on (text2ltree(oe.election_path) <@ text2ltree(re.election_path) and oe.election_level = ?1"
				+ " and case when ?1 = 0 then oe.election_event_pk when ?1 = 1 then oe.election_group_pk"
				+ " when ?1 = 2 then oe.election_pk when ?1 = 3 then oe.contest_pk end = ?2)", resultClass = MvElection.class),
		@NamedNativeQuery(
				name = MvElection.FIND_SINGLE_BY_PK_AND_LEVEL,
				query = "select * from mv_election where "
						+ "case when (?1) = 1 then election_group_pk when (?1) = 2 then election_pk when (?1) = 3 then contest_pk end = (?2);",
				resultClass = MvElection.class),
		@NamedNativeQuery(name = MvElection.HAS_ACCESS_TO_PK_ON_LEVEL, query = "select re.* from mv_election re join mv_election oe"
				+ " on (text2ltree(oe.election_path) <@ text2ltree(re.election_path) and oe.election_level = ?1"
				+ " and case when ?1 = 0 then oe.election_event_pk when ?1 = 1 then oe.election_group_pk"
				+ " when ?1 = 2 then oe.election_pk when ?1 = 3 then oe.contest_pk end = ?2) where re.election_path = ?3", resultClass = MvElection.class),
		@NamedNativeQuery(name = MvElection.NAMED_NATIVE_QUERY_FIND_FIRST_PK_BY_ELECTION_PATH_AND_OPERATOR_AREA_PATH,
				query = "select mve.mv_election_pk "
						+ "from mv_election mve "
						+ "join contest_area ca using (contest_pk) "
						+ "join mv_area mva_ca using (mv_area_pk) "
						+ "join mv_area mva on (text2ltree(mva_ca.area_path) @> text2ltree(mva.area_path)) "
						+ "or (mva.area_level = 3 and mva_ca.area_level = 4 and text2ltree(mva_ca.area_path) <@ text2ltree(mva.area_path)) "
						+ "or (mva.area_level = 0 and text2ltree(mva_ca.area_path) <@ text2ltree(mva.area_path))"
						+ "where election_path like ?1 and election_level = 3 and mva.area_path = ?2 limit 1"),
		@NamedNativeQuery(name = MvElection.NAMED_NATIVE_QUERY_FIND_BY_ELECTION_PATH_AND_OPERATOR_AREA_PATH,
				query = "select mve.* "
						+ "from mv_election mve "
						+ "join contest_area ca using (contest_pk) "
						+ "join mv_area mva_ca using (mv_area_pk) "
						+ "join mv_area mva on (text2ltree(mva_ca.area_path) @> text2ltree(mva.area_path)) "
						+ "or (mva.area_level = 3 and mva_ca.area_level = 4 and text2ltree(mva_ca.area_path) <@ text2ltree(mva.area_path)) "
						+ "or (mva.area_level = 0 and text2ltree(mva_ca.area_path) <@ text2ltree(mva.area_path))"
						+ "where election_path like ?1 and election_level = 3 and mva.area_path = ?2", resultClass = MvElection.class) })
public class MvElection extends BaseEntity implements java.io.Serializable, Treeable, ContextSecurableDynamicElection {
	public static final String HAS_ACCESS_TO_PK_ON_LEVEL = "MvElection.hasAccessToPkOnLevel";
	public static final String NAMED_NATIVE_QUERY_FIND_BY_PATH_AND_LEVEL = "MvElection.findByPathAndLevel";
	public static final String NAMED_NATIVE_QUERY_FIND_FIRST_BY_PATH_AND_LEVEL = "MvElection.findFirstByPathAndLevel";
	public static final String FIND_BY_PK_AND_LEVEL = "MvElection.findByPkAndLevel";
	public static final String FIND_SINGLE_BY_PK_AND_LEVEL = "MvElection.findSingleByPathAndLevel";
	public static final String NAMED_NATIVE_QUERY_FIND_FIRST_PK_BY_ELECTION_PATH_AND_OPERATOR_AREA_PATH = "MvElection.findFirstPkByElectionPathAndOperatorAreaPath";
	public static final String NAMED_NATIVE_QUERY_FIND_BY_ELECTION_PATH_AND_OPERATOR_AREA_PATH = "MvElection.findByElectionPathAndOperatorAreaPath";

	private ElectionEvent electionEvent;
	private Contest contest;
	private ElectionGroup electionGroup;
	private Election election;
	private String electionPath;
	private int electionLevel;
	private String electionEventId;
	private String electionGroupId;
	private String electionId;
	private String contestId;
	private String electionEventName;
	private String electionGroupName;
	private String electionName;
	private String contestName;
	private Integer areaLevel;
	private Boolean singleArea;
	private Date electionEndDateOfBirth;
	private Date contestEndDateOfBirth;
	private boolean isReportingUnit;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = SQLConstants.ELECTION_EVENT_PK, nullable = false)
	public ElectionEvent getElectionEvent() {
		return this.electionEvent;
	}

	public void setElectionEvent(final ElectionEvent electionEvent) {
		this.electionEvent = electionEvent;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "contest_pk")
	public Contest getContest() {
		return this.contest;
	}

	public void setContest(final Contest contest) {
		this.contest = contest;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "election_group_pk")
	public ElectionGroup getElectionGroup() {
		return this.electionGroup;
	}

	public void setElectionGroup(final ElectionGroup electionGroup) {
		this.electionGroup = electionGroup;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "election_pk")
	public Election getElection() {
		return this.election;
	}

	public void setElection(final Election election) {
		this.election = election;
	}

	@Column(name = "election_path", nullable = false, length = 19)
	@StringNotNullEmptyOrBlanks
	@Size(max = 19)
	public String getElectionPath() {
		return this.electionPath;
	}

	public void setElectionPath(final String electionPath) {
		this.electionPath = electionPath;
	}

	@Column(name = "election_level", nullable = false)
	public int getElectionLevel() {
		return this.electionLevel;
	}

	public void setElectionLevel(final int electionLevel) {
		this.electionLevel = electionLevel;
	}

	@Column(name = "election_event_id", nullable = false, length = 8)
	public String getElectionEventId() {
		return this.electionEventId;
	}

	public void setElectionEventId(final String electionEventId) {
		this.electionEventId = electionEventId;
	}

	@Column(name = "election_group_id", length = 8)
	public String getElectionGroupId() {
		return this.electionGroupId;
	}

	public void setElectionGroupId(final String electionGroupId) {
		this.electionGroupId = electionGroupId;
	}

	@Column(name = "election_id", length = 8)
	public String getElectionId() {
		return this.electionId;
	}

	public void setElectionId(final String electionId) {
		this.electionId = electionId;
	}

	@Column(name = "contest_id", length = 8)
	public String getContestId() {
		return this.contestId;
	}

	public void setContestId(final String contestId) {
		this.contestId = contestId;
	}

	@Column(name = "election_event_name", nullable = false, length = 100)
	@StringNotNullEmptyOrBlanks
	@Size(max = 100)
	public String getElectionEventName() {
		return this.electionEventName;
	}

	public void setElectionEventName(final String electionEventName) {
		this.electionEventName = electionEventName;
	}

	@Column(name = "election_group_name", length = 100)
	@Size(max = 100)
	public String getElectionGroupName() {
		return this.electionGroupName;
	}

	public void setElectionGroupName(final String electionGroupName) {
		this.electionGroupName = electionGroupName;
	}

	@Column(name = "election_name", length = 100)
	@Size(max = 100)
	public String getElectionName() {
		return this.electionName;
	}

	public void setElectionName(final String electionName) {
		this.electionName = electionName;
	}

	@Column(name = "contest_name", length = 100)
	@Size(max = 100)
	public String getContestName() {
		return this.contestName;
	}

	public void setContestName(final String contestName) {
		this.contestName = contestName;
	}

	@Column(name = "area_level")
	public Integer getAreaLevel() {
		return this.areaLevel;
	}

	public void setAreaLevel(final Integer areaLevel) {
		this.areaLevel = areaLevel;
	}

	@Column(name = "single_area")
	public Boolean getSingleArea() {
		return this.singleArea;
	}

	public void setSingleArea(final Boolean singleArea) {
		this.singleArea = singleArea;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "election_end_date_of_birth", length = 13)
	public Date getElectionEndDateOfBirth() {
		Date returnDate = null;
		if (this.electionEndDateOfBirth != null) {
			returnDate = new Date(this.electionEndDateOfBirth.getTime());
		}
		return returnDate;
	}

	public void setElectionEndDateOfBirth(final Date electionEndDateOfBirth) {
		if (electionEndDateOfBirth != null) {
			this.electionEndDateOfBirth = new Date(electionEndDateOfBirth.getTime());
		} else {
			this.electionEndDateOfBirth = null;
		}
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "contest_end_date_of_birth", length = 13)
	public Date getContestEndDateOfBirth() {
		Date returnDate = null;
		if (this.contestEndDateOfBirth != null) {
			returnDate = new Date(this.contestEndDateOfBirth.getTime());
		}
		return returnDate;
	}

	public void setContestEndDateOfBirth(final Date contestEndDateOfBirth) {
		if (contestEndDateOfBirth != null) {
			this.contestEndDateOfBirth = new Date(contestEndDateOfBirth.getTime());
		} else {
			this.contestEndDateOfBirth = null;
		}
	}

	@Override
	@Transient
	public String getPath() {
		return electionPath;
	}

	@Transient
	public String getElectionLevelString() {
		switch (ElectionLevelEnum.getLevel(electionLevel)) {
		case ELECTION_EVENT:
			return "@election_level[0].name";
		case ELECTION_GROUP:
			return "@election_level[1].name";
		case ELECTION:
			return "@election_level[2].name";
		case CONTEST:
			return "@election_level[3].name";

		default:
			return null;
		}

	}

	@Transient
	public String getElectionLevelId() {
		switch (ElectionLevelEnum.getLevel(electionLevel)) {
		case ELECTION_EVENT:
			return electionEventId;
		case ELECTION_GROUP:
			return electionGroupId;
		case ELECTION:
			return electionId;
		case CONTEST:
			return contestId;
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		switch (ElectionLevelEnum.getLevel(electionLevel)) {
		case ELECTION_EVENT:
			return electionEventName;
		case ELECTION_GROUP:
			return electionGroupName;
		case ELECTION:
			return electionName;
		case CONTEST:
			return contestName;

		default:
			return "";
		}
	}

	@Transient
	public String getNamedPath() {
		StringBuffer path = new StringBuffer();
		if (electionEvent != null) {
			path = new StringBuffer(electionEventName);
		}
		if (electionGroup != null) {
			path = new StringBuffer(electionGroupName);
		}
		if (election != null) {
			path = new StringBuffer(electionName);
		}
		if (contest != null) {
			path.append('.');
			path.append(contestName);
		}

		return path.toString();
	}

	@Override
	public int hashCode() {
		return EqualsHashCodeUtil.genericHashCode(this);
	}

	@Override
	public boolean equals(final Object obj) {
		return EqualsHashCodeUtil.genericEquals(this, obj);
	}

	@Transient
	public boolean isReportingUnit() {
		return isReportingUnit;
	}

	public void setReportingUnit(final boolean isReportingUnit) {
		this.isReportingUnit = isReportingUnit;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		return null;
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		if (level.equals(ElectionLevelEnum.ELECTION_EVENT)) {
			return getElectionEvent().getPk();
		}
		if (level.equals(ElectionLevelEnum.ELECTION_GROUP)) {
			return getElectionGroup().getPk();
		}
		if (level.equals(ElectionLevelEnum.ELECTION)) {
			return getElection().getPk();
		}
		if (level.equals(ElectionLevelEnum.CONTEST)) {
			return getContest().getPk();
		}

		return null;
	}

	@Override
	@Transient
	public ElectionLevelEnum getActualElectionLevel() {
		return ElectionLevelEnum.getLevel(electionLevel);
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return electionPath;
	}
}
