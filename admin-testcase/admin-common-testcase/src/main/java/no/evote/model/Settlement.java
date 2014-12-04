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
 * Settlement for contest
 */
@Entity
@Table(name = "settlement", uniqueConstraints = { @UniqueConstraint(columnNames = { "contest_pk", "final_settlement" }),
		@UniqueConstraint(columnNames = { "contest_pk", "settlement_number" }) })
@AttributeOverride(name = "pk", column = @Column(name = "settlement_pk"))
@NamedQueries({ @NamedQuery(name = "Settlement.findByContest", query = "SELECT s FROM Settlement s WHERE s.contest.pk = :contestPk") })
public class Settlement extends VersionedEntity implements java.io.Serializable {

	private Contest contest;
	private int number;
	private boolean finalSettlement;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contest_pk", nullable = false)
	public Contest getContest() {
		return this.contest;
	}

	public void setContest(final Contest contest) {
		this.contest = contest;
	}

	@Column(name = "settlement_number", nullable = false)
	public int getNumber() {
		return this.number;
	}

	public void setNumber(final int number) {
		this.number = number;
	}

	@Column(name = "final_settlement", nullable = false)
	public boolean isFinalSettlement() {
		return this.finalSettlement;
	}

	public void setFinalSettlement(final boolean finalSettlement) {
		this.finalSettlement = finalSettlement;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(contest, finalSettlement);
	}
}
