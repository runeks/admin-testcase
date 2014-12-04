package no.evote.model.views;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VoterAuditId implements java.io.Serializable {

	private long voterPk;
	private int auditOplock;
	private Date auditTimestamp;

	@Column(name = "voter_pk", nullable = false)
	public long getVoterPk() {
		return this.voterPk;
	}

	public void setVoterPk(final long voterPk) {
		this.voterPk = voterPk;
	}

	@Column(name = "audit_oplock", nullable = false)
	public int getAuditOplock() {
		return this.auditOplock;
	}

	public void setAuditOplock(final int auditOplock) {
		this.auditOplock = auditOplock;
	}

	@Column(name = "audit_timestamp", nullable = false, length = 29)
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

	@Override
	public boolean equals(final Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof VoterAuditId)) {
			return false;
		}
		VoterAuditId castOther = (VoterAuditId) other;

		return (this.getVoterPk() == castOther.getVoterPk())
				&& (this.getAuditOplock() == castOther.getAuditOplock())
				&& ((this.getAuditTimestamp() == castOther.getAuditTimestamp()) || (this.getAuditTimestamp() != null && castOther.getAuditTimestamp() != null && this
						.getAuditTimestamp().equals(castOther.getAuditTimestamp())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getVoterPk();
		result = 37 * result + this.getAuditOplock();
		result = 37 * result + (getAuditTimestamp() == null ? 0 : this.getAuditTimestamp().hashCode());
		return result;
	}

}
