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

/**
 * Status on reports sent to SSB
 */
@Entity
@Table(name = "ssb_report", schema = "admin", uniqueConstraints = @UniqueConstraint(
		columnNames = { "contest_pk", "form_id", "municipality_pk", "column_number" }))
@AttributeOverride(name = "pk", column = @Column(name = "ssb_report_pk"))
@NamedQueries({
		@NamedQuery(
				name = "SsbReport.findBySsbEntityCode",
				query = "SELECT sr FROM SsbReport sr WHERE sr.contest.pk = :contestPk "
						+ "AND sr.formId = :formId AND (sr.municipality.pk = :municipalityPk OR sr.municipality IS NULL) "
                        + "AND sr.pollingDistrict IS NULL ORDER BY sr.columnNumber"),
		@NamedQuery(
				name = "SsbReport.findSsbReportForPollingDistrictByMunicipality",
				query = "SELECT sr FROM SsbReport sr WHERE sr.contest.pk = :contestPk "
						+ "AND sr.municipality.pk = :municipalityPk AND sr.pollingDistrict IS NOT NULL AND sr.columnNumber = :columnNumber "
                        + "ORDER BY sr.pollingDistrict.id") })
public class SsbReport extends VersionedEntity implements java.io.Serializable {

	public enum ColumnNumberEnum {
		NONE(-1, false, false, null), EARLY_VOTING_PRELIMINARY(1, true, false, "ff"), ELECTION_DAY_VOTING_PRELIMINARY(2, false, false, "vf"),
        EARLY_VOTING_FINAL(3, true, true, "fe"), ELECTION_DAY_VOTING_FINAL(
				4, false, true, "ve");

		private final int id;
		private final boolean early;
		private final boolean finalCount;
		private final String countQualifier;

		private ColumnNumberEnum(final int id, final boolean early, final boolean finalCount, final String countQualifier) {
			this.id = id;
			this.early = early;
			this.finalCount = finalCount;
			this.countQualifier = countQualifier;
		}

		public int getId() {
			return id;
		}

		public boolean isEarly() {
			return early;
		}

		public boolean isFinalCount() {
			return finalCount;
		}

		public static ColumnNumberEnum getById(final int id) {
			// CSOFF: MagicNumber
			switch (id) {
			case 1:
				return ColumnNumberEnum.EARLY_VOTING_PRELIMINARY;
			case 2:
				return ColumnNumberEnum.ELECTION_DAY_VOTING_PRELIMINARY;
			case 3:
				return ColumnNumberEnum.EARLY_VOTING_FINAL;
			case 4:
				return ColumnNumberEnum.ELECTION_DAY_VOTING_FINAL;
			default:
				return ColumnNumberEnum.NONE;
			}
			// CSON: MagicNumber
		}

		public String getCountQualifier() {
			return countQualifier;
		}
	}

	private Municipality municipality;
	private PollingDistrict pollingDistrict;
	private Contest contest;
	private String formId;
	private Integer columnNumber;
	private String status;
	private String referenceNumber;
	private String instanceUrl;

	private boolean readyForReport;

	public static final String NORMAL = "Normal";
	public static final String CRITICAL = "Critical";
	public static final String NOTSENT = "NotSent";
	public static final String ERROR = "Error";
	public static final String SENDING = "Sending";

	public SsbReport() {
		// Default empty constructor
	}

	public SsbReport(final Contest contest, final Municipality municipality, final String formId, final ColumnNumberEnum columnNumber) {
		this.contest = contest;
		this.municipality = municipality;

		// Set columnNumber only when municipality i set. (Constraints)
		// column number is set to 1 if only early voting, 2 for all votes (early and election day)
		if (municipality != null) {
			this.columnNumber = columnNumber != null ? columnNumber.getId() : null;
		}
		this.formId = formId;
		status = NOTSENT;
	}

	@Override
	public String toString() {
		return String.format("SSBReport: municipality %s contest %s formId %s columnNumber %s status %s refNr  %s url %s", (municipality != null ? municipality
				.getPk().toString() : ""), contest.getPk(), formId, columnNumber != null ? columnNumber.toString() : "", status, referenceNumber, instanceUrl);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "municipality_pk")
	public Municipality getMunicipality() {
		return municipality;
	}

	public void setMunicipality(final Municipality municipality) {
		this.municipality = municipality;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "polling_district_pk")
	public PollingDistrict getPollingDistrict() {
		return pollingDistrict;
	}

	public void setPollingDistrict(final PollingDistrict pollingDistrict) {
		this.pollingDistrict = pollingDistrict;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contest_pk", nullable = false)
	public Contest getContest() {
		return contest;
	}

	public void setContest(final Contest contest) {
		this.contest = contest;
	}

	@Column(name = "form_id", nullable = false, length = 4)
	public String getFormId() {
		return formId;
	}

	public void setFormId(final String formId) {
		this.formId = formId;
	}

	@Column(name = "column_number")
	public Integer getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(final Integer columnNumber) {
		this.columnNumber = columnNumber;
	}

	@Column(name = "status", nullable = false, length = 8)
	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	@Column(name = "reference_number", length = 10)
	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(final String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	@Column(name = "instance_url", length = 500)
	public String getInstanceUrl() {
		return instanceUrl;
	}

	public void setInstanceUrl(final String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}

	@Transient
	public boolean isReadyForReport() {
		return readyForReport;
	}

	public void setReadyForReport(final boolean readyForReport) {
		this.readyForReport = readyForReport;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(contest, formId, municipality, columnNumber);
	}

	@Transient
	public boolean isEarlyVoting() {
		return ColumnNumberEnum.getById(columnNumber).isEarly();
	}

	@Transient
	public boolean isElectionDayVoting() {
		return !ColumnNumberEnum.getById(columnNumber).isEarly();
	}

	@Transient
	public String countQualifier() {
		return ColumnNumberEnum.getById(columnNumber).getCountQualifier();
	}
}
