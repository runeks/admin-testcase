package no.evote.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.constants.EvoteConstants;
import no.evote.logging.AuditLogUtil;
import no.evote.model.VoteCategory.VoteCategoryValues;
import no.evote.security.ContextSecurable;
import no.evote.util.DateUtil;
import no.evote.validation.FoedselsNummerValidator;
import no.evote.validation.ProposalValidationManual;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Candidates on candidate lists
 */
@Entity
@Table(name = "candidate", uniqueConstraints = { @UniqueConstraint(columnNames = { "ballot_pk", "display_order" }),
		@UniqueConstraint(columnNames = { "ballot_pk", "candidate_id" }) })
@AttributeOverride(name = "pk", column = @Column(name = "candidate_pk"))
@NamedQueries({
		@NamedQuery(name = "Candidate.findByAffiliation", query = "select c from Candidate c where c.affiliation.pk = :pk ORDER BY c.displayOrder"),
		@NamedQuery(name = "Candidate.findByBelowDisplayOrder", query = "select c from Candidate c where c.ballot.pk = :ballotPk AND"
				+ " c.displayOrder > :displayOrder ORDER BY c.displayOrder"),
		@NamedQuery(name = "Candidate.findByIdInOtherBallot", query = "select c from Candidate c where " + "c.ballot.ballotStatus.id <> "
				+ EvoteConstants.BALLOT_STATUS_WITHDRAWN + " and c.ballot.ballotStatus.id <> " + EvoteConstants.BALLOT_STATUS_REJECTED
				+ "and c.id = :id and c.affiliation.ballot.pk <> :bpk and c.affiliation.ballot.contest.election.pk = :epk"),
		@NamedQuery(name = "Candidate.findByIdInAnotherElection", query = "select c from Candidate c where " + "c.ballot.ballotStatus.id <> "
				+ EvoteConstants.BALLOT_STATUS_WITHDRAWN + " and c.ballot.ballotStatus.id <> " + EvoteConstants.BALLOT_STATUS_REJECTED
				+ "and c.id = :id and c.affiliation.ballot.pk <> :bpk and c.affiliation.ballot.contest.election.electionGroup.pk = :egpk"),
		@NamedQuery(name = "Candidate.findByName", query = "select c from Candidate c where c.nameLine = :name"),
		@NamedQuery(name = "Candidate.findByBallotAndOrder", query = "select c from Candidate c where c.ballot.pk = :bpk AND c.displayOrder = :order"),
		@NamedQuery(name = "Candidate.findPkByBallotAndOrder", query = "select c.pk from Candidate c where c.ballot.pk = :bpk AND c.displayOrder = :order"),
		@NamedQuery(name = "Candidate.findByBallotAndId", query = "select c from Candidate c where c.ballot.pk = :bpk AND c.id = :id"),
		@NamedQuery(name = "Candidate.findCandidateByBallotAndDisplayOrderRange",
			query = "select c from Candidate c where c.ballot.pk = :bpk AND"
					+ " c.displayOrder >= :displayOrderFrom AND c.displayOrder <= :displayOrderTo ORDER BY c.displayOrder"),
		@NamedQuery(name = "Candidate.findCandidatesForOtherBallotsInSameContest",
				query = "select c from Candidate c, Ballot b where b.pk = :ballotPk and c.ballot.contest.pk = b.contest.pk and c.ballot.pk != :ballotPk ") })
public class Candidate extends ProposalPerson implements java.io.Serializable, ContextSecurable {

	private MaritalStatus maritalStatus;
	private Affiliation affiliation;
	private Ballot ballot;
	private int displayOrder;
	private String id;
	private Date dateOfBirth;
	private boolean baselineVotes;
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
	private String residence;
	private String profession;
	private String infoText;
	private boolean approved;

	private List<String> validationMessages = new ArrayList<String>();
	private String partyName;
	private VoteCategoryValues voteCategoryValue;
	private Boolean elected;

	public Candidate() {
		super();
	}

