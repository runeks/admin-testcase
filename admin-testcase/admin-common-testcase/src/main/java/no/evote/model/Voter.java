package no.evote.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import no.evote.constants.SQLConstants;
import no.evote.logging.AuditLogUtil;
import no.evote.persistence.AntiSamyEntityListener;
import no.evote.util.DateUtil;
import no.evote.validation.AntiSamy;
import no.evote.validation.FoedselsNummer;
import no.evote.validation.Letters;
import no.evote.validation.LettersOrDigits;
import no.evote.validation.StringNotNullEmptyOrBlanks;
import no.evote.validation.VoterValidationManual;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

/**
 * Voter information
 */
@Entity
@Table(name = "voter", uniqueConstraints = { @UniqueConstraint(columnNames = { SQLConstants.ELECTION_EVENT_PK, "municipality_id", "voter_number" }),
		@UniqueConstraint(columnNames = { SQLConstants.ELECTION_EVENT_PK, "voter_id" }) })
@AttributeOverride(name = "pk", column = @Column(name = "voter_pk"))
@EntityListeners({ AntiSamyEntityListener.class })
@NamedQueries({
		@NamedQuery(name = "Voter.findById", query = "SELECT v FROM Voter v WHERE v.id = :id AND v.electionEvent.pk = :electionEventPk"),
		@NamedQuery(name = "Voter.findByVoterNumber", query = "SELECT v FROM Voter v WHERE v.electionEvent.pk = :electionEventPk "
				+ "AND v.countryId = :countryId " + "AND v.countyId = :countyId " + "AND v.municipalityId = :municipalityId " + "AND v.number = :number"),
		@NamedQuery(
				name = "Voter.findNotApprovedByMunicipality",
				query = "SELECT v FROM Voter v WHERE v.municipalityId = :municipalityId AND v.approved IS FALSE "
						+ "AND v.eligible IS TRUE AND v.electionEvent.pk = :electionEventPk"),
		@NamedQuery(
				name = "Voter.findVoterByNr",
				query = "SELECT v FROM Voter v WHERE v.dateOfBirth = :dateOfBirth AND v.postalCode = :postalCode AND v.firstName "
						+ "LIKE :fName AND v.lastName LIKE :lName AND v.electionEvent.pk = :electionEventPk"),
		@NamedQuery(name = "Voter.countByMvArea", query = "SELECT COUNT(v) FROM Voter v WHERE v.mvArea.pk = :mvAreaPk"),
		@NamedQuery(name = "Voter.deleteWhereMvElectionIsNull", query = "DELETE From Voter v WHERE v.mvArea is null and v.electionEvent.pk = :electionEventPk")  })
@NamedNativeQueries({
		@NamedNativeQuery(
				name = "Voter.updateLineNumber",
				query = "UPDATE line_number SET last_page = ?1, last_line = ?2 WHERE polling_district_pk = ?3",
				resultSetMapping = "voidDummy"),
		@NamedNativeQuery(
				name = "Voter.findByName",
				query = "SELECT * FROM voter v WHERE v.election_event_pk = :electionEventPk "
						+ "AND soundex_tsvector(v.election_event_pk, v.name_line) @@ soundex_tsquery(:electionEventPk ,:nameLine) LIMIT 1000",
				resultClass = Voter.class) })
@SqlResultSetMappings({ @SqlResultSetMapping(name = "voidDummy") })
public class Voter extends VersionedEntity implements java.io.Serializable {

	private MvArea mvArea;
	@Deprecated
	private ElectionEvent electionEvent;
	private String id;
	private Date dateOfBirth;
	private Integer number;
	private Integer importBatchNumber;
	private String countryId;
	private String countyId;
	private String municipalityId;
	private String boroughId;
	private String pollingDistrictId;
	private boolean eligible;
	private String nameLine;
	private String firstName;
	private String middleName;
	private String lastName;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String postalCode;
	private String postTown;
	private String email;
	private String telephoneNumber;
	private boolean mailingAddressSpecified;
	private String mailingAddressLine1;
	private String mailingAddressLine2;
	private String mailingAddressLine3;
	private String mailingCountryCode;
	private String approvalRequest;
	private boolean approved;
	private Timestamp dateTimeSubmitted;
	private String aarsakskode;
	private Character endringstype;
	private Character statuskode;
	private Date regDato;
	private Character spesRegType;
	private Integer electoralRollPage;
	private Integer electoralRollLine;
	private boolean votingCardReturned;
	private int temporaryCredentialsCount;
	private Timestamp temporaryCredentialsTimestamp;
	private PollingPlace temporaryCredentialsPollingPlace;
	private PollingStation pollingStation;
	private boolean fictitious = false;

