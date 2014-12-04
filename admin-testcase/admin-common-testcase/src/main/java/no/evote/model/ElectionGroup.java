package no.evote.model;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.constants.SQLConstants;
import no.evote.logging.AuditLogUtil;
import no.evote.security.ContextSecurable;
import no.evote.validation.ID;
import no.evote.validation.LettersOrDigits;
import no.evote.validation.StringNotNullEmptyOrBlanks;
import no.valg.eva.admin.common.ElectionPath;

/**
 * Grouping of elections within an election event, facilitating RBAC access to a subset of all elections via a single role
 */
@Entity
@Table(name = "election_group", uniqueConstraints = @UniqueConstraint(columnNames = { SQLConstants.ELECTION_EVENT_PK, "election_group_id" }))
@AttributeOverride(name = "pk", column = @Column(name = "election_group_pk"))
@NamedQueries({ @NamedQuery(
		name = "ElectionGroup.findById",
		query = "SELECT eg FROM ElectionGroup eg WHERE eg.electionEvent.pk = :electionEventPk AND eg.id = :id") })
public class ElectionGroup extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private ElectionEvent electionEvent;
	private String id;
	private String name;
	private boolean paperVoting;
	private boolean electronicVoting;
	private Date earlyAdvanceVotingStartDate;
	private Date earlyAdvanceVotingEndDate;
	private Date foreignAdvanceVotingStartDate;
	private Date foreignAdvanceVotingEndDate;
	private Date domesticAdvanceVotingStartDate;
	private Date domesticAdvanceVotingEndDate;
	private Date electronicVotingStartDatetime;
	private Date electronicVotingEndDatetime;
	private Date electronicVotingControlledStartDatetime;
	private Date electronicVotingControlledEndDatetime;

	public ElectionGroup() {
	}

	public ElectionGroup(final String id, final String name, final ElectionEvent electionEvent) {
		this.id = id;
		this.name = name;
		this.electionEvent = electionEvent;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = SQLConstants.ELECTION_EVENT_PK, nullable = false)
	@NotNull
	public ElectionEvent getElectionEvent() {
		return this.electionEvent;
	}

	public void setElectionEvent(final ElectionEvent electionEvent) {
		this.electionEvent = electionEvent;
	}

	@Column(name = "election_group_id", nullable = false, length = 2)
	@ID(size = 2)
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "election_group_name", nullable = false, length = 100)
	@LettersOrDigits
	@StringNotNullEmptyOrBlanks
	@Size(max = 100)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "paper_voting", nullable = false)
	public boolean isPaperVoting() {
		return this.paperVoting;
	}

	public void setPaperVoting(final boolean paperVoting) {
		this.paperVoting = paperVoting;
	}

	@Column(name = "electronic_voting", nullable = false)
	public boolean isElectronicVoting() {
		return this.electronicVoting;
	}

	public void setElectronicVoting(final boolean electronicVoting) {
		this.electronicVoting = electronicVoting;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "early_advance_voting_start_date", length = 13)
	public Date getEarlyAdvanceVotingStartDate() {
		Date returnDate = null;
		if (this.earlyAdvanceVotingStartDate != null) {
			returnDate = new Date(this.earlyAdvanceVotingStartDate.getTime());
		}
		return returnDate;
	}

	public void setEarlyAdvanceVotingStartDate(final Date earlyAdvanceVotingStartDate) {
		if (earlyAdvanceVotingStartDate != null) {
			this.earlyAdvanceVotingStartDate = new Date(earlyAdvanceVotingStartDate.getTime());
		} else {
			this.earlyAdvanceVotingStartDate = null;
		}
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "early_advance_voting_end_date", length = 13)
	public Date getEarlyAdvanceVotingEndDate() {
		Date returnDate = null;
		if (this.earlyAdvanceVotingEndDate != null) {
			returnDate = new Date(this.earlyAdvanceVotingEndDate.getTime());
		}
		return returnDate;
	}

	public void setEarlyAdvanceVotingEndDate(final Date earlyAdvanceVotingEndDate) {
		if (earlyAdvanceVotingEndDate != null) {
			this.earlyAdvanceVotingEndDate = new Date(earlyAdvanceVotingEndDate.getTime());
		} else {
			this.earlyAdvanceVotingEndDate = null;
		}
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "foreign_advance_voting_start_date", length = 13)
	public Date getForeignAdvanceVotingStartDate() {
		Date returnDate = null;
		if (this.foreignAdvanceVotingStartDate != null) {
			returnDate = new Date(this.foreignAdvanceVotingStartDate.getTime());
		}
		return returnDate;
	}

	public void setForeignAdvanceVotingStartDate(final Date foreignAdvanceVotingStartDate) {
		if (foreignAdvanceVotingStartDate != null) {
			this.foreignAdvanceVotingStartDate = new Date(foreignAdvanceVotingStartDate.getTime());
		} else {
			this.foreignAdvanceVotingStartDate = null;
		}
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "foreign_advance_voting_end_date", length = 13)
	public Date getForeignAdvanceVotingEndDate() {
		Date returnDate = null;
		if (this.foreignAdvanceVotingEndDate != null) {
			returnDate = new Date(this.foreignAdvanceVotingEndDate.getTime());
		}
		return returnDate;
	}

	public void setForeignAdvanceVotingEndDate(final Date foreignAdvanceVotingEndDate) {
		if (foreignAdvanceVotingEndDate != null) {
			this.foreignAdvanceVotingEndDate = new Date(foreignAdvanceVotingEndDate.getTime());
		} else {
			this.foreignAdvanceVotingEndDate = null;
		}
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "domestic_advance_voting_start_date", length = 13)
	public Date getDomesticAdvanceVotingStartDate() {
		Date returnDate = null;
		if (this.domesticAdvanceVotingStartDate != null) {
			returnDate = new Date(this.domesticAdvanceVotingStartDate.getTime());
		}
		return returnDate;
	}

	public void setDomesticAdvanceVotingStartDate(final Date domesticAdvanceVotingStartDate) {
		if (domesticAdvanceVotingStartDate != null) {
			this.domesticAdvanceVotingStartDate = new Date(domesticAdvanceVotingStartDate.getTime());
		} else {
			this.domesticAdvanceVotingStartDate = null;
		}
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "domestic_advance_voting_end_date", length = 13)
	public Date getDomesticAdvanceVotingEndDate() {
		Date returnDate = null;
		if (this.domesticAdvanceVotingEndDate != null) {
			returnDate = new Date(this.domesticAdvanceVotingEndDate.getTime());
		}
		return returnDate;
	}

	public void setDomesticAdvanceVotingEndDate(final Date domesticAdvanceVotingEndDate) {
		if (domesticAdvanceVotingEndDate != null) {
			this.domesticAdvanceVotingEndDate = new Date(domesticAdvanceVotingEndDate.getTime());
		} else {
			this.domesticAdvanceVotingEndDate = null;
		}
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "electronic_voting_start_datetime", length = 35)
	public Date getElectronicVotingStartDatetime() {
		Date returnDate = null;
		if (this.electronicVotingStartDatetime != null) {
			returnDate = new Date(this.electronicVotingStartDatetime.getTime());
		}
		return returnDate;
	}

	public void setElectronicVotingStartDatetime(final Date electronicVotingStartDatetime) {
		if (electronicVotingStartDatetime != null) {
			this.electronicVotingStartDatetime = new Date(electronicVotingStartDatetime.getTime());
		} else {
			this.electronicVotingStartDatetime = null;
		}
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "electronic_voting_end_datetime", length = 35)
	public Date getElectronicVotingEndDatetime() {
		Date returnDate = null;
		if (this.electronicVotingEndDatetime != null) {
			returnDate = new Date(this.electronicVotingEndDatetime.getTime());
		}
		return returnDate;
	}

	public void setElectronicVotingEndDatetime(final Date electronicVotingEndDatetime) {
		if (electronicVotingEndDatetime != null) {
			this.electronicVotingEndDatetime = new Date(electronicVotingEndDatetime.getTime());
		} else {
			this.electronicVotingEndDatetime = null;
		}
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "electronic_voting_controlled_environment_start_datetime", length = 35)
	public Date getElectronicVotingControlledStartDatetime() {
		Date returnDate = null;
		if (this.electronicVotingControlledStartDatetime != null) {
			returnDate = new Date(this.electronicVotingControlledStartDatetime.getTime());
		}
		return returnDate;
	}

	public void setElectronicVotingControlledStartDatetime(final Date electronicVotingControlledEnvironmentStartDatetime) {
		this.electronicVotingControlledStartDatetime = electronicVotingControlledEnvironmentStartDatetime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "electronic_voting_controlled_environment_end_datetime", length = 35)
	public Date getElectronicVotingControlledEndDatetime() {
		Date returnDate = null;
		if (this.electronicVotingControlledEndDatetime != null) {
			returnDate = new Date(this.electronicVotingControlledEndDatetime.getTime());
		}
		return returnDate;
	}

	public void setElectronicVotingControlledEndDatetime(final Date electronicVotingControlledEnvironmentEndDatetime) {
		this.electronicVotingControlledEndDatetime = electronicVotingControlledEnvironmentEndDatetime;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		return null;
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		switch (level) {
		case ELECTION_EVENT:
			return electionEvent.getPk();
		case ELECTION_GROUP:
			return getPk();
		default:
			return null;
		}
	}

	@Transient
	public boolean isAdvancePeriodConfigured() {
		return earlyAdvanceVotingStartDate != null || domesticAdvanceVotingStartDate != null || foreignAdvanceVotingStartDate != null;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(electionEvent, id);
	}

	@Transient
	public boolean hasElectronicVotingControlledDates() {
		return electronicVotingControlledStartDatetime != null && electronicVotingControlledEndDatetime != null;
	}

	public ElectionPath electionPath() {
		return getElectionEvent().electionPath().add(getId());
	}

}
