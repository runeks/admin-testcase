package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import no.evote.logging.AuditLogUtil;

/**
 * Reporting unit types per node in the election hierarchy
 */
@Entity
@Table(name = "mv_election_reporting_units", uniqueConstraints = @UniqueConstraint(columnNames = { "mv_election_pk", "reporting_unit_type_pk" }))
@AttributeOverride(name = "pk", column = @Column(name = "mv_election_reporting_units_pk"))
public class MvElectionReportingUnits extends VersionedEntity implements java.io.Serializable {

	private ReportingUnitType reportingUnitType;
	private MvElection mvElection;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reporting_unit_type_pk", nullable = false)
	public ReportingUnitType getReportingUnitType() {
		return this.reportingUnitType;
	}

	public void setReportingUnitType(final ReportingUnitType reportingUnitType) {
		this.reportingUnitType = reportingUnitType;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mv_election_pk", nullable = false)
	public MvElection getMvElection() {
		return this.mvElection;
	}

	public void setMvElection(final MvElection mvElection) {
		this.mvElection = mvElection;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(mvElection, reportingUnitType);
	}
}
