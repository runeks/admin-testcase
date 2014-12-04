package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import no.evote.validation.StringNotNullEmptyOrBlanks;

/**
 * Postal code categories from http://epab.posten.no/Norsk/Nedlasting/NedlastingMeny.htm
 */
@Entity
@Table(name = "postal_code_category", uniqueConstraints = @UniqueConstraint(columnNames = "postal_code_category_id"))
@AttributeOverride(name = "pk", column = @Column(name = "postal_code_category_pk"))
public class PostalCodeCategory extends VersionedEntity implements java.io.Serializable {

	private char id;
	private String name;

	@Column(name = "postal_code_category_id", nullable = false, length = 1)
	public char getId() {
		return this.id;
	}

	public void setId(final char id) {
		this.id = id;
	}

	@Column(name = "postal_code_category_name", nullable = false, length = 50)
	@StringNotNullEmptyOrBlanks
	@Size(max = 50)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return String.valueOf(id);
	}
}
