package no.evote.model;

import java.math.BigDecimal;

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
 * Settlement: Seats assigned by the modified Sainte-Lagüe method
 */
@Entity
@Table(name = "candidate_seat", uniqueConstraints = { @UniqueConstraint(columnNames = { "settlement_pk", "candidate_pk" }),
		@UniqueConstraint(columnNames = { "settlement_pk", "seat_number" }) })
@AttributeOverride(name = "pk", column = @Column(name = "candidate_seat_pk"))
@NamedQueries({ @NamedQuery(name = "CandidateSeat.findBySettlement", query = "SELECT cs FROM CandidateSeat cs WHERE"
		+ " cs.settlement.pk = :settlementPk ORDER BY cs.seatNumber") })
public class CandidateSeat extends VersionedEntity implements java.io.Serializable {

	private Settlement settlement;
	private Candidate candidate;
	private Affiliation affiliation;
	private int seatNumber;
	private BigDecimal quotient;
	private int dividend;
	private BigDecimal divisor;
	private boolean elected;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "settlement_pk", nullable = false)
	public Settlement getSettlement() {
		return this.settlement;
	}

	public void setSettlement(final Settlement settlement) {
		this.settlement = settlement;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "candidate_pk", nullable = false)
	public Candidate getCandidate() {
		return this.candidate;
	}

	public void setCandidate(final Candidate candidate) {
		this.candidate = candidate;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "affiliation_pk", nullable = false)
	public Affiliation getAffiliation() {
		return this.affiliation;
	}

	public void setAffiliation(final Affiliation affiliation) {
		this.affiliation = affiliation;
	}

	@Column(name = "seat_number", nullable = false)
	public int getSeatNumber() {
		return this.seatNumber;
	}

	public void setSeatNumber(final int seatNumber) {
		this.seatNumber = seatNumber;
	}

	@Column(name = "quotient", nullable = false, precision = 14)
	public BigDecimal getQuotient() {
		return this.quotient;
	}

	public void setQuotient(final BigDecimal quotient) {
		this.quotient = quotient;
	}

	@Column(name = "dividend", nullable = false)
	public int getDividend() {
		return this.dividend;
	}

	public void setDividend(final int dividend) {
		this.dividend = dividend;
	}

	@Column(name = "divisor", nullable = false, precision = 5)
	public BigDecimal getDivisor() {
		return this.divisor;
	}

	public void setDivisor(final BigDecimal divisor) {
		this.divisor = divisor;
	}

	@Column(name = "elected", nullable = false)
	public boolean isElected() {
		return this.elected;
	}

	public void setElected(final boolean elected) {
		this.elected = elected;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(settlement, candidate);
	}
}
