package no.evote.model;

import static java.lang.String.format;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.constants.SQLConstants;
import no.evote.exception.ValidateException;
import no.evote.security.ContextSecurableDynamicArea;
import no.evote.util.Treeable;
import no.evote.validation.StringNotNullEmptyOrBlanks;
import no.valg.eva.admin.common.Area;
import no.valg.eva.admin.common.AreaPath;

/**
 * Materialized view containing all nodes in the hierarchy formed by the tables country, county, municipality, borough, polling_district, and polling_place,
 * facilitating RBAC access to any level in the hierarchy via a single field/pointer
 */
@Entity
@Table(name = "mv_area", uniqueConstraints = { @UniqueConstraint(columnNames = { SQLConstants.ELECTION_EVENT_PK, "country_pk", "county_pk", "municipality_pk",
		"borough_pk", "polling_district_pk", "polling_place_pk" }) })
@AttributeOverride(name = "pk", column = @Column(name = "mv_area_pk"))
@NamedQueries({
		@NamedQuery(name = "MvArea.findRoot", query = "SELECT mva FROM MvArea mva WHERE mva.country IS NULL AND " + "mva.electionEvent.pk = :eepk"),
		@NamedQuery(name = "MvArea.findByPath", query = "SELECT mva FROM MvArea mva WHERE mva.areaPath = :path"),
		@NamedQuery(name = "MvArea.findByPollingDistrict", query = "SELECT mva.pk FROM MvArea mva WHERE mva.areaLevel = 5 AND "
				+ "mva.pollingDistrict.pk = :pollingDistrictPk"),
		@NamedQuery(name = MvArea.FIND_BY_POLLING_DISTRICT_ID_AND_MUNICIPALITY_PK, query = "SELECT mva FROM MvArea mva WHERE mva.areaLevel = 5 AND "
				+ "mva.pollingDistrict.id = :pollingDistrictId AND " + "mva.municipality.pk = :municipalityPk") ,
		@NamedQuery(name = MvArea.FIND_BY_POLLING_PLACE_ID_AND_MUNICIPALITY_PK,
                query = "SELECT mva FROM MvArea mva WHERE mva.areaLevel = 6 AND mva.pollingDistrict.id = '0000' AND "
				+ "mva.pollingPlace.id = :pollingPlaceId AND " + "mva.municipality.pk = :municipalityPk")  })
@NamedNativeQueries({
		@NamedNativeQuery(name = MvArea.NAMED_NATIVE_QUERY_FIND_BY_PATH_AND_LEVEL, query = "SELECT * FROM mv_area mva"
				+ " WHERE text2ltree(mva.area_path) <@ text2ltree(?1) AND mva.area_level = ?2 ORDER BY mva.area_path", resultClass = MvArea.class),
		@NamedNativeQuery(name = MvArea.NAMED_NATIVE_QUERY_FIND_FIRST_BY_PATH_AND_LEVEL, query = "SELECT * FROM mv_area mva"
				+ " WHERE text2ltree(mva.area_path) <@ text2ltree(?1) AND mva.area_level = ?2 ORDER BY mva.area_path limit 1", resultClass = MvArea.class),
		@NamedNativeQuery(name = MvArea.NAMED_NATIVE_QUERY_FIND_BY_PATH_AND_LEVELS_LE_LEVEL, query = "SELECT * FROM mv_area mva "
				+ "WHERE text2ltree(mva.area_path) <@ text2ltree(?1) AND mva.area_level <= ?2 ORDER BY mva.area_path", resultClass = MvArea.class),
		@NamedNativeQuery(name = MvArea.HAS_ACCESS_TO_PK_ON_LEVEL, query = "select ra.* from mv_area ra join mv_area oa on (text2ltree(oa.area_path) <@"
				+ " text2ltree(ra.area_path) and oa.area_level = ?1 and case when ?1 = 0 then oa.election_event_pk when ?1 = 1 then oa.country_pk"
				+ " when ?1 = 2 then oa.county_pk when ?1 = 3 then oa.municipality_pk when ?1 = 4 then oa.borough_pk when ?1 = 5 then oa.polling_district_pk"
				+ " when ?1 = 6 then oa.polling_place_pk when ?1 = 7 then oa.polling_station_pk end = ?2) where ra.area_path = ?3", resultClass = MvArea.class) })
