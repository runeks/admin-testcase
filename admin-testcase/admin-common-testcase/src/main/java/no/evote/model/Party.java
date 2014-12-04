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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import no.evote.constants.EvoteConstants;
import no.evote.constants.SQLConstants;
import no.evote.util.EqualsHashCodeUtil;
import no.evote.validation.LettersOrDigits;
import no.evote.validation.PartyValidationManual;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Standard party list. Codes from Statistisk sentralbyrå
 */
@Entity
@Table(name = "party", uniqueConstraints = { @UniqueConstraint(columnNames = { SQLConstants.ELECTION_EVENT_PK, "display_order" }),
		@UniqueConstraint(columnNames = { SQLConstants.ELECTION_EVENT_PK, "short_code" }),
		@UniqueConstraint(columnNames = { SQLConstants.ELECTION_EVENT_PK, "party_id" }) })
@AttributeOverride(name = "pk", column = @Column(name = "party_pk"))
@NamedQueries({
		@NamedQuery(name = "Party.findWithNoAffiliationByContest", query = "SELECT p1 FROM Party p1 WHERE "
				+ "p1.electionEvent.pk = (SELECT c.electionEvent.pk FROM MvElection c "
				+ "WHERE c.contest.pk = :contestPk AND c.electionLevel = 3) AND p1.partyCategory.id < '3' AND p1.pk NOT IN "
				+ "(SELECT p2.pk FROM Party p2, Affiliation a WHERE p2.pk = a.party.pk AND a.ballot.contest.pk = :contestPk)"),
		@NamedQuery(name = "Party.findById", query = "SELECT p from Party p WHERE p.electionEvent.pk = :electionEventPk AND UPPER(p.id) = UPPER(:id)"),
		@NamedQuery(name = "Party.findPartiesInEvent", query = "SELECT p from Party p WHERE p.electionEvent.pk = :electionEventPk"),
		@NamedQuery(
				name = "Party.findByShortCodeEvent",
				query = "SELECT p from Party p WHERE p.electionEvent.pk = :electionEventPk AND p.shortCode = :shortCode") })
public class Party extends VersionedEntity implements java.io.Serializable {

	private ElectionEvent electionEvent;
	private PartyCategory partyCategory;
	private String id;
	private Integer displayOrder;
	private Integer shortCode;
	private String organisationId;
	private String name;
	private boolean approved;
	private boolean blank;
	private String translatedPartyName;

	public Party() {
	}

	public Party(final String id, final String name, final Integer shortCode, final Integer displayOrder, final PartyCategory partyCategory,
			final ElectionEvent electionEvent) {
		this.electionEvent = electionEvent;
		this.id = id;
		this.shortCode = shortCode;
		this.displayOrder = displayOrder;
		this.partyCategory = partyCategory;
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = SQLConstants.ELECTION_EVENT_PK, nullable = false)
	@NotNull(groups = { PartyValidationManual.class })
	public ElectionEvent getElectionEvent() {
		return this.electionEvent;
	}

	public void setElectionEvent(final ElectionEvent electionEvent) {
		this.electionEvent = electionEvent;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "party_category_pk", nullable = false)
	public PartyCategory getPartyCategory() {
		return this.partyCategory;
	}

	public void setPartyCategory(final PartyCategory partyCategory) {
		this.partyCategory = partyCategory;
	}

	@Length(min = 0, max = 8, message = "@validation.party.id.max", groups = { PartyValidationManual.class })
	@NotEmpty(message = "@validation.party.id.notEmpty", groups = { PartyValidationManual.class })
	@Column(name = "party_id", nullable = false, length = 8)
	@Pattern(regexp = EvoteConstants.REGEXP_PARTY_ID, message = "@validation.party.id.regex", groups = { PartyValidationManual.class })
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "display_order", nullable = true)
	public Integer getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(final Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Column(name = "short_code", nullable = true)
	public Integer getShortCode() {
		return this.shortCode;
	}

	public void setShortCode(final Integer shortCode) {
		this.shortCode = shortCode;
	}

	@Column(name = "organisation_id", length = 9)
	public String getOrganisationId() {
		return this.organisationId;
	}

	public void setOrganisationId(final String organisationId) {
		this.organisationId = organisationId;
	}

	@NotEmpty(message = "@validation.party.name.notEmpty", groups = { PartyValidationManual.class })
	@Length(min = 0, max = 200, message = "@validation.party.name.max", groups = { PartyValidationManual.class })
	@Column(name = "party_name", nullable = false, length = 50)
	@LettersOrDigits(groups = PartyValidationManual.class)
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

	@Column(name = "blank", nullable = false)
	public boolean isBlank() {
		return this.blank;
	}

	public void setBlank(final boolean blank) {
		this.blank = blank;
	}

	/*
		@Override
		public String toString() {
			return id;
		}
	*/

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Party) {
			return EqualsHashCodeUtil.genericEquals(obj, this);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return EqualsHashCodeUtil.genericHashCode(this);
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return id;
	}

	@Transient
	public String getTranslatedPartyName() {
		return translatedPartyName;
	}

	public void setTranslatedPartyName(final String translatedPartyName) {
		this.translatedPartyName = translatedPartyName;
	}
}
