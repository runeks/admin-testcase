package no.evote.model;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import no.evote.constants.SQLConstants;
import no.evote.logging.AuditLogUtil;

/**
 * Election event day(s)
 */
@Entity
@Table(name = "election_day", uniqueConstraints = { @UniqueConstraint(columnNames = { SQLConstants.ELECTION_EVENT_PK, "election_day_date" }) })
@AttributeOverride(name = "pk", column = @Column(name = "election_day_pk"))
@NamedNativeQueries({ @NamedNativeQuery(
		name = "ElectionDay.findForPollingDistrict",
		query = "SELECT ed.* FROM election_day ed "
                + "JOIN mv_area pda ON pda.polling_district_pk = ? AND pda.area_level = 5 "
                + "JOIN mv_area ppa ON ppa.polling_district_pk = pda.polling_district_pk AND ppa.area_level = 6 "
                + "WHERE EXISTS (select opening_hours_pk FROM opening_hours oh where oh.polling_place_pk = ppa.polling_place_pk "
                + "AND oh.election_day_pk = ed.election_day_pk)"
                + "ORDER BY ed.election_day_date ASC",
				resultClass = ElectionDay.class) })
public class ElectionDay extends VersionedEntity implements java.io.Serializable {

	private ElectionEvent electionEvent;
	private Date date;
	private Date startTime;
	private Date endTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = SQLConstants.ELECTION_EVENT_PK, nullable = false)
	public ElectionEvent getElectionEvent() {
		return this.electionEvent;
	}

	public void setElectionEvent(final ElectionEvent electionEvent) {
		this.electionEvent = electionEvent;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "election_day_date", nullable = false, length = 13)
	public Date getDate() {
		Date returnDate = null;
		if (this.date != null) {
			returnDate = new Date(this.date.getTime());
		}
		return returnDate;
	}

	public void setDate(final Date date) {
		if (date != null) {
			this.date = new Date(date.getTime());
		} else {
			this.date = null;
		}
	}

	@Temporal(TemporalType.TIME)
	@Column(name = "election_day_start_time", nullable = false, length = 15)
	public Date getStartTime() {
		Date returnDate = null;
		if (this.startTime != null) {
			returnDate = new Date(this.startTime.getTime());
		}
		return returnDate;
	}

	public void setStartTime(final Date startTime) {
		if (startTime != null) {
			this.startTime = new Date(startTime.getTime());
		} else {
			this.startTime = null;
		}
	}

	@Temporal(TemporalType.TIME)
	@Column(name = "election_day_end_time", nullable = false, length = 15)
	public Date getEndTime() {
		Date returnDate = null;
		if (this.endTime != null) {
			returnDate = new Date(this.endTime.getTime());
		}
		return returnDate;
	}

	public void setEndTime(final Date endTime) {
		if (endTime != null) {
			this.endTime = new Date(endTime.getTime());
		} else {
			this.endTime = null;
		}
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(electionEvent, date, startTime, endTime);
	}
}
