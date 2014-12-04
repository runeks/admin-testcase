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
 * The last assigned proposer_list number, per ballot
 */
@Entity
@Table(name = "proposer_list_number", uniqueConstraints = @UniqueConstraint(columnNames = "ballot_pk"))
@AttributeOverride(name = "pk", column = @Column(name = "proposer_list_number_pk"))
@NamedQueries({ @NamedQuery(name = "ProposerListNumber.countByBallot", query = "SELECT pln.lastProposerListNumber FROM ProposerListNumber pln"
		+ " WHERE pln.ballot.pk = :ballotPk") })
public class ProposerListNumber extends VersionedEntity implements java.io.Serializable {

	private Ballot ballot;
	private int lastProposerListNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ballot_pk", unique = true, nullable = false)
	public Ballot getBallot() {
		return this.ballot;
	}

	public void setBallot(final Ballot ballot) {
		this.ballot = ballot;
	}

	@Column(name = "last_proposer_list_number", nullable = false)
	public int getLastProposerListNumber() {
		return this.lastProposerListNumber;
	}

	public void setLastProposerListNumber(final int lastProposerListNumber) {
		this.lastProposerListNumber = lastProposerListNumber;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(ballot);
	}
}
