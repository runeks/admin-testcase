package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.security.ContextSecurable;
import no.evote.util.EqualsHashCodeUtil;

/**
 * Candidate lists / ballots (an approved candidate list represents a ballot)
 */
@Entity
@Table(name = "ballot", uniqueConstraints = { @UniqueConstraint(columnNames = { "contest_pk", "ballot_id" }),
		@UniqueConstraint(columnNames = { "contest_pk", "display_order" }) })
@AttributeOverride(name = "pk", column = @Column(name = "ballot_pk"))
@NamedQueries({
		@NamedQuery(name = "Ballot.findByContest", query = "select b from Ballot b where b.contest.pk = :pk ORDER BY b.displayOrder ASC"),
		@NamedQuery(
				name = "Ballot.findApprovedByContest",
				query = "select b from Ballot b where b.contest.pk = :pk AND b.approved = true ORDER BY b.displayOrder ASC"),
		@NamedQuery(name = "Ballot.CountByContest", query = "select count(b) from Ballot b where b.contest.pk = :pk"),
		@NamedQuery(name = "Ballot.findByContestAndId", query = "select b from Ballot b where b.contest.pk = :pk AND b.id = :id"),
		@NamedQuery(name = "Ballot.findPkByContestAndId", query = "select b.pk from Ballot b where b.contest.pk = :pk AND b.id = :id"),
		@NamedQuery(name = "findByContestAndOrder", query = "select b from Ballot b where b.contest.pk = :pk AND b.displayOrder = :order") })
public class Ballot extends VersionedEntity implements java.io.Serializable, ContextSecurable, Comparable<Ballot> {

	private Locale locale;
	private BallotStatus ballotStatus;
	private Contest contest;
	private String id;
	private Integer displayOrder;
	private boolean approved;
	private Affiliation affiliation;

	public Ballot() {
		super();
	}

	public Ballot(final Contest contest, final String id, final boolean approved) {
		super();
		this.contest = contest;
		this.id = id;
		this.approved = approved;
	}

	public Ballot(Ballot ballot) {
		super();
		this.contest = ballot.getContest();
		this.id = ballot.getId();
		this.locale = ballot.getLocale();
		this.approved = ballot.isApproved();
		this.ballotStatus = ballot.getBallotStatus();
		this.displayOrder = ballot.getDisplayOrder();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locale_pk", nullable = false)
	public Locale getLocale() {
		return this.locale;
	}

	public void setLocale(final Locale locale) {
		this.locale = locale;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ballot_status_pk", nullable = false)
	public BallotStatus getBallotStatus() {
		return this.ballotStatus;
	}

	public void setBallotStatus(final BallotStatus ballotStatus) {
		this.ballotStatus = ballotStatus;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "contest_pk", nullable = false)
	public Contest getContest() {
		return this.contest;
	}

	public void setContest(final Contest contest) {
		this.contest = contest;
	}

	@Column(name = "ballot_id", nullable = false, length = 10)
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "display_order")
	public Integer getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(final Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Column(name = "approved", nullable = false)
	public boolean isApproved() {
		return this.approved;
	}

	public void setApproved(final boolean approved) {
		this.approved = approved;
	}

	@OneToOne(mappedBy = "ballot")
	public Affiliation getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(final Affiliation affiliation) {
		this.affiliation = affiliation;
	}

	@Override
	public boolean equals(final Object other) {
		return EqualsHashCodeUtil.genericEquals(this, other);
	}

	@Override
	public int hashCode() {
		return EqualsHashCodeUtil.genericHashCode(this);
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		return null;
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		if (level == ElectionLevelEnum.CONTEST) {
			return contest.getPk();
		}
		return null;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return id;
	}

	@Override
	public int compareTo(final Ballot other) {
		return getDisplayOrder().compareTo(other.getDisplayOrder());
	}

	public String partyName() {
		Affiliation affiliation = getAffiliation();
		return affiliation != null ? affiliation.partyName() : null;
	}
}
