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
import javax.validation.constraints.Size;

import no.evote.logging.AuditLogUtil;
import no.evote.constants.AreaLevelEnum;
import no.evote.security.ContextSecurable;
import no.evote.constants.ElectionLevelEnum;
import no.evote.validation.ReferendumOptionValidationManual;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Referendum options
 */
@Entity
@Table(name = "referendum_option", uniqueConstraints = { @UniqueConstraint(columnNames = { "ballot_pk", "display_order" }),
		@UniqueConstraint(columnNames = { "ballot_pk", "referendum_option_id" }) })
@AttributeOverride(name = "pk", column = @Column(name = "referendum_option_pk"))
@NamedQueries({ @NamedQuery(name = "ReferendumOption.findByContest", query = "select ro from ReferendumOption ro where ro.ballot.contest.pk = :contestPk "
		+ "AND ro.approved = true ORDER BY ro.displayOrder ASC") })
public class ReferendumOption extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private Ballot ballot;
	private int displayOrder;
	private String id;
	private String name;
	private boolean approved;
	private boolean inValid;

	public ReferendumOption(final Ballot ballot, final String id, final String name, final boolean approved) {
		this.ballot = ballot;
		this.id = id;
		this.name = name;
		this.approved = approved;
	}

	public ReferendumOption() {
		super();
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ballot_pk", nullable = false)
	public Ballot getBallot() {
		return this.ballot;
	}

	public void setBallot(final Ballot ballot) {
		this.ballot = ballot;
	}

	@Column(name = "display_order", nullable = false)
	public int getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(final int displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Column(name = "referendum_option_id", nullable = false, length = 6)
	@NotEmpty(message = "@referendum.validation.id.notEmpty", groups = { ReferendumOptionValidationManual.class })
	@Size(max = 6, message = "@referendum.validation.id.length", groups = { ReferendumOptionValidationManual.class })
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "referendum_option_name", nullable = false, length = 100)
	@NotEmpty(message = "@referendum.validation.notEmpty", groups = { ReferendumOptionValidationManual.class })
	@Size(max = 100, message = "@validation.name.length", groups = { ReferendumOptionValidationManual.class })
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "approved", nullable = false)
	public boolean isApproved() {
		return this.approved;
	}

	public void setApproved(final boolean approved) {
		this.approved = approved;
	}

	@Transient
	public boolean isInValid() {
		return inValid;
	}

	public void setInValid(final boolean inValid) {
		this.inValid = inValid;
	}

	@Override
	public String toString() {
		return name;
	}

	@Transient
	public boolean isIdSet() {
		return false;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		return null;
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		if (level.equals(ElectionLevelEnum.CONTEST)) {
			return ballot.getContest().getPk();
		}
		return null;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(ballot, displayOrder);
	}
}