	public Candidate(Candidate candidate) {
		super();
		affiliation = candidate.getAffiliation();
		ballot = candidate.getBallot();
		maritalStatus = candidate.getMaritalStatus();
		displayOrder = candidate.getDisplayOrder();
		id = candidate.getId();
		dateOfBirth = candidate.getDateOfBirth();
		baselineVotes = candidate.isBaselineVotes();
		nameLine = candidate.getNameLine();
		firstName = candidate.getFirstName();
		middleName = candidate.getMiddleName();
		lastName = candidate.getLastName();
		addressLine1 = candidate.getAddressLine1();
		addressLine2 = candidate.getAddressLine2();
		addressLine3 = candidate.getAddressLine3();
		postalCode = candidate.getPostalCode();
		postTown = candidate.getPostTown();
		email = candidate.getEmail();
		telephoneNumber = candidate.getTelephoneNumber();
		residence = candidate.getResidence();
		profession = candidate.getProfession();
		infoText = candidate.getInfoText();
		approved = candidate.isApproved();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "marital_status_pk", nullable = false)
	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(final MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "affiliation_pk")
	public Affiliation getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(final Affiliation affiliation) {
		this.affiliation = affiliation;
	}

	@Override
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ballot_pk", nullable = false)
	public Ballot getBallot() {
		return ballot;
	}

	public void setBallot(final Ballot ballot) {
		this.ballot = ballot;
	}

	@Override
	@Column(name = "display_order", nullable = false)
	@Min(value = 0, groups = { ProposalValidationManual.class })
	@Max(value = EvoteConstants.MAX_CANDIDATES_IN_AFFILIATION, message = "@validation.candidate.max", groups = { ProposalValidationManual.class })
	public int getDisplayOrder() {
		return displayOrder;
	}

