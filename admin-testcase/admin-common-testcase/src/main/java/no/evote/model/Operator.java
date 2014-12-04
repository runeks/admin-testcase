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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import no.evote.constants.SQLConstants;
import no.evote.logging.AuditLogUtil;
import no.evote.util.EqualsHashCodeUtil;
import no.evote.validation.FoedselsNummer;
import no.evote.validation.Letters;
import no.evote.validation.LettersOrDigits;
import no.evote.validation.OperatorValidationManual;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * RBAC: Application user
 */
@Entity
@Table(name = "operator", uniqueConstraints = @UniqueConstraint(columnNames = { SQLConstants.ELECTION_EVENT_PK, "operator_id" }))
@AttributeOverride(name = "pk", column = @Column(name = "operator_pk"))
@NamedQueries({
		@NamedQuery(name = "Operator.findAll", query = "SELECT o FROM Operator o ORDER BY o.lastName, o.firstName, o.middleName"),
		@NamedQuery(
				name = "Operator.findAllInElectionEvent",
				query = "SELECT o FROM Operator o WHERE o.electionEvent = :event ORDER BY o.lastName, o.firstName, o.middleName"),
		@NamedQuery(name = "Operator.findById", query = "SELECT o FROM Operator o WHERE o.id = :id ORDER BY o.lastName, o.firstName, o.middleName"),
		@NamedQuery(
				name = "Operator.findByParty",
				query = "SELECT o FROM Operator o WHERE o.party.pk = :partyPk ORDER BY o.lastName, o.firstName, o.middleName"),
		@NamedQuery(name = "Operator.findOperatorsParty", query = "SELECT p FROM Party p, Operator o WHERE o.pk = :operatorPk AND o.party.pk = p.pk"),
		@NamedQuery(name = "Operator.findByElectionEventAndId", query = "SELECT o FROM Operator o WHERE o.electionEvent.pk = :eventPk AND o.id = :operatorId"),
		@NamedQuery(name = "Operator.findElectionEvents", query = "SELECT e FROM Operator o" + ", IN( o.electionEvent ) e WHERE o.id = :operatorId") })
@NamedNativeQueries({
		@NamedNativeQuery(name = "Operator.findByName", query = "SELECT * FROM Operator WHERE election_event_pk = :electionEventPk"
                + " AND soundex_tsvector(election_event_pk, name_line) @@ soundex_tsquery(:electionEventPk, :nameLine) LIMIT 1000", resultClass = Operator.class),
		@NamedNativeQuery(name = "Operator.findAllDistinctId", query = "SELECT DISTINCT ON (o.operator_id) * FROM operator o", resultClass = Operator.class),
		@NamedNativeQuery(name = "Operator.findDescOperators", query = "SELECT DISTINCT op2.* FROM mv_area mva "
				+ "JOIN mv_area mva2 ON ((public.text2ltree(mva.area_path) OPERATOR(public.@>) public.text2ltree(mva2.area_path))) "
				+ "JOIN operator_role opr2 on mva2.mv_area_pk = opr2.mv_area_pk " + "JOIN operator op2 on opr2.operator_pk = op2.operator_pk "
				+ "WHERE mva.mv_area_pk = ?1 ORDER BY op2.last_name, op2.first_name, op2.middle_name", resultClass = Operator.class),
		@NamedNativeQuery(
				name = "Operator.findOperatorsAtOwnLevel",
				query = "SELECT DISTINCT op2.* FROM operator_role opr2 JOIN operator op2 on opr2.operator_pk = op2.operator_pk "
						+ "WHERE opr2.mv_area_pk = ?1 ORDER BY op2.last_name, op2.first_name, op2.middle_name",
				resultClass = Operator.class) })
public class Operator extends VersionedEntity implements java.io.Serializable {

