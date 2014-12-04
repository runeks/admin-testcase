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
 * Settlement: Count of referendum option votes
 */
@Entity
@Table(name = "referendum_opt_vote_count", uniqueConstraints = @UniqueConstraint(columnNames = { "settlement_pk", "referendum_option_pk" }))
@AttributeOverride(name = "pk", column = @Column(name = "referendum_opt_vote_count_pk"))
@NamedQueries({ @NamedQuery(name = "ReferendumOptVoteCount.findBySettlement", query = "SELECT rovc FROM ReferendumOptVoteCount rovc "
		+ "WHERE rovc.settlement.pk = :settlementPk ORDER BY rovc.referendumOption.displayOrder") })
public class ReferendumOptVoteCount extends VersionedEntity implements java.io.Serializable {

	private Settlement settlement;
	private ReferendumOption referendumOption;
	private int votes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "settlement_pk", nullable = false)
	public Settlement getSettlement() {
		return this.settlement;
	}

	public void setSettlement(final Settlement settlement) {
		this.settlement = settlement;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "referendum_option_pk", nullable = false)
	public ReferendumOption getReferendumOption() {
		return this.referendumOption;
	}

	public void setReferendumOption(final ReferendumOption referendumOption) {
		this.referendumOption = referendumOption;
	}

	@Column(name = "votes", nullable = false)
	public int getVotes() {
		return this.votes;
	}

	public void setVotes(final int votes) {
		this.votes = votes;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(settlement, referendumOption);
	}
}
