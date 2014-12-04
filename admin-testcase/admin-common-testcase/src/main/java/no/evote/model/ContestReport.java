package no.evote.model;

import static java.util.Collections.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.exception.EvoteException;
import no.evote.logging.AuditLogUtil;
import no.evote.security.ContextSecurable;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.counting.model.BallotCountRef;
import no.valg.eva.admin.common.counting.model.CountCategory;
import no.valg.eva.admin.common.counting.model.CountStatus;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents contest protocols with optional counts. The reporting is performed for a contest within the authority of the reporting unit
 */
@Entity
@Table(name = "contest_report", uniqueConstraints = { @UniqueConstraint(columnNames = { "contest_pk", "final_report" }),
		@UniqueConstraint(columnNames = { "reporting_unit_pk", "contest_pk" }) })
@AttributeOverride(name = "pk", column = @Column(name = "contest_report_pk"))
@NamedQueries({
		@NamedQuery(name = "ContestReport.findByReportingUnitContest", query = "SELECT cr FROM ContestReport cr WHERE cr.reportingUnit.pk = :rupk "
				+ "AND cr.contest.pk = :cpk"),
		@NamedQuery(name = "ContestReport.findByContest", query = "SELECT cr FROM ContestReport cr WHERE cr.contest.pk = :cpk"),
		@NamedQuery(name = "ContestReport.findByCountPk", query = "SELECT cr FROM ContestReport cr, VoteCount vc WHERE vc in elements(cr.voteCountSet) and vc.pk = :cpk"),
		@NamedQuery(
				name = "ContestReport.findByBallotCount",
				query = "select cr from ContestReport cr, BallotCount bc, VoteCount vc "
						+ "WHERE bc.pk = :ballotCountPk and bc in elements(vc.ballotCountSet) and vc in elements(cr.voteCountSet)"),
		
		@NamedQuery(name = "ContestReport.findByReportingUnit", query = "SELECT cr FROM ContestReport cr WHERE cr.reportingUnit.pk = :rupk") })
