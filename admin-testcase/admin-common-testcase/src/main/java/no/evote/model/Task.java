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

import no.evote.constants.SQLConstants;
import no.evote.logging.AuditLogUtil;

/**
 * Task list
 */
@Entity
@Table(name = "task", uniqueConstraints = @UniqueConstraint(columnNames = { SQLConstants.ELECTION_EVENT_PK, "task_number" }))
@AttributeOverride(name = "pk", column = @Column(name = "task_pk"))
@NamedQueries({ @NamedQuery(name = "Task.deleteMvElectionPk", query = "delete from Task t where t.mvElection.pk = :mvElectionPk"),
		@NamedQuery(name = "Task.deleteElectionEventPk", query = "delete from Task t where t.electionEvent.pk = :electionEventPk"),
		@NamedQuery(name = "Task.deletePartyPk", query = "delete from Task t where t.party.pk = :partyPk"),
		@NamedQuery(name = "Task.deleteTaskForPathAndEntity", query = "delete from Task t where t.access.pk = :pathPk and t.entityPk = :entityPk") })
public class Task extends VersionedEntity implements java.io.Serializable {

	private MvArea mvArea;
	private ElectionEvent electionEvent;
	private Access access;
	private Party party;
	private MvElection mvElection;
	private int number;
	private int entityPk;
	private boolean operatorsNotified;
	private boolean resolved;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mv_area_pk")
	public MvArea getMvArea() {
		return this.mvArea;
	}

	public void setMvArea(final MvArea mvArea) {
		this.mvArea = mvArea;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = SQLConstants.ELECTION_EVENT_PK, nullable = false)
	public ElectionEvent getElectionEvent() {
		return this.electionEvent;
	}

	public void setElectionEvent(final ElectionEvent electionEvent) {
		this.electionEvent = electionEvent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_access_pk", nullable = false)
	public Access getAccess() {
		return this.access;
	}

	public void setAccess(final Access access) {
		this.access = access;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "party_pk")
	public Party getParty() {
		return this.party;
	}

	public void setParty(final Party party) {
		this.party = party;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mv_election_pk")
	public MvElection getMvElection() {
		return this.mvElection;
	}

	public void setMvElection(final MvElection mvElection) {
		this.mvElection = mvElection;
	}

	@Column(name = "task_number", nullable = false)
	public int getNumber() {
		return this.number;
	}

	public void setNumber(final int number) {
		this.number = number;
	}

	@Column(name = "task_entity_pk", nullable = false)
	public int getEntityPk() {
		return this.entityPk;
	}

	public void setEntityPk(final int entityPk) {
		this.entityPk = entityPk;
	}

	@Column(name = "operators_notified", nullable = false)
	public boolean isOperatorsNotified() {
		return this.operatorsNotified;
	}

	public void setOperatorsNotified(final boolean operatorsNotified) {
		this.operatorsNotified = operatorsNotified;
	}

	@Column(name = "resolved", nullable = false)
	public boolean isResolved() {
		return this.resolved;
	}

	public void setResolved(final boolean resolved) {
		this.resolved = resolved;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(electionEvent, number);
	}
}
