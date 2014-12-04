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

import no.evote.constants.SQLConstants;
import no.evote.logging.AuditLogUtil;
import no.evote.validation.StringNotNullEmptyOrBlanks;

/**
 * Postal codes from http://epab.posten.no/Norsk/Nedlasting/NedlastingMeny.htm
 */
@Entity
@Table(name = "postal_code", uniqueConstraints = @UniqueConstraint(columnNames = { SQLConstants.ELECTION_EVENT_PK, "postal_code_id" }))
@AttributeOverride(name = "pk", column = @Column(name = "postal_code_pk"))
@NamedQueries({ @NamedQuery(name = "PostalCode.findByIdAndElectionEvent", query = "select p from PostalCode p where p.id = :postalCodeId"
		+ " AND p.electionEvent.pk = :electionEventPk") })
public class PostalCode extends VersionedEntity implements java.io.Serializable {

	private PostalCodeCategory postalCodeCategory;
	private ElectionEvent electionEvent;
	private Municipality municipality;
	private String id;
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "postal_code_category_pk", nullable = false)
	public PostalCodeCategory getPostalCodeCategory() {
		return this.postalCodeCategory;
	}

	public void setPostalCodeCategory(final PostalCodeCategory postalCodeCategory) {
		this.postalCodeCategory = postalCodeCategory;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = SQLConstants.ELECTION_EVENT_PK, nullable = false)
	public ElectionEvent getElectionEvent() {
		return this.electionEvent;
	}

	public void setElectionEvent(final ElectionEvent electionEvent) {
		this.electionEvent = electionEvent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "municipality_pk", nullable = false)
	public Municipality getMunicipality() {
		return this.municipality;
	}

	public void setMunicipality(final Municipality municipality) {
		this.municipality = municipality;
	}

	@Column(name = "postal_code_id", nullable = false, length = 4)
	@StringNotNullEmptyOrBlanks
	@Size(max = 4)
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "postal_code_name", nullable = false, length = 50)
	@StringNotNullEmptyOrBlanks
	@Size(max = 50)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public boolean equals(final Object other) {
		return other instanceof PostalCode && this.getId() != null && this.getId().equals(((PostalCode) other).getId());
	}

	@Override
	public int hashCode() {
		return this.getId() != null ? this.getClass().hashCode() + this.getId().hashCode() : super.hashCode();
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(electionEvent, id);
	}
}
