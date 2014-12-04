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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import no.evote.constants.SQLConstants;
import no.evote.logging.AuditLogUtil;
import no.evote.constants.AreaLevelEnum;
import no.evote.security.ContextSecurable;
import no.evote.constants.ElectionLevelEnum;
import no.evote.validation.StringNotNullEmptyOrBlanks;

/**
 * Incoming/outgoing generic communication from/to voters, e.g. applications for inclusion in the Electoral Roll / feedback on the applications
 */
@Entity
@Table(name = "generic_communication", uniqueConstraints = @UniqueConstraint(columnNames = { SQLConstants.ELECTION_EVENT_PK, "incoming", "transaction_id" }))
@AttributeOverride(name = "pk", column = @Column(name = "generic_communication_pk"))
@NamedQueries({ @NamedQuery(name = "GenericCommunication.findByMvAreaAndElectionEvent", query = "select gc from GenericCommunication as gc "
		+ " where gc.electionEvent.pk = :electionEventPk and gc.municipality.pk = :municipalityPk") })
public class GenericCommunication extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private ElectionEvent electionEvent;
	private IncomingStatus incomingStatus;
	private Municipality municipality;
	private String transactionId;
	private boolean incoming;
	private String voterId;
	private Date dateOfBirth;
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
	private String messageText;
	private String infoText;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = SQLConstants.ELECTION_EVENT_PK, nullable = false)
	public ElectionEvent getElectionEvent() {
		return this.electionEvent;
	}

	public void setElectionEvent(final ElectionEvent electionEvent) {
		this.electionEvent = electionEvent;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "incoming_status_pk")
	public IncomingStatus getIncomingStatus() {
		return this.incomingStatus;
	}

	public void setIncomingStatus(final IncomingStatus incomingStatus) {
		this.incomingStatus = incomingStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "municipality_pk", nullable = false)
	public Municipality getMunicipality() {
		return this.municipality;
	}

	public void setMunicipality(final Municipality municipality) {
		this.municipality = municipality;
	}

	@Column(name = "transaction_id", nullable = false, length = 25)
	@StringNotNullEmptyOrBlanks
	@Size(max = 25)
	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(final String transactionId) {
		this.transactionId = transactionId;
	}

	@Column(name = "incoming", nullable = false)
	public boolean isIncoming() {
		return this.incoming;
	}

	public void setIncoming(final boolean incoming) {
		this.incoming = incoming;
	}

	@Column(name = "voter_id", nullable = false, length = 11)
	public String getVoterId() {
		return this.voterId;
	}

	public void setVoterId(final String voterId) {
		this.voterId = voterId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_birth", length = 13)
	public Date getDateOfBirth() {
		Date returnDate = null;
		if (this.dateOfBirth != null) {
			returnDate = new Date(this.dateOfBirth.getTime());
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

	@Column(name = "name_line", nullable = false, length = 152)
	@StringNotNullEmptyOrBlanks
	@Size(max = 152)
	public String getNameLine() {
		return this.nameLine;
	}

	public void setNameLine(final String nameLine) {
		this.nameLine = nameLine;
	}

	@Column(name = "first_name", length = 50)
	@Size(max = 50)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "middle_name")
	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "last_name", nullable = false, length = 50)
	@StringNotNullEmptyOrBlanks
	@Size(max = 50)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "address_line1", length = 50)
	@Size(max = 50)
	public String getAddressLine1() {
		return this.addressLine1;
	}

	public void setAddressLine1(final String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	@Column(name = "address_line2", length = 50)
	@Size(max = 50)
	public String getAddressLine2() {
		return this.addressLine2;
	}

	public void setAddressLine2(final String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	@Column(name = "address_line3", length = 50)
	@Size(max = 50)
	public String getAddressLine3() {
		return this.addressLine3;
	}

	public void setAddressLine3(final String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	@Column(name = "postal_code", length = 4)
	@Pattern(regexp = "([0-9]{4})?", message = "@validation.postalCode.regex")
	public String getPostalCode() {
		return this.postalCode;
	}

	public void setPostalCode(final String postalCode) {
		this.postalCode = postalCode;
	}

	@Column(name = "post_town", length = 50)
	@Size(max = 50)
	public String getPostTown() {
		return this.postTown;
	}

	public void setPostTown(final String postTown) {
		this.postTown = postTown;
	}

	@Column(name = "email", nullable = false, length = 129)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Column(name = "telephone_number", length = 35)
	public String getTelephoneNumber() {
		return this.telephoneNumber;
	}

	public void setTelephoneNumber(final String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	@Column(name = "message_text", nullable = false, length = 32768)
	@StringNotNullEmptyOrBlanks
	@Size(max = 32768)
	public String getMessageText() {
		return this.messageText;
	}

	public void setMessageText(final String messageText) {
		this.messageText = messageText;
	}

	@Column(name = "info_text", length = 150)
	@Size(max = 150)
	public String getInfoText() {
		return this.infoText;
	}

	public void setInfoText(final String infoText) {
		this.infoText = infoText;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		return null;
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		if (level.equals(ElectionLevelEnum.ELECTION_EVENT)) {
			return electionEvent.getPk();
		}
		return null;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(electionEvent, incoming, transactionId);
	}
}
