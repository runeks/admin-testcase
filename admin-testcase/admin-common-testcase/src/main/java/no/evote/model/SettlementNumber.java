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
 * The last assigned settlement number for the contest
 */
@Entity
@Table(name = "settlement_number", uniqueConstraints = @UniqueConstraint(columnNames = "contest_pk"))
@AttributeOverride(name = "pk", column = @Column(name = "settlement_number_pk"))
public class SettlementNumber extends VersionedEntity implements java.io.Serializable {

	private Contest contest;
	private int lastSettlementNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contest_pk", unique = true, nullable = false)
	public Contest getContest() {
		return this.contest;
	}

	public void setContest(final Contest contest) {
		this.contest = contest;
	}

	@Column(name = "last_settlement_number", nullable = false)
	public int getLastSettlementNumber() {
		return this.lastSettlementNumber;
	}

	public void setLastSettlementNumber(final int lastSettlementNumber) {
		this.lastSettlementNumber = lastSettlementNumber;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(contest);
	}
}
