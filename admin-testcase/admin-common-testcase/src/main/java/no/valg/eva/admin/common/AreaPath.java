package no.valg.eva.admin.common;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.join;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.evote.constants.AreaLevelEnum;

/**
 * Value object for area path. EEVENT.CO.CT.MUNI.... where EEVENT is election event, CO country, CT county and MUNI municipality.
 */
public class AreaPath implements Serializable {
	public static final String MUNICIPALITY_POLLING_DISTRICT_ID = "0000";

	public static final String OSLO_COUNTY_ID = "03";
    public static final String OSLO_MUNICIPALITY_ID = "0301";

	public static final String BOROUGH_ID_GAMLE_OSLO = "030101";
	public static final String BOROUGH_ID_GRUNERLOKKA = "030102";
	public static final String BOROUGH_ID_SAGENE = "030103";
	public static final String BOROUGH_ID_ST_HANSHAUGEN = "030104";
	public static final String BOROUGH_ID_FROGNER = "030105";
	public static final String BOROUGH_ID_ULLERN = "030106";
	public static final String BOROUGH_ID_VESTRE_AKER = "030107";
	public static final String BOROUGH_ID_NORDRE_AKER = "030108";
	public static final String BOROUGH_ID_BJERKE = "030109";
	public static final String BOROUGH_ID_GRORUD = "030110";
	public static final String BOROUGH_ID_STOVNER = "030111";
	public static final String BOROUGH_ID_ALNA = "030112";
	public static final String BOROUGH_ID_OSTENSJO = "030113";
	public static final String BOROUGH_ID_NORDSTRAND = "030114";
	public static final String BOROUGH_ID_SONDRE_NORDSTRAND = "030115";
	
	public static final String AREA_PATH_REGEX = "^(\\d{6})(\\.(\\d{2})(\\.(\\d{2})(\\.(\\d{4})(\\.(\\d{6})(\\.(\\d{4})(\\.(\\d{4})(\\.(\\d{2}))?)?)?)?)?)?)?$";
	private static final Pattern AREA_PATH_PATTERN = Pattern.compile(AREA_PATH_REGEX);
	private final String path;
	private final AreaLevelEnum level;
	private final String electionEventId;
	private final String countryId;
	private final String countyId;
	private final String municipalityId;
	private final String boroughId;
	private final String pollingDistrictId;
	private final String pollingPlaceId;
	private final String pollingStationId;

	public AreaPath(final String path) {
		this.path = path;

		Matcher pathMatcher = AREA_PATH_PATTERN.matcher(path);
		if (!pathMatcher.matches()) {
			throw new IllegalArgumentException(format("illegal path <%s>, must match <%s>", path, AREA_PATH_PATTERN));
		}

		// regex groups
		// CSOFF: MagicNumber
		int electionEvent = 1;
		int country = 3;
		int county = 5;
		int municipality = 7;
		int borough = 9;
		int pollingDistrict = 11;
		int pollingPlace = 13;
		int pollingStation = 15;
		// CSON: MagicNumber

		electionEventId = pathMatcher.group(electionEvent);
		AreaLevelEnum level = AreaLevelEnum.ROOT;
		countryId = pathMatcher.group(country);
		if (countryId != null) {
			level = AreaLevelEnum.COUNTRY;
		}
		countyId = pathMatcher.group(county);
		if (countyId != null) {
			level = AreaLevelEnum.COUNTY;
		}
		municipalityId = pathMatcher.group(municipality);
		if (municipalityId != null) {
			level = AreaLevelEnum.MUNICIPALITY;
		}
		boroughId = pathMatcher.group(borough);
		if (boroughId != null) {
			level = AreaLevelEnum.BOROUGH;
		}
		pollingDistrictId = pathMatcher.group(pollingDistrict);
		if (pollingDistrictId != null) {
			level = AreaLevelEnum.POLLING_DISTRICT;
		}
		pollingPlaceId = pathMatcher.group(pollingPlace);
		if (pollingPlaceId != null) {
			level = AreaLevelEnum.POLLING_PLACE;
		}
		pollingStationId = pathMatcher.group(pollingStation);
		if (pollingStationId != null) {
			level = AreaLevelEnum.POLLING_STATION;
		}
		this.level = level;
	}

	public static AreaPath from(final String areaPath) {
		return new AreaPath(areaPath);
	}

	public String path() {
		return path;
	}

	public AreaLevelEnum getLevel() {
		return level;
	}

	public String getElectionEventId() {
		return electionEventId;
	}

	public String getCountryId() {
		return countryId;
	}

	public String getCountyId() {
		return countyId;
	}

	public String getMunicipalityId() {
		return municipalityId;
	}

	public String getBoroughId() {
		return boroughId;
	}

	public String getPollingDistrictId() {
		return pollingDistrictId;
	}

	public String getPollingPlaceId() {
		return pollingPlaceId;
	}

	public String getPollingStationId() {
		return pollingStationId;
	}

	/**
	 * @return true if this contains subPath, that is this is a parent of subPath
	 */
	public boolean contains(final AreaPath subPath) {
		return subPath.path.startsWith(path);
	}

	/**
	 * @return true if this is part of parentPath
	 */
	public boolean isSubpathOf(final AreaPath parentPath) {
		return path.startsWith(parentPath.path);
	}

