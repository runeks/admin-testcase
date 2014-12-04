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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import no.evote.logging.AuditLogUtil;
import no.evote.constants.AreaLevelEnum;
import no.evote.security.ContextSecurable;
import no.evote.constants.ElectionLevelEnum;

/**
 * Opening hours for polling places
 */
@Entity
@Table(name = "opening_hours", uniqueConstraints = @UniqueConstraint(columnNames = { "polling_place_pk", "election_day_pk", "start_time" }))
@AttributeOverride(name = "pk", column = @Column(name = "opening_hours_pk"))
@NamedQueries({
		@NamedQuery(name = "OpeningHours.findOpeningHoursForPollingPlace", query = "SELECT oh FROM OpeningHours oh WHERE oh.pollingPlace.pk = :pollingPlacePk "
				+ "ORDER BY oh.electionDay.date ASC, oh.startTime ASC"),
		@NamedQuery(
				name = "OpeningHours.countOpeningHoursByElectionDay",
				query = "SELECT count(oh) FROM OpeningHours oh WHERE oh.electionDay.pk = :electionDayPk ") })
@NamedNativeQueries({ @NamedNativeQuery(
		name = "OpeningHours.findPollingPlacesWithoutOpeningHours",
		query = "SELECT pp.* FROM polling_place pp LEFT JOIN opening_hours oh ON pp.polling_place_pk = oh.polling_place_pk "
				+ "LEFT JOIN polling_district pd ON pp.polling_district_pk = pd.polling_district_pk "
				+ "LEFT JOIN borough b ON pd.borough_pk = b.borough_pk LEFT JOIN municipality m ON b.municipality_pk = m.municipality_pk WHERE oh.start_time is null "
				+ "AND election_day_voting is true AND pd.municipality is false AND m.municipality_pk = ?",
		resultClass = PollingPlace.class) })
public class OpeningHours extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private PollingPlace pollingPlace;
	private ElectionDay electionDay;
	private Date startTime;
	private Date endTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "polling_place_pk", nullable = false)
	@NotNull
	public PollingPlace getPollingPlace() {
		return this.pollingPlace;
	}

	public void setPollingPlace(final PollingPlace pollingPlace) {
		this.pollingPlace = pollingPlace;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "election_day_pk", nullable = false)
	@NotNull
	public ElectionDay getElectionDay() {
		return this.electionDay;
	}

	public void setElectionDay(final ElectionDay electionDay) {
		this.electionDay = electionDay;
	}

	@Temporal(TemporalType.TIME)
	@Column(name = "start_time", nullable = false, length = 15)
	@NotNull
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
	@Column(name = "end_time", nullable = false, length = 15)
	@NotNull
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
	public Long getAreaPk(final AreaLevelEnum level) {
		if (level.equals(AreaLevelEnum.POLLING_PLACE)) {
			return pollingPlace.getPk();
		}
		return null;
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		return null;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(pollingPlace, electionDay, startTime);
	}
}
