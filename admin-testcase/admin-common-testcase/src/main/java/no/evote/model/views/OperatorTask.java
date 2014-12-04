package no.evote.model.views;

import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
@Table(name = "operator_task")
@NamedQueries({ @NamedQuery(name = "OperatorTask.findOperatorTasks", query = "SELECT ot FROM OperatorTask ot WHERE ot.id.operatorRolePk = :operatorRolePk "
		+ "ORDER BY ot.resolved, ot.auditTimestamp DESC") })
public class OperatorTask implements java.io.Serializable {
	private OperatorTaskId id;
	private String auditUser;
	private String auditOperator;
	private Character auditOperation;
	private Date auditTimestamp;
	private Long operatorPk;
	private Long rolePk;
	private Long taskPk;
	private Long taskAccessPk;
	private Long taskEntityPk;
	private Long mvElectionPk;
	private Long mvAreaPk;
	private Long partyPk;
	private Boolean operatorsNotified;
	private Boolean resolved;
	private Long resolvedOperatorPk;

	// Transient:
	private String accessName;
	private String path;
	private List<String> additionalText;

	@Transient
	public List<String> getAdditionalText() {
		return additionalText;
	}

	public void setAdditionalText(final List<String> additionalText) {
		this.additionalText = additionalText;
	}

	@Transient
	public String getPath() {
		return path;
	}

	public void setPath(final String path) {
		this.path = path;
	}

	@Transient
	public String getAccessName() {
		return accessName;
	}

	public void setAccessName(final String accessName) {
		this.accessName = accessName;
	}

	@EmbeddedId
	@AttributeOverrides({ @AttributeOverride(name = "operatorRolePk", column = @Column(name = "operator_role_pk", nullable = false)),
			@AttributeOverride(name = "taskNumber", column = @Column(name = "task_number", nullable = false)) })
	public OperatorTaskId getId() {
		return this.id;
	}

	public void setId(final OperatorTaskId id) {
		this.id = id;
	}

	@Column(name = "audit_user")
	public String getAuditUser() {
		return this.auditUser;
	}

	public void setAuditUser(final String auditUser) {
		this.auditUser = auditUser;
	}

	@Column(name = "audit_operator")
	public String getAuditOperator() {
		return this.auditOperator;
	}

	public void setAuditOperator(final String auditOperator) {
		this.auditOperator = auditOperator;
	}

	@Column(name = "audit_operation", length = 1)
	public Character getAuditOperation() {
		return this.auditOperation;
	}

	public void setAuditOperation(final Character auditOperation) {
		this.auditOperation = auditOperation;
	}

	@javax.persistence.Temporal(TemporalType.TIMESTAMP)
	@Column(name = "audit_timestamp", length = 29)
	public Date getAuditTimestamp() {
		Date returnDate = null;
		if (this.auditTimestamp != null) {
			returnDate = new Date(this.auditTimestamp.getTime());
		}
		return returnDate;
	}

	public void setAuditTimestamp(final Date auditTimestamp) {
		if (auditTimestamp != null) {
			this.auditTimestamp = new Date(auditTimestamp.getTime());
		} else {
			this.auditTimestamp = null;
		}
	}

	@Column(name = "role_pk")
	public Long getRolePk() {
		return this.rolePk;
	}

	public void setRolePk(final Long rolePk) {
		this.rolePk = rolePk;
	}

	@Column(name = "operator_pk")
	public Long getOperatorPk() {
		return this.operatorPk;
	}

	public void setOperatorPk(final Long operatorPk) {
		this.operatorPk = operatorPk;
	}

	@Column(name = "task_pk")
	public Long getTaskPk() {
		return this.taskPk;
	}

	public void setTaskPk(final Long taskPk) {
		this.taskPk = taskPk;
	}

	@Column(name = "task_access_pk")
	public Long getTaskAccessPk() {
		return this.taskAccessPk;
	}

	public void setTaskAccessPk(final Long taskAccessPk) {
		this.taskAccessPk = taskAccessPk;
	}

	@Column(name = "task_entity_pk")
	public Long getTaskEntityPk() {
		return this.taskEntityPk;
	}

	public void setTaskEntityPk(final Long taskEntityPk) {
		this.taskEntityPk = taskEntityPk;
	}

	@Column(name = "mv_election_pk")
	public Long getMvElectionPk() {
		return this.mvElectionPk;
	}

	public void setMvElectionPk(final Long mvElectionPk) {
		this.mvElectionPk = mvElectionPk;
	}

	@Column(name = "mv_area_pk")
	public Long getMvAreaPk() {
		return this.mvAreaPk;
	}

	public void setMvAreaPk(final Long mvAreaPk) {
		this.mvAreaPk = mvAreaPk;
	}

	@Column(name = "party_pk")
	public Long getPartyPk() {
		return this.partyPk;
	}

	public void setPartyPk(final Long partyPk) {
		this.partyPk = partyPk;
	}

	@Column(name = "operators_notified")
	public Boolean getOperatorsNotified() {
		return this.operatorsNotified;
	}

	public void setOperatorsNotified(final Boolean operatorsNotified) {
		this.operatorsNotified = operatorsNotified;
	}

	@Column(name = "resolved")
	public Boolean getResolved() {
		return this.resolved;
	}

	public void setResolved(final Boolean resolved) {
		this.resolved = resolved;
	}

	@Column(name = "resolved_operator_pk")
	public Long getResolvedOperatorPk() {
		return this.resolvedOperatorPk;
	}

	public void setResolvedOperatorPk(final Long resolvedOperatorPk) {
		this.resolvedOperatorPk = resolvedOperatorPk;
	}

}