public class MvArea extends BaseEntity implements Serializable, Treeable, ContextSecurableDynamicArea {
	public static final String NAMED_NATIVE_QUERY_FIND_BY_PATH_AND_LEVEL = "MvArea.findByPathAndLevel";
	public static final String NAMED_NATIVE_QUERY_FIND_FIRST_BY_PATH_AND_LEVEL = "MvArea.findFirstByPathAndLevel";
	public static final String HAS_ACCESS_TO_PK_ON_LEVEL = "MvArea.hasAccessToPkOnLevel";
	public static final String NAMED_NATIVE_QUERY_FIND_BY_PATH_AND_LEVELS_LE_LEVEL = "MvArea.findByPathAndLevelsLELevel";
	public static final String FIND_BY_POLLING_DISTRICT_ID_AND_MUNICIPALITY_PK = "MvArea.findByPollingDistrictIdAndMunicipalityPk";
	public static final String FIND_BY_POLLING_PLACE_ID_AND_MUNICIPALITY_PK = "MvArea.findByPollingPlaceIdAndMunicipalityPk";
	private static final long serialVersionUID = 4747543654495970865L;
	private static final String ROOT = "Root";
	
	private PollingDistrict pollingDistrictByParentPollingDistrictPk;
	private PollingDistrict pollingDistrict;
	private ElectionEvent electionEvent;
	private Country country;
	private County county;
	private Municipality municipality;
	private Borough borough;
	private PollingPlace pollingPlace;
	private String areaPath;
	private int areaLevel;
	private String electionEventId;
	private String countryId;
	private String countyId;
	private String municipalityId;
	private String boroughId;
	private String pollingDistrictId;
	private String pollingPlaceId;
	private String electionEventName;
	private String countryName;
	private String countyName;
	private String municipalityName;
	private String boroughName;
	private String pollingDistrictName;
	private String pollingPlaceName;
	private Boolean parentPollingDistrict;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_polling_district_pk")
	public PollingDistrict getPollingDistrictByParentPollingDistrictPk() {
		return this.pollingDistrictByParentPollingDistrictPk;
	}

	public void setPollingDistrictByParentPollingDistrictPk(final PollingDistrict pollingDistrictByParentPollingDistrictPk) {
		this.pollingDistrictByParentPollingDistrictPk = pollingDistrictByParentPollingDistrictPk;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "polling_district_pk")
	public PollingDistrict getPollingDistrict() {
		return this.pollingDistrict;
	}

	public void setPollingDistrict(final PollingDistrict pollingDistrict) {
		this.pollingDistrict = pollingDistrict;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = SQLConstants.ELECTION_EVENT_PK, nullable = false)
	public ElectionEvent getElectionEvent() {
		return this.electionEvent;
	}

