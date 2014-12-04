package no.evote.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
import no.evote.validation.LettersOrDigits;
import no.evote.validation.StringNotNullEmptyOrBlanks;
import no.valg.eva.admin.common.AreaPath;

/**
 * Polling districts
 */
@Entity
@Table(name = "polling_district", uniqueConstraints = { @UniqueConstraint(columnNames = { "borough_pk", "polling_district_id" }),
		@UniqueConstraint(columnNames = { "borough_pk", "municipality" }) })
@AttributeOverride(name = "pk", column = @Column(name = "polling_district_pk"))
@NamedQueries({
		@NamedQuery(name = "PollingDistrict.findById", query = "SELECT pd FROM PollingDistrict pd WHERE pd.borough.pk = :boroughPk AND pd.id = :id"),
		@NamedQuery(name = "PollingDistrict.findPollingDistrictsForParent", query = "SELECT pd FROM PollingDistrict pd WHERE pd.pollingDistrict.pk = "
				+ ":parentPollingDistrictPk ORDER BY pd.id"),
		@NamedQuery(
				name = "PollingDistrict.findOrdinaryPollingDistrictsForBorough",
				query = "SELECT pd FROM PollingDistrict pd WHERE pd.borough.pk = :boroughPk AND "
						+ "pd.pollingDistrict IS NULL AND pd.parentPollingDistrict IS FALSE "
                        + "AND pd.technicalPollingDistrict IS FALSE AND pd.municipality IS FALSE ORDER BY pd.id"),
		@NamedQuery(name = "PollingDistrict.findPollingDistrictByMunicipalityProxy", query = "SELECT pd FROM Municipality m, Borough b, PollingDistrict pd  "
				+ "WHERE m.pk=:municipalityPk AND m.pk = b.municipality.pk AND b.pk = pd.borough.pk AND pd.municipality = 'true'"),
		@NamedQuery(name = "PollingDistrict.findPollingDistrictsByMunicipalityPk", query = "SELECT pd FROM Municipality m, Borough b, PollingDistrict pd  "
				+ "WHERE m.pk=:municipalityPk AND m.pk = b.municipality.pk AND b.pk = pd.borough.pk"),
		@NamedQuery(
				name = "PollingDistrict.findNotTechnicalPollingDistrictsByMunicipalityPk",
				query = "SELECT pd FROM Municipality m, Borough b, PollingDistrict pd  WHERE m.pk=:municipalityPk AND m.pk = b.municipality.pk "
                        + "AND b.pk = pd.borough.pk AND "
						+ "pd.technicalPollingDistrict IS FALSE AND pd.pollingDistrict IS NULL ORDER BY pd.id"),
		@NamedQuery(name = "PollingDistrict.findCountByBorough", query = "SELECT COUNT(pd) FROM PollingDistrict pd WHERE pd.borough.pk = :boroughPk"),
		@NamedQuery(name = "PollingDistrict.findByBorough", query = "SELECT pd FROM PollingDistrict pd WHERE pd.borough.pk = :boroughPk"),
		@NamedQuery(
				name = "PollingDistrict.findWithoutPollingPlaces",
				query = "SELECT pd FROM PollingDistrict pd, Borough b where "
						+ "pd.borough.pk = b.pk AND b.municipality.pk = :municipalityPk AND pd.parentPollingDistrict IS FALSE AND pd.municipality IS FALSE "
                        + "AND pd.technicalPollingDistrict IS FALSE"
						+ " AND NOT EXISTS (SELECT pp FROM PollingPlace pp WHERE pp.pollingDistrict.pk = pd.pk AND pp.electionDayVoting IS TRUE)"),
		@NamedQuery(name = "PollingDistrict.findByMunicipality", query = "select pd from PollingDistrict pd, Borough b where "
				+ "pd.borough.pk = b.pk and b.municipality.pk = :municipalityPk"),
		@NamedQuery(name = "PollingDistrict.findIsUsingPollingStation", query = "SELECT pp.pollingDistrict " + "FROM PollingPlace pp "
				+ "WHERE pp.usingPollingStations IS TRUE AND pp.electionDayVoting IS TRUE " + "AND EXISTS ( " + "FROM MvArea a " + "WHERE a.pollingPlace = pp "
				+ "AND a.electionEvent.pk = :electionEventPk AND a.municipality.electronicMarkoffs IS FALSE)") })
