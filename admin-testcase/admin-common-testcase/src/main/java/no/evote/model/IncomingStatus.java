package no.evote.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import no.evote.validation.StringNotNullEmptyOrBlanks;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Status for incoming generic communication
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "incoming_status", uniqueConstraints = @UniqueConstraint(columnNames = "incoming_status_id"))
@AttributeOverride(name = "pk", column = @Column(name = "incoming_status_pk"))
public class IncomingStatus extends VersionedEntity implements java.io.Serializable {

	private Access access;
	private int id;
	private String name;

	public enum IncomingStatusValues {
		NOT_PROCESSED(0), APPROVED(1), REJECTED(2);

		private static final Map<Integer, IncomingStatusValues> LOOKUP = new HashMap<Integer, IncomingStatusValues>();

		static {
			for (IncomingStatusValues s : EnumSet.allOf(IncomingStatusValues.class)) {
				LOOKUP.put(s.getId(), s);
			}
		}

		private final int id;

		private IncomingStatusValues(final int id) {
			this.id = id;
		}

		public Integer getId() {
			return id;
		}

		public static IncomingStatusValues get(final int id) {
			return LOOKUP.get(id);
		}
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_access_pk")
	public Access getAccess() {
		return this.access;
	}

	public void setAccess(final Access access) {
		this.access = access;
	}

	@Transient
	public IncomingStatusValues getIncomingStatus() {
		return IncomingStatusValues.get(id);
	}

	@Column(name = "incoming_status_id", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@Column(name = "incoming_status_name", nullable = false, length = 50)
	@StringNotNullEmptyOrBlanks
	@Size(max = 50)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Transient
	public boolean isNotProcessed() {
		return (getIncomingStatus().equals(IncomingStatusValues.NOT_PROCESSED));
	}

	@Transient
	public boolean isApproved() {
		return (getIncomingStatus().equals(IncomingStatusValues.APPROVED));
	}

	@Transient
	public boolean isRejected() {
		return (getIncomingStatus().equals(IncomingStatusValues.REJECTED));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getPk() == null) ? 0 : getPk().hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		IncomingStatus other = (IncomingStatus) obj;
		if (getPk() == null) {
			if (other.getPk() != null) {
				return false;
			}
		} else if (!getPk().equals(other.getPk())) {
			return false;
		}
		return true;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return String.valueOf(id);
	}
}
