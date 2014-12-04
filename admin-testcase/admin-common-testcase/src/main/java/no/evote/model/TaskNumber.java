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
 * The last assigned task number in the election event
 */
@Entity
@Table(name = "task_number", uniqueConstraints = @UniqueConstraint(columnNames = SQLConstants.ELECTION_EVENT_PK))
@AttributeOverride(name = "pk", column = @Column(name = "task_number_pk"))
@NamedQueries({ @NamedQuery(name = "TaskNumber.deleteElectionEventPk", query = "delete from TaskNumber t where t.electionEvent.pk = :electionEventPk") })
public class TaskNumber extends VersionedEntity implements java.io.Serializable {

	private ElectionEvent electionEvent;
	private int lastTaskNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = SQLConstants.ELECTION_EVENT_PK, unique = true, nullable = false)
	public ElectionEvent getElectionEvent() {
		return this.electionEvent;
	}

	public void setElectionEvent(final ElectionEvent electionEvent) {
		this.electionEvent = electionEvent;
	}

	@Column(name = "last_task_number", nullable = false)
	public int getLastTaskNumber() {
		return this.lastTaskNumber;
	}

	public void setLastTaskNumber(final int lastTaskNumber) {
		this.lastTaskNumber = lastTaskNumber;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(electionEvent);
	}
}