	/**
	 * @return county path of this, illegalState if not municipality or below
	 */
	public AreaPath toCountyPath() {
		if (countyId == null) {
			throw new IllegalStateException(format("path to county can not be found for <%s>", path));
		}
		return from(path(electionEventId, countryId, countyId));
	}

	/**
	 * @return municipality path of this, illegalState if not municipality or below
	 */
	public AreaPath toMunicipalityPath() {
		if (municipalityId == null) {
			throw new IllegalStateException(format("path to municipality can not be found for <%s>", path));
		}
		return from(path(electionEventId, countryId, countyId, municipalityId));
	}

	public AreaPath toBoroughPath() {
		if (boroughId == null) {
			throw new IllegalStateException(format("path to borough can not be found for <%s>", path));
		}
		return from(path(electionEventId, countryId, countyId, municipalityId, boroughId));
	}

	public AreaPath toBoroughSubPath(String boroughId) {
		if (municipalityId == null) {
			throw new IllegalStateException(format("path to municipality cannot be found for <%s>", path));
		}
		if (boroughId == null) {
			throw new IllegalArgumentException("borough id cannot be null");
		}
		return from(path(electionEventId, countryId, countyId, municipalityId, boroughId));
	}

	public AreaPath toPollingDistrictPath() {
		if (pollingDistrictId == null) {
			throw new IllegalStateException(format("path to polling district can not be found for <%s>", path));
		}
		return from(path(electionEventId, countryId, countyId, municipalityId, boroughId, pollingDistrictId));
	}

	public AreaPath toPollingDistrictSubPath(String pollingDistrictId) {
	if (boroughId == null) {
			throw new IllegalStateException(format("path to borough cannot be found for <%s>", path));
		}
		if (pollingDistrictId == null) {
			throw new IllegalArgumentException("polling district id cannot be null");
		}
		return from(path(electionEventId, countryId, countyId, municipalityId, boroughId, pollingDistrictId));
	}

	public AreaPath toMunicipalityPollingDistrictPath() {
		String boroughId = municipalityId + "00";
		String pollingDistrictId = MUNICIPALITY_POLLING_DISTRICT_ID;
		return toBoroughSubPath(boroughId).toPollingDistrictSubPath(pollingDistrictId);
	}

	public void validateAreaPath(AreaLevelEnum first, AreaLevelEnum... rest) {
		EnumSet<AreaLevelEnum> validLevels = EnumSet.of(first, rest);
		AreaLevelEnum level = getLevel();
		if (!validLevels.contains(level)) {
			throw new IllegalArgumentException(format(
					"expected area path to be one of <%s>, but was <%s>",
					validLevels,
					level));
		}
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AreaPath)) {
			return false;
		}
		AreaPath areaPath = (AreaPath) o;
		return path.equals(areaPath.path);
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

	@Override
	public String toString() {
		return path;
	}

	public AreaPath add(String id) {
		return AreaPath.from(path() + "." + id);
	}

	public AreaPath getParentPath() {
		return new AreaPath(path.replaceAll("\\.[0-9]+$", ""));
	}

	public boolean isMunicipalityPollingDistrict() {
		return "0000".equals(pollingDistrictId);
	}

    public AreaPath toAreaLevelPath(AreaLevelEnum areaLevelEnum) {
        switch (areaLevelEnum) {
			case ROOT:
				return toRootPath();
			case COUNTRY:
				return toCountryPath();
            case COUNTY:
                return toCountyPath();
            case MUNICIPALITY:
                return toMunicipalityPath();
            case BOROUGH:
                return toBoroughPath();
            case POLLING_DISTRICT:
                return toPollingDistrictPath();
			case POLLING_PLACE:
				return toPollingPlacePath();
            case POLLING_STATION:
                return toPollingStationPath();
            default:
                throw new IllegalArgumentException(format("unsupported area level: <%s>", areaLevelEnum));
        }
    }

	private AreaPath toPollingPlacePath() {
		if (pollingStationId == null) {
			throw new IllegalStateException(format("path to polling place can not be found for <%s>", path));
		}
		return from(path(electionEventId, countryId, countyId, municipalityId, boroughId, pollingDistrictId, pollingPlaceId));
	}

	private AreaPath toPollingStationPath() {
		if (pollingStationId == null) {
			throw new IllegalStateException(format("path to polling station can not be found for <%s>", path));
		}
		return from(path(electionEventId, countryId, countyId, municipalityId, boroughId, pollingDistrictId, pollingPlaceId, pollingStationId));
	}

	private AreaPath toCountryPath() {
		if (countryId == null) {
			throw new IllegalStateException(format("path to country can not be found for <%s>", path));
		}
		return from(path(electionEventId, countryId));
	}

	private AreaPath toRootPath() {
		return from(electionEventId);
	}

	public boolean isTechnicalPollingDistrict() {
		if (getLevel() != AreaLevelEnum.POLLING_DISTRICT) {
            return false;
        }
        if (!boroughId.equals(municipalityId + "00")) {
            return false;
        }
        if (isMunicipalityPollingDistrict()) {
            return false;
        }
        return true;
	}

	private String path(String... pathElements) {
		return join(pathElements, '.');
	}


}