public class PollingDistrict extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private PollingDistrict pollingDistrict;
	private Set<PollingDistrict> childPollingDistricts;
	private Borough borough;
	private String id;
	private String name;
	private boolean municipality;
	private boolean parentPollingDistrict;
	private boolean childPollingDistrict;
	private boolean technicalPollingDistrict;
	private Set<PollingPlace> pollingPlaces = new HashSet<>();
	
	public PollingDistrict() {
	}

	public PollingDistrict(final String id, final String name, final Borough borough) {
		this.id = id;
		this.name = name;
		this.borough = borough;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_polling_district_pk")
	public PollingDistrict getPollingDistrict() {
		return pollingDistrict;
	}

	public void setPollingDistrict(final PollingDistrict pollingDistrict) {
		this.pollingDistrict = pollingDistrict;
	}

	@OneToMany(mappedBy = "pollingDistrict", cascade = CascadeType.PERSIST)
	public Set<PollingDistrict> getChildPollingDistricts() {
		return childPollingDistricts;
	}

	public void setChildPollingDistricts(final Set<PollingDistrict> childPollingDistricts) {
		this.childPollingDistricts = childPollingDistricts;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "borough_pk", nullable = false)
	@NotNull
	public Borough getBorough() {
		return borough;
	}

	public void setBorough(final Borough borough) {
		this.borough = borough;
	}

	@Column(name = "polling_district_id", nullable = false, length = 4)
	@ID(size = 4)
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "polling_district_name", nullable = false, length = 50)
	@LettersOrDigits
	@StringNotNullEmptyOrBlanks
	@Size(max = 50)
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "municipality", nullable = false)
	public boolean isMunicipality() {
		return municipality;
	}

	public void setMunicipality(final boolean municipality) {
		this.municipality = municipality;
	}

	@Column(name = "parent_polling_district", nullable = false)
	public boolean isParentPollingDistrict() {
		return parentPollingDistrict;
	}

	public void setParentPollingDistrict(final boolean parentPollingDistrict) {
		this.parentPollingDistrict = parentPollingDistrict;
	}

	@OneToMany(mappedBy = "pollingDistrict", fetch = FetchType.LAZY)
	public Set<PollingPlace> getPollingPlaces() {
		return pollingPlaces;
	}

	void setPollingPlaces(Set<PollingPlace> pollingPlaces) {
		this.pollingPlaces = pollingPlaces;
	}


	@Transient
	public void setChildPollingDistrict(final boolean childPollingDistrict) {
		this.childPollingDistrict = childPollingDistrict;
	}

	@Transient
	public boolean isChildPollingDistrict() {
		return childPollingDistrict;
	}

	@Transient
	public boolean isRegularPollingDistrict() {
		return !parentPollingDistrict && !technicalPollingDistrict && !municipality && !childPollingDistrict;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		switch (level) {
		case POLLING_DISTRICT:
			return this.getPk();
		case BOROUGH:
			return borough.getPk();
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
		return AuditLogUtil.generateId(borough, id);
	}

	@Column(name = "technical_polling_district", nullable = false)
	@NotNull
	public boolean isTechnicalPollingDistrict() {
		return technicalPollingDistrict;
	}

	public void setTechnicalPollingDistrict(final boolean technicalPollingDistrict) {
		this.technicalPollingDistrict = technicalPollingDistrict;
	}

	public AreaPath areaPath() {
		return getBorough().areaPath().add(getId());
	}
}
