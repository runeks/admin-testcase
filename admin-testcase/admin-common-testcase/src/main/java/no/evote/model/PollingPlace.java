package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.logging.AuditLogUtil;
import no.evote.persistence.AntiSamyEntityListener;
import no.evote.security.ContextSecurable;
import no.evote.validation.AntiSamy;
import no.evote.validation.Gps;
import no.evote.validation.ID;
import no.evote.validation.Letters;
import no.evote.validation.LettersOrDigits;
import no.evote.validation.StringNotNullEmptyOrBlanks;
import no.valg.eva.admin.common.AreaPath;

/**
 * Polling places
 */
@Entity
@Table(name = "polling_place", uniqueConstraints = { @UniqueConstraint(columnNames = { "polling_district_pk", "polling_place_id" }),
		@UniqueConstraint(columnNames = { "polling_district_pk", "election_day_voting" }) })
@AttributeOverride(name = "pk", column = @Column(name = "polling_place_pk"))
@EntityListeners({ AntiSamyEntityListener.class })
@NamedQueries({
		@NamedQuery(name = "PollingPlace.findById", query = "SELECT pp FROM PollingPlace pp WHERE pp.pollingDistrict.pk = :pollingDistrictPk AND pp.id = :id"),
		@NamedQuery(name = "PollingPlace.findFirstPollingPlace", query = "SELECT pp FROM PollingPlace pp WHERE pp.pollingDistrict.pk = :pollingDistrictPk"),
		@NamedQuery(name = "PollingPlace.findByElectionDayVoting", query = "SELECT pp FROM PollingPlace pp WHERE pp.pollingDistrict.pk = :pollingDistrictPk "
				+ "AND pp.electionDayVoting IS TRUE"),
		@NamedQuery(
				name = "PollingPlace.findAdvanceInBallotBoxPollingPlaceByPollingDistrict",
				query = "SELECT pp FROM PollingPlace pp WHERE pp.pollingDistrict.pk = :pollingDistrictPk AND pp.electionDayVoting IS FALSE "
                        + "AND pp.advanceVoteInBallotBox IS TRUE"),
		@NamedQuery(
				name = "PollingPlace.findNotAdvanceInBallotBoxPollingPlaceByPollingDistrict",
				query = "SELECT pp FROM PollingPlace pp WHERE pp.pollingDistrict.pk = :pollingDistrictPk AND pp.electionDayVoting IS FALSE "
                        + "AND pp.advanceVoteInBallotBox IS FALSE"),
		@NamedQuery(
				name = "PollingPlace.getElectoralRollForPollingPlace",
				query = "SELECT v FROM Voter v WHERE v.mvArea.pollingDistrict.pk = :pollingDistrictPk"),
		@NamedQuery(name = "PollingPlace.findWithoutPollingStationsInMunicipality", query = "SELECT pp FROM PollingPlace pp "
				+ "WHERE (SELECT count(*) from PollingStation ps where ps.pollingPlace = pp) = 0 "
				+ "AND pp.pollingDistrict.borough.municipality.pk = :municipalityPk AND pp.usingPollingStations = true "
				+ "AND pp.pollingDistrict.borough.municipality.electronicMarkoffs = false AND pp.electionDayVoting = true"),
		@NamedQuery(name = "PollingPlace.findWithoutGPSCoordinatesInMunicipality", query = "SELECT pp FROM PollingPlace pp "
				+ "WHERE pp.pollingDistrict.borough.municipality.pk = :municipalityPk AND (pp.gpsCoordinates is null OR pp.gpsCoordinates = '') ") })
@NamedQuery(name = "PollingPlace.findByPollingDistrict", query = "SELECT pp FROM PollingPlace pp WHERE pp.pollingDistrict.pk = :pollingDistrictPk")
public class PollingPlace extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private PollingDistrict pollingDistrict;
	private String id;
	private boolean electionDayVoting;
	private String name;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String postalCode;
	private String postTown;
	@AntiSamy
	private String infoText;
	private Boolean usingPollingStations = false;
	private boolean advanceVoteInBallotBox;
	private String gpsCoordinates;

	public PollingPlace() {
	}

	public PollingPlace(final String id, final String name, final String postalCode, final PollingDistrict pollingDistrict) {
		this.id = id;
		this.name = name;
		this.postalCode = postalCode;
		this.pollingDistrict = pollingDistrict;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "polling_district_pk", nullable = false)
	@NotNull
	public PollingDistrict getPollingDistrict() {
		return pollingDistrict;
	}

	public void setPollingDistrict(final PollingDistrict pollingDistrict) {
		this.pollingDistrict = pollingDistrict;
	}

	@Column(name = "polling_place_id", nullable = false, length = 4)
	@ID(size = 4)
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "election_day_voting", nullable = false)
	public boolean isElectionDayVoting() {
		return electionDayVoting;
	}

	public void setElectionDayVoting(final boolean electionDayVoting) {
		this.electionDayVoting = electionDayVoting;
	}

	@Column(name = "polling_place_name", nullable = false, length = 50)
	@LettersOrDigits
	@StringNotNullEmptyOrBlanks
	@Size(max = 50)
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "address_line1", length = 50)
	@LettersOrDigits
	@Size(max = 50)
	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(final String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	@Column(name = "address_line2", length = 50)
	@LettersOrDigits
	@Size(max = 50)
	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(final String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	@Column(name = "address_line3", length = 50)
	@LettersOrDigits
	@Size(max = 50)
	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(final String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	@Column(name = "postal_code", length = 4)
	@no.evote.validation.PostalCode
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(final String postalCode) {
		this.postalCode = postalCode;
	}

	@Letters
	@Column(name = "post_town", length = 50)
	@Size(max = 50)
	public String getPostTown() {
		return postTown;
	}

	public void setPostTown(final String postTown) {
		this.postTown = postTown;
	}

	@Column(name = "info_text", length = 150)
	@Size(max = 150)
	public String getInfoText() {
		return infoText;
	}

	public void setInfoText(final String infoText) {
		this.infoText = infoText;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		switch (level) {
		case POLLING_PLACE:
			return this.getPk();
		case POLLING_DISTRICT:
			return pollingDistrict.getPk();
		default:
			return null;
		}
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		return null;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(pollingDistrict, id);
	}

	@Column(name = "using_polling_stations", nullable = false)
	public Boolean getUsingPollingStations() {
		return usingPollingStations;
	}

	public void setUsingPollingStations(final Boolean usingPollingStations) {
		this.usingPollingStations = usingPollingStations;
	}

	@Column(name = "advance_vote_in_ballot_box", nullable = false)
	public boolean isAdvanceVoteInBallotBox() {
		return advanceVoteInBallotBox;
	}

	public void setAdvanceVoteInBallotBox(final boolean advanceVoteInBallotBox) {
		this.advanceVoteInBallotBox = advanceVoteInBallotBox;
	}

	@Size(max = 50)
	@Gps
	@Column(name = "gps_coordinates")
	public String getGpsCoordinates() {
		return gpsCoordinates;
	}

	public void setGpsCoordinates(final String gpsCoordinates) {
		this.gpsCoordinates = gpsCoordinates;
	}

	public AreaPath areaPath() {
		return getPollingDistrict().areaPath().add(getId());
	}
}