	@Override
	public void setDisplayOrder(final int displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Override
	@Column(name = "candidate_id", nullable = false, length = 11)
	public String getId() {
		return id;
	}

	@Override
	public void setId(final String id) {
		this.id = id;
	}

	@Override
	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_birth", length = 13)
	@NotNull(message = "@validation.dataOfBirth.notNull", groups = { ProposalValidationManual.class })
	@Past(message = "@validation.dataOfBirth.past", groups = { ProposalValidationManual.class })
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

	@Column(name = "baseline_votes", nullable = false)
	public boolean isBaselineVotes() {
		return baselineVotes;
	}

	public void setBaselineVotes(final boolean baselineVotes) {
		this.baselineVotes = baselineVotes;
	}

	@Column(name = "name_line", nullable = false, length = 152)
	@Size(max = 152, groups = { ProposalValidationManual.class })
	public String getNameLine() {
		return nameLine;
	}

	@Override
	public void setNameLine(final String nameLine) {
		this.nameLine = nameLine;
	}

	@Override
	@Column(name = "first_name", nullable = false, length = 50)
	@NotEmpty(message = "@validation.name.first.notEmpty", groups = { ProposalValidationManual.class })
	@Size(max = 50, message = "@validation.name.length", groups = { ProposalValidationManual.class })
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	@Override
	@Column(name = "middle_name", length = 50)
	@Size(max = 50, groups = { ProposalValidationManual.class })
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	@Override
	@Column(name = "last_name", nullable = false, length = 50)
	@NotEmpty(message = "@validation.name.last.notEmpty", groups = { ProposalValidationManual.class })
	@Size(max = 50, message = "@validation.name.length", groups = { ProposalValidationManual.class })
	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "address_line1", length = 50)
	@Length(min = 0, max = 50, message = "@validation.address.length", groups = { ProposalValidationManual.class })
	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(final String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	@Column(name = "address_line2", length = 50)
	@Size(max = 50, groups = { ProposalValidationManual.class })
	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(final String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	@Column(name = "address_line3", length = 50)
	@Size(max = 50, groups = { ProposalValidationManual.class })
	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(final String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	@Override
	@Column(name = "postal_code", length = 4)
	@Pattern(regexp = "([0-9]{4})?", message = "@validation.postalCode.regex", groups = { ProposalValidationManual.class })
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(final String postalCode) {
		this.postalCode = postalCode;
	}

	@Column(name = "post_town", length = 50)
	@Length(min = 0, max = 20, message = "@validation.postalTown.length", groups = { ProposalValidationManual.class })
	public String getPostTown() {
		return postTown;
	}

	@Override
	public void setPostTown(final String postTown) {
		this.postTown = postTown;
	}

	@Column(name = "email", length = 129)
	@Length(min = 0, max = 129, message = "@validation.email.length", groups = { ProposalValidationManual.class })
	@Email(message = "@validation.email", groups = { ProposalValidationManual.class })
	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Column(name = "telephone_number", length = 35)
	@Length(min = 0, max = 35, message = "@validation.tlf.length", groups = { ProposalValidationManual.class })
	@Pattern(regexp = "\\+?([0-9]{3,14})?", message = "@validation.tlf.regex", groups = { ProposalValidationManual.class })
	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(final String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	@Column(name = "residence", length = 50)
	@Length(min = 0, max = 50, message = "@validation.residence.length", groups = { ProposalValidationManual.class })
	public String getResidence() {
		return residence;
	}

	public void setResidence(final String residence) {
		this.residence = residence;
	}

	@Column(name = "profession", length = 50)
	@Size(max = 50, message = "@validation.profession.length", groups = { ProposalValidationManual.class })
	public String getProfession() {
		return profession;
	}

	public void setProfession(final String profession) {
		this.profession = profession;
	}

	@Column(name = "info_text", length = 150)
	@Size(max = 150, message = "@validation.infoText.length", groups = { ProposalValidationManual.class })
	public String getInfoText() {
		return infoText;
	}

	public void setInfoText(final String infoText) {
		this.infoText = infoText;
	}

	@Column(name = "approved", nullable = false)
	public boolean isApproved() {
		return approved;
	}

	@Override
	public void setApproved(final boolean approved) {
		this.approved = approved;
	}

	@Transient
	public String getFormattedDateOfBirth() {
		return DateUtil.getFormatedShortDate(getDateOfBirth());
	}

	@Transient
	public void setFormattedDateOfBirth(final String dateOfBirth) {
		setDateOfBirth(DateUtil.parse(dateOfBirth));
	}

	@Override
	@Transient
	public boolean isInValid() {
		return !validationMessages.isEmpty();
	}

	@Override
	@Transient
	public void addValidationMessage(final String validationMessage) {
		validationMessages.add(validationMessage);
	}

	@Override
	@Transient
	public void clearValidationMessages() {
		validationMessages.clear();
	}

	@Override
	@Transient
	public List<String> getValidationMessageList() {
		return validationMessages;
	}

	public void setValidationMessageList(final List<String> validationMessages) {
		this.validationMessages = validationMessages;
	}

	@Override
	@Transient
	public boolean isIdSet() {
		return FoedselsNummerValidator.isFoedselsNummerValid(id);
	}

	@Transient
	public String getPartyName() {
		return partyName;
	}

	@Transient
	public void setPartyName(final String partyName) {
		this.partyName = partyName;
	}

	@Transient
	public VoteCategoryValues getVoteCategoryValue() {
		return voteCategoryValue;
	}

	@Transient
	public void setVoteCategoryValue(final VoteCategoryValues voteCategoryValue) {
		this.voteCategoryValue = voteCategoryValue;
	}

	@Transient
	public String getInformationString(final boolean showCandidateProfession, final boolean showCandidateResidence) {
		StringBuilder candidateInfo = new StringBuilder();

		candidateInfo.append(toString()).append(", ");

		if (!StringUtils.isEmpty(getFormattedDateOfBirth())) {
			candidateInfo.append(new SimpleDateFormat("yyyy", Locale.getDefault()).format(getDateOfBirth()) + ", ");
		}
		if (showCandidateProfession && !StringUtils.isEmpty(getProfession())) {
			candidateInfo.append(getProfession()).append(", ");
		}
		if (showCandidateResidence && !StringUtils.isEmpty(getResidence())) {
			candidateInfo.append(getResidence()).append(", ");
		}

		return candidateInfo.toString().substring(0, (candidateInfo.length() - 2)).toString();
	}

	@Transient
	public Boolean getElected() {
		return elected;
	}

	public void setElected(final Boolean sqlResultLine) {
		elected = sqlResultLine;
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
		return AuditLogUtil.generateId(ballot, id);
	}

	@Transient
	public boolean isMale() {
		return getGender().equals(Gender.MALE.getValue());
	}

	@Transient
	public boolean isFemale() {
		return getGender().equals(Gender.FEMALE.getValue());
	}

	@Transient
	public String getGender() {
		return !FoedselsNummerValidator.isFoedselsNummerValid(getId()) ? "" : isOdd(ninthDigit(getId())) ? Gender.MALE.getValue() : Gender.FEMALE.getValue();
	}

	private boolean isOdd(final int i) {
		return i % 2 != 0;
	}

	private Integer ninthDigit(final String fnr) {
		// CSOFF: MagicNumber
		return Integer.valueOf(fnr.substring(8, 9));
		// CSON: MagicNumber
	}

	public enum Gender {
		FEMALE("F"), MALE("M");

		private final String value;

		private Gender(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}