public class ContestReport extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private ReportingUnit reportingUnit;
	private Contest contest;
	private boolean finalReport;
	private int lateValidationCovers;
	private Set<VoteCount> voteCountSet = new HashSet<>();

	public ContestReport() {
		super();
	}

	public ContestReport(final ContestReport contestReport, final ReportingUnit reportingUnit, final Contest contest) {
		super();
		this.reportingUnit = reportingUnit;
		this.contest = contest;
		finalReport = contestReport.isFinalReport();
		lateValidationCovers = contestReport.getLateValidationCovers();
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reporting_unit_pk", nullable = false)
	public ReportingUnit getReportingUnit() {
		return reportingUnit;
	}

	public void setReportingUnit(final ReportingUnit reportingUnit) {
		this.reportingUnit = reportingUnit;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "contest_pk", nullable = false)
	public Contest getContest() {
		return contest;
	}

	public void setContest(final Contest contest) {
		this.contest = contest;
	}

	@Column(name = "final_report", nullable = false)
	public boolean isFinalReport() {
		return finalReport;
	}

	public void setFinalReport(final boolean finalReport) {
		this.finalReport = finalReport;
	}

	@NotNull
	@Min(0)
	@Column(name = "late_validation_covers", nullable = false)
	public int getLateValidationCovers() {
		return lateValidationCovers;
	}

	public void setLateValidationCovers(final int lateValidationCovers) {
		this.lateValidationCovers = lateValidationCovers;
	}

	@OneToMany(mappedBy = "contestReport", cascade = CascadeType.ALL)
	public Set<VoteCount> getVoteCountSet() {
		return voteCountSet;
	}

	public void setVoteCountSet(final Set<VoteCount> voteCountSet) {
		this.voteCountSet = voteCountSet;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		return null;
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
		return AuditLogUtil.generateId(contest, finalReport);
	}

	/**
	 * Find first vote count by area and count qualifier
	 * @return first vote count if it exists, null otherwise
	 */
	@Transient
	public VoteCount findFirstVoteCountByMvAreaCountQualifierAndCategory(
			final long mvAreaPk,
			final no.valg.eva.admin.common.counting.model.CountQualifier countQualifier,
			final CountCategory category) {

		for (final VoteCount voteCount : getVoteCountSet()) {
			if (hasArea(mvAreaPk, voteCount)
					&& hasQualifier(countQualifier, voteCount)
					&& hasCategory(category, voteCount)) {
				return voteCount;
			}
		}
		return null;
	}

	private boolean hasArea(long mvAreaPk, VoteCount voteCount) {
		return voteCount.getMvArea().getPk() == mvAreaPk;
	}

	/**
	 * Finds first vote count by area path, count qualifier and category.
	 * @return first vote count if it exists, null otherwise
	 */
	@Transient
	public VoteCount findFirstVoteCountByAreaPathQualifierAndCategory(
			AreaPath areaPath,
			no.valg.eva.admin.common.counting.model.CountQualifier countQualifier,
			CountCategory category) {

		for (final VoteCount voteCount : getVoteCountSet()) {
			if (hasArea(areaPath, voteCount)
					&& hasQualifier(countQualifier, voteCount)
					&& hasCategory(category, voteCount)) {
				return voteCount;
			}
		}
		return null;
	}

	private boolean hasCategory(CountCategory category, VoteCount voteCount) {
		return voteCount.getVoteCountCategory().getId().equalsIgnoreCase(category.getId());
	}

	private boolean hasQualifier(no.valg.eva.admin.common.counting.model.CountQualifier countQualifier, VoteCount voteCount) {
		return voteCount.getCountQualifier().getId().equalsIgnoreCase(countQualifier.getId());
	}

	private boolean hasArea(AreaPath areaPath, VoteCount voteCount) {
		return AreaPath.from(voteCount.getMvArea().getAreaPath()).equals(areaPath);
	}

	private boolean isApproved(VoteCount voteCount) {
		return voteCount.getCountStatus() == CountStatus.APPROVED || voteCount.getCountStatus() == CountStatus.TO_SETTLEMENT;
	}

	/**
	 * @return first vote count if it exists, null otherwise
	 */
	@Transient
	public VoteCount findFirstVoteCountByCountQualifierAndCategory(
			final no.valg.eva.admin.common.counting.model.CountQualifier countQualifier,
			final CountCategory category) {

		for (final VoteCount voteCount : getVoteCountSet()) {
			if (hasQualifier(countQualifier, voteCount)
					&& hasCategory(category, voteCount)) {
				return voteCount;
			}
		}
		return null;
	}

	@Transient
	public List<VoteCount> findVoteCountsByMvAreaCountQualifierAndCategory(
			long mvAreaPk, no.valg.eva.admin.common.counting.model.CountQualifier countQualifier, CountCategory category) {
	List<VoteCount> result = new ArrayList<>();
		for (VoteCount voteCount : getVoteCountSet()) {
	if (hasArea(mvAreaPk, voteCount)
					&& hasQualifier(countQualifier, voteCount)
					&& hasCategory(category, voteCount)) {
				result.add(voteCount);
			}
		}
		sort(result, new Comparator<VoteCount>() {
			@Override
			public int compare(VoteCount vc1, VoteCount vc2) {
				return vc1.getId().compareTo(vc2.getId());
			}
		});
		return result;
	}
	
	@Transient
	public VoteCount findApprovedVoteCountByMvAreaCountQualifierAndCategory(
			long mvAreaPk, no.valg.eva.admin.common.counting.model.CountQualifier countQualifier, CountCategory category) {
		for (VoteCount voteCount : getVoteCountSet()) {
			if (hasArea(mvAreaPk, voteCount)
					&& hasQualifier(countQualifier, voteCount)
					&& hasCategory(category, voteCount)
					&& isApproved(voteCount)) {
				return voteCount;
			}
		}
		return null;
	}

	@Transient
	public List<VoteCount> findVoteCountsByCountQualifierAndStatus(
			final no.valg.eva.admin.common.counting.model.CountQualifier countQualifier,
			final CountStatus countStatus) {

		List<VoteCount> result = new ArrayList<>();
		for (final VoteCount voteCount : getVoteCountSet()) {
			String countQualifierId = voteCount.getCountQualifierId();
			int voteCountStatusId = voteCount.getVoteCountStatusId();
			if (countQualifierId.equalsIgnoreCase(countQualifier.getId()) && voteCountStatusId == countStatus.getId()) {
				result.add(voteCount);
			}
		}

		return result;
	}

	/**
	 * Find vote count by counting area and id.
	 * 
	 * @param countingArea
	 *            the area, usually a polling district, the count is performed at
	 * @param id
	 *            identifies the count within an area (eg. PVO1), composed of the count qualifier id (eg. P), count category id (eg. VO) and an index (eg. 1)
	 * @return the vote count if found, null otherwise
	 */
	public VoteCount findVoteCountByCountingAreaAndId(MvArea countingArea, String id) {
		for (VoteCount voteCount : getVoteCountSet()) {
			String voteCountId = voteCount.getId();
			MvArea voteCountArea = voteCount.getMvArea();
			if (voteCountId.equals(id) && voteCountArea.equals(countingArea)) {
				return voteCount;
			}
		}
		return null;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		ContestReport rhs = (ContestReport) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(this.reportingUnit, rhs.reportingUnit)
				.append(this.contest, rhs.contest)
				.append(this.finalReport, rhs.finalReport)
				.append(this.lateValidationCovers, rhs.lateValidationCovers)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(reportingUnit)
				.append(contest)
				.append(finalReport)
				.append(lateValidationCovers)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.appendSuper(super.toString())
				.append("reportingUnit", reportingUnit)
				.append("contest", contest)
				.append("finalReport", finalReport)
				.append("lateValidationCovers", lateValidationCovers)
				.toString();
	}

	/**
	 * Finds the number of vote counts that meets the unique criteria
	 * 
	 * @return unique kind count
	 */
	public int uniqueKindCount(final no.evote.model.CountQualifier countQualifier, final VoteCountCategory countCategory, final MvArea countArea) {
		int count = 0;
		for (final VoteCount voteCount : getVoteCountSet()) {
			if (voteCount.belongsTo(countArea) && voteCount.getCountQualifier().equals(countQualifier)
					&& voteCount.getVoteCountCategory().equals(countCategory)) {
				count += 1;
			}
		}

		return count;
	}

	/**
	 * Adds vote count to this and assigns an id based on this contest report.
	 * 
	 * @param voteCount
	 *            count to add
	 */
	public void add(final VoteCount voteCount) {
		// assign id to vote count
		CountQualifier countQualifier = voteCount.getCountQualifier();
		VoteCountCategory voteCountCategory = voteCount.getVoteCountCategory();
		MvArea mvArea = voteCount.getMvArea();
		int uniqueKindCount = uniqueKindCount(countQualifier, voteCountCategory, mvArea);
		voteCount.setId(countQualifier.getId() + voteCountCategory.getId() + (uniqueKindCount + 1));

		voteCountSet.add(voteCount);
		voteCount.setContestReport(this);
	}

	/**
	 * ContestReport (protokoll, "møtebok") is created by/reported by a reporting unit (et styre).
	 * @param reportingUnit
	 *            "styre"
	 * @return true if contest report is reported by the given reporting unit
	 */
	@Transient
	public boolean isReportedBy(final ReportingUnit reportingUnit) {
		return reportingUnit.equals(getReportingUnit());
	}

	@Transient
	public BallotCount getBallotCount(BallotCountRef ballotCountRef) {
		for (VoteCount voteCount : getVoteCountSet()) {
			for (no.evote.model.BallotCount count : voteCount.getBallotCountList()) {
				if (count.getPk() == ballotCountRef.getPk()) {
					return count;
				}
			}
		}
		return null;
	}

	/**
	 * Get vote count in this contest report with given primary key
	 * @param voteCountPk
	 */
	public VoteCount getVoteCount(Long voteCountPk) {
		for (VoteCount voteCount : getVoteCountSet()) {
			if (voteCount.getPk().equals(voteCountPk)) {
				return voteCount;
			}
		}
		throw new EvoteException("Could not find final count " + voteCountPk);
	}
}
