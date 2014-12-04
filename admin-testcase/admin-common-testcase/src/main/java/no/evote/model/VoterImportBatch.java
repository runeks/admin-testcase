package no.evote.model;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import no.evote.constants.SQLConstants;
import no.evote.logging.AuditLogUtil;
import no.evote.constants.AreaLevelEnum;
import no.evote.security.ContextSecurable;
import no.evote.constants.ElectionLevelEnum;

/**
 * Information on the latest voter import batch
 */
@Entity
@Table(name = "voter_import_batch", uniqueConstraints = @UniqueConstraint(columnNames = SQLConstants.ELECTION_EVENT_PK))
@AttributeOverride(name = "pk", column = @Column(name = "voter_import_batch_pk"))
public class VoterImportBatch extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private ElectionEvent electionEvent;
	private int lastImportBatchNumber;
	private Date lastImportStart;
	private Date lastImportEnd;
	private int lastImportRecordsTotal;
	private int lastImportRecordsInsert;
	private int lastImportRecordsUpdate;
	private int lastImportRecordsDelete;
	private int lastImportRecordsSkip;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = SQLConstants.ELECTION_EVENT_PK, nullable = false)
	public ElectionEvent getElectionEvent() {
		return this.electionEvent;
	}

	public void setElectionEvent(final ElectionEvent electionEvent) {
		this.electionEvent = electionEvent;
	}

	@Column(name = "last_import_batch_number", nullable = false)
	public int getLastImportBatchNumber() {
		return this.lastImportBatchNumber;
	}

	public void setLastImportBatchNumber(final int lastImportBatchNumber) {
		this.lastImportBatchNumber = lastImportBatchNumber;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_import_start", nullable = false, length = 29)
	public Date getLastImportStart() {
		Date returnDate = null;
		if (this.lastImportStart != null) {
			returnDate = new Date(this.lastImportStart.getTime());
		}
		return returnDate;
	}

	public void setLastImportStart(final Date lastImportStart) {
		if (lastImportStart != null) {
			this.lastImportStart = new Date(lastImportStart.getTime());
		} else {
			this.lastImportStart = null;
		}
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_import_end", nullable = false, length = 29)
	public Date getLastImportEnd() {
		Date returnDate = null;
		if (this.lastImportEnd != null) {
			returnDate = new Date(this.lastImportEnd.getTime());
		}
		return returnDate;
	}

	public void setLastImportEnd(final Date lastImportEnd) {
		if (lastImportEnd != null) {
			this.lastImportEnd = new Date(lastImportEnd.getTime());
		} else {
			this.lastImportEnd = null;
		}
	}

	@Column(name = "last_import_records_total", nullable = false)
	public int getLastImportRecordsTotal() {
		return this.lastImportRecordsTotal;
	}

	public void setLastImportRecordsTotal(final int lastImportRecordsTotal) {
		this.lastImportRecordsTotal = lastImportRecordsTotal;
	}

	@Column(name = "last_import_records_insert", nullable = false)
	public int getLastImportRecordsInsert() {
		return this.lastImportRecordsInsert;
	}

	public void setLastImportRecordsInsert(final int lastImportRecordsInsert) {
		this.lastImportRecordsInsert = lastImportRecordsInsert;
	}

	@Column(name = "last_import_records_update", nullable = false)
	public int getLastImportRecordsUpdate() {
		return this.lastImportRecordsUpdate;
	}

	public void setLastImportRecordsUpdate(final int lastImportRecordsUpdate) {
		this.lastImportRecordsUpdate = lastImportRecordsUpdate;
	}

	@Column(name = "last_import_records_delete", nullable = false)
	public int getLastImportRecordsDelete() {
		return this.lastImportRecordsDelete;
	}

	public void setLastImportRecordsDelete(final int lastImportRecordsDelete) {
		this.lastImportRecordsDelete = lastImportRecordsDelete;
	}

	@Column(name = "last_import_records_skip", nullable = false)
	public int getLastImportRecordsSkip() {
		return this.lastImportRecordsSkip;
	}

	public void setLastImportRecordsSkip(final int lastImportRecordsSkip) {
		this.lastImportRecordsSkip = lastImportRecordsSkip;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		return null;
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		if (level.equals(ElectionLevelEnum.ELECTION_EVENT)) {
			return electionEvent.getPk();
		}
		return null;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(electionEvent);
	}
}