	@AntiSamy
	private String additionalInformation;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mv_area_pk")
	public MvArea getMvArea() {
		return mvArea;
	}

	public void setMvArea(final MvArea mvArea) {
		this.mvArea = mvArea;
	}

	@Deprecated
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = SQLConstants.ELECTION_EVENT_PK, nullable = false)
	public ElectionEvent getElectionEvent() {
		return electionEvent;
	}

	@Deprecated
	public void setElectionEvent(final ElectionEvent electionEvent) {
		this.electionEvent = electionEvent;
	}

	@Column(name = "voter_id", nullable = false, length = 11)
	@FoedselsNummer(groups = { VoterValidationManual.class })
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_birth", length = 13)
	@Past(groups = { VoterValidationManual.class })
	public Date getDateOfBirth() {
		Date returnDate = null;
		if (dateOfBirth != null) {
			returnDate = new Date(dateOfBirth.getTime());
		}
		return returnDate;
	}

	public void setDateOfBirth(final Date dateOfBirth) {
		if (dateOfBirth != null) {
			this.dateOfBirth = new Date(dateOfBirth.getTime());
		} else {
			this.dateOfBirth = null;
		}
	}

	@Column(name = "voter_number")
	public Integer getNumber() {
		return number;
	}

	public void setNumber(final Integer number) {
		this.number = number;
	}

	@Column(name = "import_batch_number")
	public Integer getImportBatchNumber() {
		return importBatchNumber;
	}

	public void setImportBatchNumber(final Integer importBatchNumber) {
		this.importBatchNumber = importBatchNumber;
	}

	@Column(name = "country_id", nullable = false, length = 2)
	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(final String countryId) {
		this.countryId = countryId;
	}

	@Column(name = "county_id", nullable = false, length = 2)
	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(final String countyId) {
		this.countyId = countyId;
	}

	@Column(name = "municipality_id", nullable = false, length = 4)
	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(final String municipalityId) {
		this.municipalityId = municipalityId;
	}

	@Column(name = "borough_id", nullable = false, length = 6)
	public String getBoroughId() {
		return boroughId;
	}

	public void setBoroughId(final String boroughId) {
		this.boroughId = boroughId;
	}

	@Column(name = "polling_district_id", nullable = false, length = 4)
	public String getPollingDistrictId() {
		return pollingDistrictId;
	}

	public void setPollingDistrictId(final String pollingDistrictId) {
		this.pollingDistrictId = pollingDistrictId;
	}

	@Column(name = "eligible", nullable = false)
	public boolean isEligible() {
		return eligible;
	}

	public void setEligible(final boolean eligible) {
		this.eligible = eligible;
	}

	@Column(name = "name_line", nullable = false, length = 152)
	@Size(max = 152)
	public String getNameLine() {
		return nameLine;
	}

	public void setNameLine(final String nameLine) {
		this.nameLine = nameLine;
	}

	@Column(name = "first_name", nullable = false, length = 50)
	@Letters(extraChars = " .-'", groups = VoterValidationManual.class)
	@StringNotNullEmptyOrBlanks(groups = { VoterValidationManual.class })
	@Size(max = 50, groups = { VoterValidationManual.class })
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "middle_name", length = 50)
	@Letters(extraChars = " .-'", groups = VoterValidationManual.class)
	@Size(max = 50, groups = { VoterValidationManual.class })
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "last_name", nullable = false, length = 50)
	@Letters(extraChars = " .-'", groups = VoterValidationManual.class)
	@StringNotNullEmptyOrBlanks(groups = { VoterValidationManual.class })
	@Size(max = 50, groups = { VoterValidationManual.class })
	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "address_line1", length = 50)
	@LettersOrDigits(groups = VoterValidationManual.class)
	@Length(min = 0, max = 50, message = "{@validation.streetAddress.length}", groups = { VoterValidationManual.class })
	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(final String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	@Column(name = "address_line2", length = 50)
	@LettersOrDigits(groups = VoterValidationManual.class)
	@Length(min = 0, max = 50, message = "{@validation.coAddress.length}", groups = { VoterValidationManual.class })
	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(final String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	@Column(name = "address_line3", length = 50)
	@LettersOrDigits(groups = VoterValidationManual.class)
	@Size(max = 50, groups = { VoterValidationManual.class })
	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(final String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	@Column(name = "postal_code", length = 4)
	@Pattern(regexp = "([0-9]{4})?", message = "{@validation.postalCode.regex}", groups = { VoterValidationManual.class })
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(final String postalCode) {
		this.postalCode = postalCode;
	}

	@Column(name = "post_town", length = 50)
	@Letters(groups = VoterValidationManual.class)
	@Size(max = 50, groups = { VoterValidationManual.class })
	public String getPostTown() {
		return postTown;
	}

	public void setPostTown(final String postTown) {
		this.postTown = postTown;
	}

	@Column(name = "email", length = 129)
	@Email(message = "{@validation.email}", groups = { VoterValidationManual.class })
	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Column(name = "telephone_number", length = 35)
	@Length(min = 0, max = 35, message = "{@validation.tlf.length}", groups = { VoterValidationManual.class })
	@Pattern(regexp = "\\+?([0-9]{3,34})?", message = "{@validation.tlf.regex}", groups = { VoterValidationManual.class })
	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(final String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	@Column(name = "mailing_address_specified")
	public boolean isMailingAddressSpecified() {
		return mailingAddressSpecified;
	}

	public void setMailingAddressSpecified(final boolean mailingAddressSpecified) {
		this.mailingAddressSpecified = mailingAddressSpecified;
	}

	@Column(name = "mailing_address_line1", length = 50)
	@Length(min = 0, max = 50, message = "{@validation.addressLine1.length}", groups = { VoterValidationManual.class })
	public String getMailingAddressLine1() {
		return mailingAddressLine1;
	}

	public void setMailingAddressLine1(final String mailingAddressLine1) {
		this.mailingAddressLine1 = mailingAddressLine1;
	}

	@Column(name = "mailing_address_line2", length = 50)
	@Length(min = 0, max = 50, message = "{@validation.addressLine2.length}", groups = { VoterValidationManual.class })
	public String getMailingAddressLine2() {
		return mailingAddressLine2;
	}

	public void setMailingAddressLine2(final String mailingAddressLine2) {
		this.mailingAddressLine2 = mailingAddressLine2;
	}

	@Column(name = "mailing_address_line3", length = 50)
	@Length(min = 0, max = 50, message = "{@validation.addressLine3.length}", groups = { VoterValidationManual.class })
	public String getMailingAddressLine3() {
		return mailingAddressLine3;
	}

	public void setMailingAddressLine3(final String mailingAddressLine3) {
		this.mailingAddressLine3 = mailingAddressLine3;
	}

	@Column(name = "mailing_country_code")
	public String getMailingCountryCode() {
		return mailingCountryCode;
	}

	public void setMailingCountryCode(final String mailingCountryCode) {
		this.mailingCountryCode = mailingCountryCode;
	}

	@Column(name = "approval_request", length = 150)
	@Size(max = 150, groups = { VoterValidationManual.class })
	public String getApprovalRequest() {
		return approvalRequest;
	}

	public void setApprovalRequest(final String approvalRequest) {
		this.approvalRequest = approvalRequest;
	}

	@Column(name = "approved", nullable = false)
	public boolean isApproved() {
		return approved;
	}

	public void setApproved(final boolean approved) {
		this.approved = approved;
	}

	@Column(name = "date_time_submitted", nullable = false, length = 29)
	public Timestamp getDateTimeSubmitted() {
		Timestamp returnDate = null;
		if (dateTimeSubmitted != null) {
			returnDate = new Timestamp(dateTimeSubmitted.getTime());
			returnDate.setNanos(dateTimeSubmitted.getNanos());
		}
		return returnDate;
	}

	public void setDateTimeSubmitted(final Timestamp dateTimeSubmitted) {
		if (dateTimeSubmitted != null) {
			this.dateTimeSubmitted = new Timestamp(dateTimeSubmitted.getTime());
			this.dateTimeSubmitted.setNanos(dateTimeSubmitted.getNanos());
		} else {
			this.dateTimeSubmitted = null;
		}
	}

	public void setDateTimeSubmitted(final Date dateTimeSubmitted) {
		if (dateTimeSubmitted != null) {
			this.dateTimeSubmitted = new Timestamp(dateTimeSubmitted.getTime());
		} else {
			this.dateTimeSubmitted = null;
		}
	}

	@Column(name = "aarsakskode", length = 2)
	public String getAarsakskode() {
		return aarsakskode;
	}

	public void setAarsakskode(final String aarsakskode) {
		this.aarsakskode = aarsakskode;
	}

	@Column(name = "endringstype", length = 1)
	public Character getEndringstype() {
		return endringstype;
	}

	public void setEndringstype(final Character endringstype) {
		this.endringstype = endringstype;
	}

	@Column(name = "statuskode", length = 1)
	public Character getStatuskode() {
		return statuskode;
	}

	public void setStatuskode(final Character statuskode) {
		this.statuskode = statuskode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "reg_dato", length = 13)
	public Date getRegDato() {
		Date returnDate = null;
		if (regDato != null) {
			returnDate = new Date(regDato.getTime());
		}
		return returnDate;
	}

	public void setRegDato(final Date regDato) {
		if (regDato != null) {
			this.regDato = new Date(regDato.getTime());
		} else {
			this.regDato = null;
		}
	}

	@Column(name = "spes_reg_type", length = 1)
	public Character getSpesRegType() {
		return spesRegType;
	}

	public void setSpesRegType(final Character spesRegType) {
		this.spesRegType = spesRegType;
	}

	@Column(name = "electoral_roll_page")
	public Integer getElectoralRollPage() {
		return electoralRollPage;
	}

	public void setElectoralRollPage(final Integer electoralRollPage) {
		this.electoralRollPage = electoralRollPage;
	}

	@Column(name = "electoral_roll_line")
	public Integer getElectoralRollLine() {
		return electoralRollLine;
	}

	public void setElectoralRollLine(final Integer electoralRollLine) {
		this.electoralRollLine = electoralRollLine;
	}

	@Column(name = "voting_card_returned", nullable = false)
	public boolean isVotingCardReturned() {
		return votingCardReturned;
	}

	public void setVotingCardReturned(final boolean votingCardReturned) {
		this.votingCardReturned = votingCardReturned;
	}

	@Transient
	public String getFormattedDateOfBirth() {
		return DateUtil.getFormatedShortDate(getDateOfBirth());
	}

	@Transient
	public void setFormattedDateOfBirth(final String dateOfBirth) {
		setDateOfBirth(DateUtil.parse(dateOfBirth));
	}

	@Transient
	public String getAgeInYears() {
		return DateUtil.getAgeInYears(getDateOfBirth());
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(electionEvent, municipalityId, id);
	}

	@Column(name = "temporary_credentials_count", nullable = false)
	public int getTemporaryCredentialsCount() {
		return temporaryCredentialsCount;
	}

	public void setTemporaryCredentialsCount(final int temporaryCredentialsCount) {
		this.temporaryCredentialsCount = temporaryCredentialsCount;
	}

	@Column(name = "temporary_credentials_timestamp", length = 29)
	public Timestamp getTemporaryCredentialsTimestamp() {
		Timestamp returnDate = null;
		if (temporaryCredentialsTimestamp != null) {
			returnDate = new Timestamp(temporaryCredentialsTimestamp.getTime());
			returnDate.setNanos(temporaryCredentialsTimestamp.getNanos());
		}
		return returnDate;
	}

	public void setTemporaryCredentialsTimestamp(final Timestamp temporaryCredentialsTimestamp) {
		if (temporaryCredentialsTimestamp != null) {
			this.temporaryCredentialsTimestamp = new Timestamp(temporaryCredentialsTimestamp.getTime());
			this.temporaryCredentialsTimestamp.setNanos(temporaryCredentialsTimestamp.getNanos());
		} else {
			this.temporaryCredentialsTimestamp = null;
		}
	}

	public void setTemporaryCredentialsTimestamp(final Date temporaryCredentialsTimestamp) {
		if (temporaryCredentialsTimestamp != null) {
			this.temporaryCredentialsTimestamp = new Timestamp(temporaryCredentialsTimestamp.getTime());
		} else {
			this.temporaryCredentialsTimestamp = null;
		}
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "temporary_credentials_polling_place_pk")
	public PollingPlace getTemporaryCredentialsPollingPlace() {
		return temporaryCredentialsPollingPlace;
	}

	public void setTemporaryCredentialsPollingPlace(final PollingPlace temporaryCredentialsPollingPlace) {
		this.temporaryCredentialsPollingPlace = temporaryCredentialsPollingPlace;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "polling_station_pk")
	public PollingStation getPollingStation() {
		return pollingStation;
	}

	public void setPollingStation(final PollingStation pollingStation) {
		this.pollingStation = pollingStation;
	}

	public void updateNameLine() {
		StringBuffer nameLine = new StringBuffer("");
		if (getFirstName() != null) {
			nameLine.append(getFirstName());
			nameLine.append(" ");
		}
		if (getMiddleName() != null) {
			nameLine.append(getMiddleName());
			nameLine.append(" ");
		}
		if (getLastName() != null) {
			nameLine.append(getLastName());
		}
		setNameLine(nameLine.toString());
	}

	@Column(name = "additional_information", length = 200)
	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(final String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	@Column(name = "fictitious", nullable = false)
	public boolean isFictitious() {
		return fictitious;
	}

	public void setFictitious(final boolean fictitious) {
		this.fictitious = fictitious;
	}

	public Character getCorrectEndringsType(final Voter oldVoter) {
		Character endringType = 'E';
		if (!oldVoter.isApproved() && this.isApproved()) {
			endringType = 'T';
		} else if (oldVoter.isApproved() && !this.isApproved()) {
			endringType = 'A';
		}

		return endringType;
	}
}
