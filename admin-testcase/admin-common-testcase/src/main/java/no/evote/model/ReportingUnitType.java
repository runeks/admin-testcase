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
 * Types of reporting units, used as templates to create reporting units
 */
@Entity
@Table(name = "reporting_unit_type", uniqueConstraints = @UniqueConstraint(columnNames = "reporting_unit_type_id"))
@AttributeOverride(name = "pk", column = @Column(name = "reporting_unit_type_pk"))
public class ReportingUnitType extends VersionedEntity implements java.io.Serializable {

	private int id;
	private String name;
	private int electionLevel;
	private Integer areaLevel;

	@Column(name = "reporting_unit_type_id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@Column(name = "reporting_unit_type_name", nullable = false, length = 50)
	@StringNotNullEmptyOrBlanks
	@Size(max = 50)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "election_level", nullable = false)
	public int getElectionLevel() {
		return this.electionLevel;
	}

	public void setElectionLevel(final int electionLevel) {
		this.electionLevel = electionLevel;
	}

	@Column(name = "area_level")
	public Integer getAreaLevel() {
		return this.areaLevel;
	}

	public void setAreaLevel(final Integer areaLevel) {
		this.areaLevel = areaLevel;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return String.valueOf(id);
	}
}
