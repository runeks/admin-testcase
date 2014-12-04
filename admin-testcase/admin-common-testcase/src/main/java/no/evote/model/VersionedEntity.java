package no.evote.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Base entity for all versioned entities (i.e. has audit* fields)
 */
@MappedSuperclass
public abstract class VersionedEntity extends BaseEntity implements Serializable {

	private int auditOplock;
	private String auditOperation;
	private Date auditTimestamp;
	private String auditOperator;

	@Version
	@Column(name = "audit_oplock", nullable = false)
	public int getAuditOplock() {
		return this.auditOplock;
	}

	public void setAuditOplock(final int auditOplock) {
		this.auditOplock = auditOplock;
	}

	@Column(name = "audit_operation", length = 1)
	public String getAuditOperation() {
		return this.auditOperation;
	}

	public void setAuditOperation(final String auditOperation) {
		this.auditOperation = auditOperation;
	}

	@Temporal(TemporalType.TIMESTAMP)
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

	@Column(name = "audit_operator")
	public String getAuditOperator() {
		return this.auditOperator;
	}

	public void setAuditOperator(final String auditOperator) {
		this.auditOperator = auditOperator;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof VersionedEntity)) {
			return false;
		}
		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.appendSuper(super.toString())
				.append("auditOplock", auditOplock)
				.append("auditOperation", auditOperation)
				.append("auditTimestamp", auditTimestamp)
				.append("auditOperator", auditOperator)
				.toString();
	}
}
