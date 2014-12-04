package no.evote.constants;

public enum AreaLevelEnum {
	NONE(-1),
	ROOT(0),
	COUNTRY(1),
	COUNTY(2),
	MUNICIPALITY(3),
	BOROUGH(4),
	POLLING_DISTRICT(5),
	POLLING_PLACE(6),
	POLLING_STATION(7);

	private final int level;

	AreaLevelEnum(final int level) {
		this.level = level;
	}

	public static AreaLevelEnum getLevel(final int level) {
		if (level == ROOT.level) {
			return ROOT;
		}
		if (level == COUNTRY.level) {
			return COUNTRY;
		}
		if (level == COUNTY.level) {
			return COUNTY;
		}
		if (level == MUNICIPALITY.level) {
			return MUNICIPALITY;
		}
		if (level == BOROUGH.level) {
			return BOROUGH;
		}
		if (level == POLLING_DISTRICT.level) {
			return POLLING_DISTRICT;
		}
		if (level == POLLING_PLACE.level) {
			return POLLING_PLACE;
		}
		if (level == POLLING_STATION.level) {
			return POLLING_STATION;
		}

		return NONE;
	}

	public int getLevel() {
		return level;
	}

	public String getName() {
		return "@area_level[" + level + "].name";
	}

	public boolean lowerThan(AreaLevelEnum level) {
		return this.getLevel() > level.getLevel();
	}

	public String getLevelDescription() {
		return name().replace('_', ' ').toLowerCase();
	}
}
