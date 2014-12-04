package no.evote.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Base entity for all entities (i.e. has pk field)
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private Long pk;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(unique = true, insertable = false, updatable = false, nullable = false)
	public Long getPk() {
		return this.pk;
	}

	public final void setPk(final Long pk) {
		this.pk = pk;
	}

	@Transient
	public abstract String getAuditLogId();

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof BaseEntity)) {
			return false;
		}
		BaseEntity rhs = (BaseEntity) obj;
		return new EqualsBuilder()
				.append(this.pk, rhs.pk)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(pk)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("pk", pk)
				.toString();
	}
}