	public void setElectionEvent(final ElectionEvent electionEvent) {
		this.electionEvent = electionEvent;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country_pk")
	public Country getCountry() {
		return this.country;
	}

	public void setCountry(final Country country) {
		this.country = country;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "county_pk")
	public County getCounty() {
		return this.county;
	}

	public void setCounty(final County county) {
		this.county = county;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "municipality_pk")
	public Municipality getMunicipality() {
		return this.municipality;
	}
	
	public void setMunicipality(final Municipality municipality) {
		this.municipality = municipality;
	}

	public boolean hasMunicipalityElectronicMarkOffs() {
		return this.municipality.isElectronicMarkoffs();
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "borough_pk")
	public Borough getBorough() {
		return this.borough;
	}

	public void setBorough(final Borough borough) {
		this.borough = borough;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "polling_place_pk")
	public PollingPlace getPollingPlace() {
		return this.pollingPlace;
	}

	public void setPollingPlace(final PollingPlace pollingPlace) {
		this.pollingPlace = pollingPlace;
	}

	@Column(name = "area_path", nullable = false, length = 37)
	@StringNotNullEmptyOrBlanks
	@Size(max = 37)
	public String getAreaPath() {
		return this.areaPath;
	}

	public void setAreaPath(final String areaPath) {
		this.areaPath = areaPath;
	}

	@Column(name = "area_level", nullable = false)
	public int getAreaLevel() {
		return this.areaLevel;
	}

	public void setAreaLevel(final int areaLevel) {
		this.areaLevel = areaLevel;
	}

	@Column(name = "election_event_id", nullable = false, length = 6)
	@StringNotNullEmptyOrBlanks
	@Size(max = 6)
	public String getElectionEventId() {
		return this.electionEventId;
	}

	public void setElectionEventId(final String electionEventId) {
		this.electionEventId = electionEventId;
	}

	@Column(name = "country_id", length = 2)
	public String getCountryId() {
		return this.countryId;
	}

	public void setCountryId(final String countryId) {
		this.countryId = countryId;
	}

	@Column(name = "county_id", length = 2)
	public String getCountyId() {
		return this.countyId;
	}

	public void setCountyId(final String countyId) {
		this.countyId = countyId;
	}

	@Column(name = "municipality_id", length = 4)
	public String getMunicipalityId() {
		return this.municipalityId;
	}

	public void setMunicipalityId(final String municipalityId) {
		this.municipalityId = municipalityId;
	}

	@Column(name = "borough_id", length = 6)
	public String getBoroughId() {
		return this.boroughId;
	}

	public void setBoroughId(final String boroughId) {
		this.boroughId = boroughId;
	}

	@Column(name = "polling_district_id", length = 4)
	public String getPollingDistrictId() {
		return this.pollingDistrictId;
	}

	public void setPollingDistrictId(final String pollingDistrictId) {
		this.pollingDistrictId = pollingDistrictId;
	}

	@Column(name = "polling_place_id", length = 4)
	public String getPollingPlaceId() {
		return this.pollingPlaceId;
	}

	public void setPollingPlaceId(final String pollingPlaceId) {
		this.pollingPlaceId = pollingPlaceId;
	}

	@Column(name = "election_event_name", nullable = false, length = 100)
	@StringNotNullEmptyOrBlanks
	@Size(max = 50)
	public String getElectionEventName() {
		return this.electionEventName;
	}

	public void setElectionEventName(final String electionEventName) {
		this.electionEventName = electionEventName;
	}

	@Column(name = "country_name", length = 50)
	@Size(max = 50)
	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(final String countryName) {
		this.countryName = countryName;
	}

	@Column(name = "county_name", length = 50)
	@Size(max = 50)
	public String getCountyName() {
		return this.countyName;
	}

	public void setCountyName(final String countyName) {
		this.countyName = countyName;
	}

	@Column(name = "municipality_name", length = 50)
	@Size(max = 50)
	public String getMunicipalityName() {
		return this.municipalityName;
	}

	public void setMunicipalityName(final String municipalityName) {
		this.municipalityName = municipalityName;
	}

	@Column(name = "borough_name", length = 50)
	@Size(max = 50)
	public String getBoroughName() {
		return this.boroughName;
	}

	public void setBoroughName(final String boroughName) {
		this.boroughName = boroughName;
	}

	@Column(name = "polling_district_name", length = 50)
	@Size(max = 50)
	public String getPollingDistrictName() {
		return this.pollingDistrictName;
	}

	public void setPollingDistrictName(final String pollingDistrictName) {
		this.pollingDistrictName = pollingDistrictName;
	}

	@Column(name = "polling_place_name", length = 50)
	@Size(max = 50)
	public String getPollingPlaceName() {
		return this.pollingPlaceName;
	}

	public void setPollingPlaceName(final String pollingPlaceName) {
		this.pollingPlaceName = pollingPlaceName;
	}

	@Column(name = "parent_polling_district")
	public Boolean getParentPollingDistrict() {
		return this.parentPollingDistrict;
	}

	@Transient
	public boolean isParentPollingDistrict() {
		return getParentPollingDistrict() != null && getParentPollingDistrict();
	}

	public void setParentPollingDistrict(final Boolean parentPollingDistrict) {
		this.parentPollingDistrict = parentPollingDistrict;
	}

	@Transient
	public boolean isChildPollingDistrict() {
		return getPollingDistrictByParentPollingDistrictPk() != null;
	}
	
	@Override
	@Transient
	public String getPath() {
		return areaPath;
	}

	@Transient
	public BaseEntity getLeafArea() {
		AreaLevelEnum areaLevelEnum = AreaLevelEnum.getLevel(areaLevel);
		switch (areaLevelEnum) {
		case ROOT:
			return electionEvent;
		case COUNTRY:
			return country;
		case COUNTY:
			return county;
		case MUNICIPALITY:
			return municipality;
		case BOROUGH:
			return borough;
		case POLLING_DISTRICT:
			return pollingDistrict;
		case POLLING_PLACE:
			return pollingPlace;
		default:
			return null;
		}
	}

	@Transient
	public boolean isMunicipalityLevel() {
		return areaLevel == AreaLevelEnum.MUNICIPALITY.getLevel();
	}

	@Transient
	public boolean isPollingDistrictLevel() {
		return areaLevel == AreaLevelEnum.POLLING_DISTRICT.getLevel();
	}

	@Transient
	public String getAreaLevelString() {
		return "@area_level[" + areaLevel + "].name";
	}

	@Override
	public String toString() {
		return getAreaName();
	}

	@Transient
	public String getAreaName() {
		switch (getActualAreaLevel()) {
			case COUNTRY:
				return countryName;
			case COUNTY:
				return countyName;
			case MUNICIPALITY:
				return municipalityName;
			case BOROUGH:
				return boroughName;
			case POLLING_DISTRICT:
				return pollingDistrictName;
			case POLLING_PLACE:
				return pollingPlaceName;
			default:
				return electionEventName;
		}
	}

	public String id() {
		AreaLevelEnum areaLevelEnum = AreaLevelEnum.getLevel(areaLevel);
		switch (areaLevelEnum) {
		case ROOT:
			return ROOT;
		case COUNTRY:
			return countryId;
		case COUNTY:
			return countyId;
		case MUNICIPALITY:
			return municipalityId;
		case BOROUGH:
			return boroughId;
		case POLLING_DISTRICT:
			return pollingDistrictId;
		case POLLING_PLACE:
			return pollingPlaceId;
		default:
			return ROOT;
		}
	}

	@Transient
	public String getIdAndNamePath() {
		StringBuffer path = new StringBuffer();
		if (country != null) {
			path = new StringBuffer(countryId).append(' ').append(countryName);
		}
		if (county != null) {
			path = new StringBuffer(countyId).append(' ').append(countyName);
		}
		if (municipality != null) {
			path.append(", ");
			path.append(municipalityId).append(' ').append(municipalityName);
		}
		if (borough != null) {
			path.append(", ");
			path.append(boroughId).append(' ').append(boroughName);
		}
		if (pollingDistrict != null) {
			path.append(", ");
			path.append(pollingDistrictId).append(' ').append(pollingDistrictName);
		}
		if (pollingPlace != null) {
			path.append(", ");
			path.append(pollingPlaceId).append(' ').append(pollingPlaceName);
		}
		return path.toString();
	}

	@Transient
	public String getNamedPath() {
		StringBuffer path = new StringBuffer();
		if (country != null) {
			path = new StringBuffer(countryName);
		}
		if (county != null) {
			path = new StringBuffer(countyName);
		}
		if (municipality != null) {
			path.append('.');
			path.append(municipalityName);
		}
		if (borough != null) {
			path.append('.');
			path.append(boroughName);
		}
		if (pollingDistrict != null) {
			path.append('.');
			path.append(pollingDistrictName);
		}
		if (pollingPlace != null) {
			path.append('.');
			path.append(pollingPlaceName);
		}
		return path.toString();
	}

	public Long getPkForLevel(final int level) {
		Long pk = null;
		AreaLevelEnum areaLevelEnum = AreaLevelEnum.getLevel(level);
		switch (areaLevelEnum) {
		case ROOT:
			if (electionEvent != null) {
				pk = electionEvent.getPk();
			}
			break;
		case COUNTRY:
			if (country != null) {
				pk = country.getPk();
			}
			break;
		case COUNTY:
			if (county != null) {
				pk = county.getPk();
			}
			break;
		case MUNICIPALITY:
			if (municipality != null) {
				pk = municipality.getPk();
			}
			break;
		case BOROUGH:
			if (borough != null) {
				pk = borough.getPk();
			}
			break;
		case POLLING_DISTRICT:
			if (pollingDistrict != null) {
				pk = pollingDistrict.getPk();
			}
			break;
		case POLLING_PLACE:
			if (pollingPlace != null) {
				pk = pollingPlace.getPk();
			}
			break;
		default:
			return null;
		}

		return pk;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		if (level.equals(AreaLevelEnum.ROOT)) {
			return getElectionEvent().getPk();
		} else if (level.equals(AreaLevelEnum.COUNTRY)) {
			return getCountry().getPk();
		} else if (level.equals(AreaLevelEnum.COUNTY)) {
			return getCounty().getPk();
		} else if (level.equals(AreaLevelEnum.MUNICIPALITY)) {
			return getMunicipality().getPk();
		} else if (level.equals(AreaLevelEnum.BOROUGH)) {
			return getBorough().getPk();
		} else if (level.equals(AreaLevelEnum.POLLING_DISTRICT)) {
			return getPollingDistrict().getPk();
		} else if (level.equals(AreaLevelEnum.POLLING_PLACE)) {
			return getPollingPlace().getPk();
		}
		return null;
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		return null;
	}

	@Override
	@Transient
	public AreaLevelEnum getActualAreaLevel() {
		return AreaLevelEnum.getLevel(areaLevel);
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return areaPath;
	}

    public void validateAreaLevel(final AreaLevelEnum areaLevelEnum) {
        if (getActualAreaLevel() != areaLevelEnum) {
            throw new ValidateException(format(
                    "expected the MvArea area level to be <%s>, but was <%s>",
                    areaLevelEnum,
                    getActualAreaLevel()));
        }
    }

	public Area toViewObject() {
		return new Area(AreaPath.from(areaPath), getAreaName());
	}
}
