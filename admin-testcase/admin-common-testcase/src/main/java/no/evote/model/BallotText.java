package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import no.evote.logging.AuditLogUtil;
import no.evote.validation.StringNotNullEmptyOrBlanks;

/**
 * Text linked to individual ballots
 */
@Entity
@Table(name = "ballot_text", uniqueConstraints = @UniqueConstraint(columnNames = { "ballot_pk", "ballot_text_name" }))
@AttributeOverride(name = "pk", column = @Column(name = "ballot_text_pk"))
public class BallotText extends VersionedEntity implements java.io.Serializable {

	private Ballot ballot;
	private String name;
	private String ballotText;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ballot_pk", nullable = false)
	public Ballot getBallot() {
		return this.ballot;
	}

	public void setBallot(final Ballot ballot) {
		this.ballot = ballot;
	}

	@Column(name = "ballot_text_name", nullable = false, length = 50)
	@StringNotNullEmptyOrBlanks
	@Size(max = 50)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "ballot_text", nullable = false, length = 150)
	@StringNotNullEmptyOrBlanks
	@Size(max = 150)
	public String getBallotText() {
		return this.ballotText;
	}

	public void setBallotText(final String ballotText) {
		this.ballotText = ballotText;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(ballot, name);
	}
}
