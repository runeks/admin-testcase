package no.evote.ssb2013;

import java.util.HashSet;
import java.util.Set;

/**
 * Contains municipalities and group of municipalities reporting at polling district level.
 */
public final class Municipalities {

	public static final String OSLO = "0301";
	public static final String BERGEN = "1201";
	public static final String TRONDHEIM = "1601";
	public static final String STAVANGER = "1103";

	private Municipalities() {

	}

	private static Set<String> municipalitiesReportingAtPollingDistrictLevel = new HashSet<>();
	static {
		municipalitiesReportingAtPollingDistrictLevel.add(OSLO);
		municipalitiesReportingAtPollingDistrictLevel.add(BERGEN);
		municipalitiesReportingAtPollingDistrictLevel.add(TRONDHEIM);
		municipalitiesReportingAtPollingDistrictLevel.add(STAVANGER);
	}

	/**
	 * @param municipalityId
	 *            id of municipality
	 * @return true if municipality reports at polling district level (kretsvis), else false
	 */
	public static boolean reportsAtPollingDistrictLevel(final String municipalityId) {
		return municipalitiesReportingAtPollingDistrictLevel.contains(municipalityId);
	}
}