	private ElectionEvent electionEvent;
	private String id;
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
	private String infoText;
	private Party party;
	private boolean active;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "party_pk")
	public Party getParty() {
		return party;
	}

	public void setParty(final Party party) {
		this.party = party;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = SQLConstants.ELECTION_EVENT_PK, nullable = false)
	public ElectionEvent getElectionEvent() {
		return electionEvent;
	}

	public void setElectionEvent(final ElectionEvent electionEvent) {
		this.electionEvent = electionEvent;
	}

	@FoedselsNummer(groups = { OperatorValidationManual.class })
	@Column(name = "operator_id", nullable = false, length = 11)
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "name_line", nullable = false, length = 152)
	@Size(max = 152)
	public String getNameLine() {
		return nameLine;
	}

	public void setNameLine(final String nameLine) {
		this.nameLine = nameLine;
	}

	@NotEmpty(message = "{@validation.name.first.notEmpty}", groups = { OperatorValidationManual.class })
	@Size(max = 50, groups = { OperatorValidationManual.class })
	@Letters(extraChars = " .-'", groups = { OperatorValidationManual.class })
	@Column(name = "first_name", nullable = false, length = 50)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "middle_name", length = 50)
	@Size(max = 50, groups = { OperatorValidationManual.class })
	@Letters(extraChars = " .-'", groups = { OperatorValidationManual.class })
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	@NotEmpty(message = "{@validation.name.last.notEmpty}", groups = { OperatorValidationManual.class })
	@Size(max = 50, groups = { OperatorValidationManual.class })
	@Letters(extraChars = " .-'", groups = { OperatorValidationManual.class })
	@Column(name = "last_name", nullable = false, length = 50)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	@Length(min = 0, max = 50, message = "{@validation.streetAddress.length}", groups = { OperatorValidationManual.class })
	@LettersOrDigits(groups = { OperatorValidationManual.class })
	@Column(name = "address_line1", length = 50)
	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(final String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	@Length(min = 0, max = 50, message = "{@validation.streetAddress.length}", groups = { OperatorValidationManual.class })
	@LettersOrDigits(groups = { OperatorValidationManual.class })
	@Column(name = "address_line2", length = 50)
	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(final String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	@Length(min = 0, max = 50, message = "{@validation.streetAddress.length}", groups = { OperatorValidationManual.class })
	@LettersOrDigits(groups = { OperatorValidationManual.class })
	@Column(name = "address_line3", length = 50)
	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(final String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	@Pattern(regexp = "([0-9]{4})?", message = "{@validation.postalCode.regex}", groups = { OperatorValidationManual.class })
	@Column(name = "postal_code", length = 4)
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(final String postalCode) {
		this.postalCode = postalCode;
	}

	@Letters(groups = { OperatorValidationManual.class })
	@Column(name = "post_town", length = 50)
	@Size(max = 50, groups = { OperatorValidationManual.class })
	public String getPostTown() {
		return postTown;
	}

	public void setPostTown(final String postTown) {
		this.postTown = postTown;
	}

	@Email(message = "{@validation.email}", groups = { OperatorValidationManual.class })
	@Column(name = "email", length = 129)
	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Length(min = 0, max = 14, message = "{@validation.tlf.length}", groups = { OperatorValidationManual.class })
	@Pattern(regexp = "\\+?([0-9]{3,14})?", message = "{@validation.tlf.regex}", groups = { OperatorValidationManual.class })
	@Column(name = "telephone_number", length = 35)
	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(final String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	@Column(name = "info_text", length = 150)
	@Size(max = 150, groups = { OperatorValidationManual.class })
	public String getInfoText() {
		return infoText;
	}

	public void setInfoText(final String infoText) {
		this.infoText = infoText;
	}

	@Column(name = "active", nullable = false)
	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Operator) {
			if (getElectionEvent() != null) {
				if (getElectionEvent().getPk() != null) {
					return (EqualsHashCodeUtil.genericEquals(obj, this) && getElectionEvent().getPk().
							equals(((Operator) obj).getElectionEvent().getPk()));
				} else {
					return (EqualsHashCodeUtil.genericEquals(obj, this) && (EqualsHashCodeUtil
							.genericEquals(((Operator) obj).getElectionEvent(), getElectionEvent())));
				}
			}
			return EqualsHashCodeUtil.genericEquals(obj, this);
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (getElectionEvent() != null && getElectionEvent().getPk() != null) {
			return EqualsHashCodeUtil.genericHashCode(this) + getElectionEvent().getPk().hashCode();
		}
		return EqualsHashCodeUtil.genericHashCode(this);

	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(electionEvent, id);
	}
}
