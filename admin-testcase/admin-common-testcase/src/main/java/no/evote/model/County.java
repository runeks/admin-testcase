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
import javax.validation.constraints.Size;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.logging.AuditLogUtil;
import no.evote.security.ContextSecurable;
import no.evote.validation.ID;
import no.evote.validation.Letters;
import no.evote.validation.StringNotNullEmptyOrBlanks;
import no.valg.eva.admin.common.AreaPath;

/**
 * Counties
 */
@Entity
@Table(name = "county", uniqueConstraints = @UniqueConstraint(columnNames = { "country_pk", "county_id" }))
@NamedQueries({ @NamedQuery(name = "County.findById", query = "SELECT c FROM County c WHERE c.country.pk = :countryPk AND c.id = :id"),
		@NamedQuery(name = "County.findByMunicipality", query = "select m.county from Municipality m where m.pk = :municipalityPk"),
		@NamedQuery(name = "Counties.findByCountry", query = "SELECT c FROM County c WHERE c.country.pk = :countryPk"),
		@NamedQuery(name = "Counties.findCountByCountry", query = "SELECT COUNT(c) FROM County c WHERE c.country.pk = :countryPk") })
@AttributeOverride(name = "pk", column = @Column(name = "county_pk"))
public class County extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private Country country;
	private String id;
	private String name;

	public County() {
	}

	public County(final String id, final String name, final Country country) {
		this.id = id;
		this.name = name;
		this.country = country;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_pk", nullable = false)
	@NotNull
	public Country getCountry() {
		return this.country;
	}

	public void setCountry(final Country country) {
		this.country = country;
	}

	@Column(name = "county_id", nullable = false, length = 2)
	@ID(size = 2)
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "county_name", nullable = false, length = 50)
	@Letters(extraChars = "-. ")
	@StringNotNullEmptyOrBlanks
	@Size(max = 50)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof County)) {
			return false;
		}
		County other = (County) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		switch (level) {
		case COUNTRY:
			return country.getPk();
		case COUNTY:
			return getPk();
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
		return AuditLogUtil.generateId(country, id);
	}

	public AreaPath areaPath() {
		return getCountry().areaPath().add(getId());
	}
}
