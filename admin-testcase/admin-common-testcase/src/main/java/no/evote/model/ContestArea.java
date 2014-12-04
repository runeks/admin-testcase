package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import no.evote.logging.AuditLogUtil;
import no.evote.constants.AreaLevelEnum;
import no.evote.security.ContextSecurableDynamicArea;
import no.evote.constants.ElectionLevelEnum;

/**
 * One or more areas contained in the contest (Norwegian "valgkrets")
 */
@Entity
@Table(name = "contest_area", uniqueConstraints = @UniqueConstraint(columnNames = { "contest_pk", "mv_area_pk" }))
@AttributeOverride(name = "pk", column = @Column(name = "contest_area_pk"))
@NamedQueries({
		@NamedQuery(name = "ContestArea.findContestAreasForContest", query = "SELECT ca FROM ContestArea ca LEFT JOIN ca.mvArea AS ma "
				+ "WHERE ca.contest.pk = :contestPk ORDER BY ma.areaPath"),
		@NamedQuery(
				name = "ContestArea.findAreaPathsForContests",
				query = "SELECT ca.contest.pk, ca.mvArea.areaPath FROM ContestArea ca WHERE ca.contest.pk IN (:contestPks)"),
		@NamedQuery(name = "ContestArea.getAreaLevelByContest", query = "SELECT ca.mvArea.areaLevel FROM ContestArea ca WHERE ca.contest.pk = :contestPk") })
public class ContestArea extends VersionedEntity implements java.io.Serializable, ContextSecurableDynamicArea {

	private MvArea mvArea;
	private Contest contest;
	private boolean parentArea;
	private boolean childArea;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mv_area_pk", nullable = false)
	public MvArea getMvArea() {
		return this.mvArea;
	}

	public void setMvArea(final MvArea mvArea) {
		this.mvArea = mvArea;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contest_pk", nullable = false)
	public Contest getContest() {
		return this.contest;
	}

	public void setContest(final Contest contest) {
		this.contest = contest;
	}

	@Column(name = "parent_area", nullable = false)
	public boolean isParentArea() {
		return this.parentArea;
	}

	public void setParentArea(final boolean parentArea) {
		this.parentArea = parentArea;
	}

	@Column(name = "child_area", nullable = false)
	public boolean isChildArea() {
		return this.childArea;
	}

	public void setChildArea(final boolean childArea) {
		this.childArea = childArea;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		return mvArea.getPkForLevel(getActualAreaLevel().getLevel());
	}

	@Override
	@Transient
	public AreaLevelEnum getActualAreaLevel() {
		return AreaLevelEnum.getLevel(mvArea.getAreaLevel());
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		if (level.equals(ElectionLevelEnum.CONTEST)) {
			return contest.getPk();
		}
		return null;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(contest, mvArea);
	}
}
