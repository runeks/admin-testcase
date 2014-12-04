package no.valg.eva.admin.common.counting.model.countingoverview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.ElectionPath;
import no.valg.eva.admin.common.counting.constants.CountingMode;
import no.valg.eva.admin.common.counting.model.CountCategory;
import no.valg.eva.admin.common.counting.model.CountQualifier;

/**
 * Overview of count statuses for each count category, for each polling district if category is VO (valgting ordinære),
 * for area (borough or municipality) for other categories.
 */
public class AreaOverview {

	private final String areaName;
	private final AreaPath areaPath;
	private final ElectionPath electionPath;
	private final Map<CountCategory, CountingMode> countingModeForCategory;
    private final boolean requiredProtocolCount;

	private boolean childPollingDistricts;
	private List<PartialCountingOverview> partialCountingOverviews = new ArrayList<>();

	public AreaOverview(String areaName, AreaPath areaPath, ElectionPath electionPath, Map<CountCategory, CountingMode> countingModeForCategory,
						boolean requiredProtocolCount) {
		this.areaName = areaName;
		this.areaPath = areaPath;
		this.electionPath = electionPath;
        this.countingModeForCategory = countingModeForCategory;
        this.requiredProtocolCount = requiredProtocolCount;
    }

	public void add(PartialCountingOverview partialCountingOverview) {
		childPollingDistricts |= partialCountingOverview.isChildPollingDistrict();
		partialCountingOverviews.add(partialCountingOverview);
	}

    public void addAll(Collection<PartialCountingOverview> partialCountingOverviews) {
		for (PartialCountingOverview partialCountingOverview : partialCountingOverviews) {
			add(partialCountingOverview);
		}
	}

	/**
	 * Takes a list of parent instances and aggregates children onto unique parents which are added to this.
	 * @param partialCountingOverviews instances that are not child instances, that is parents or "normal" instances.
	 */
	public void addAndAggregateParents(Collection<PartialCountingOverview> partialCountingOverviews) {

        Map<AreaPath, PartialCountingOverview> parentMap = new HashMap<>();

        for (PartialCountingOverview partialCountingOverview : partialCountingOverviews) {
			if (partialCountingOverview.isChildPollingDistrict()) {
				throw new IllegalArgumentException(
						"implementation error: method only supports aggregation of parent nodes and nodes that does not represent child polling districts");
			}
			if (partialCountingOverview.isParent()) {
				PartialCountingOverview parent = aggregateChildren(parentMap, partialCountingOverview);
                parentMap.put(parent.getAreaPath(), parent);
            } else {
                add(partialCountingOverview);
            }
		}
        addAll(parentMap.values());
	}

	private PartialCountingOverview aggregateChildren(Map<AreaPath, PartialCountingOverview> parentMap, PartialCountingOverview partialCountingOverview) {
		PartialCountingOverview parent = parentMap.get(partialCountingOverview.getAreaPath());
		if (parent == null) {
			parent = partialCountingOverview;
		} else {
			parent = parent.withChildren(partialCountingOverview.getChildOverviews());
		}
		return parent;
	}

	/**
	 * Finds partial counting overviews for category and qualifier
	 * @param countCategory  category to find partial counting overviews for
	 * @param countQualifier qualifer to find partial counting overviews for
	 * @return list of partial counting overviews for given count category and qualifier
	 */
	public List<PartialCountingOverview> overviewsFor(CountCategory countCategory, CountQualifier countQualifier) {
		List<PartialCountingOverview> overviews = new ArrayList<>();
		for (PartialCountingOverview partialCountingOverview : partialCountingOverviews) {
			if (partialCountingOverview.getCountCategory().equals(countCategory) && partialCountingOverview.getCountQualifier().equals(countQualifier)) {
				overviews.add(partialCountingOverview);
			}
		}
		return overviews;
	}

	/**
	 * Combines partial counting overviews into count overviews for a given area.  Partial overview is a count overview for a given count qualifier.
	 * Assumes there can be zero protocol counts.  Assumes there must be preliminary counts if there are final counts.
	 * @param countCategory category to create count overviews for
	 * @return collection of counting overviews for count category
	 */
    public Collection<CountingOverview> overviewsFor(CountCategory countCategory) {
        Map<AreaPath, CountingOverview> combinedMap = new HashMap<>();

        List<PartialCountingOverview> protocolOverviews = overviewsFor(countCategory, CountQualifier.PROTOCOL);
        for (PartialCountingOverview protocolOverview : protocolOverviews) {
            CountingOverview overview = combinedMap.get(protocolOverview.getAreaPath());
            if (overview == null) {
                overview = new CountingOverview(protocolOverview.getAreaPath(), protocolOverview.getElectionPath(), protocolOverview.getName(), countCategory,
						false, false);
            }
			
            combinedMap.put(protocolOverview.getAreaPath(),
					overview.withProtocolCountStatusAndChildren(protocolOverview.getCountStatus(), protocolOverview.getChildOverviews()));
        }

        List<PartialCountingOverview> preliminaryOverviews = overviewsFor(countCategory, CountQualifier.PRELIMINARY);
        for (PartialCountingOverview preliminaryOverview : preliminaryOverviews) {
            CountingOverview overview = combinedMap.get(preliminaryOverview.getAreaPath());
            if (overview == null) {
                overview = new CountingOverview(preliminaryOverview.getAreaPath(),
						preliminaryOverview.getElectionPath(), preliminaryOverview.getName(), countCategory, preliminaryOverview.isManualCount(), false);
            }
            combinedMap.put(preliminaryOverview.getAreaPath(), overview
					.withPreliminaryCountStatus(preliminaryOverview.getCountStatus())
					.withPreliminaryManualCount(preliminaryOverview.isManualCount()));
        }

        List<PartialCountingOverview> finalOverviews = overviewsFor(countCategory, CountQualifier.FINAL);
        for (PartialCountingOverview finalOverview : finalOverviews) {
            CountingOverview overview = combinedMap.get(finalOverview.getAreaPath());
            combinedMap.put(finalOverview.getAreaPath(),
					overview.withFinalCountStatus(finalOverview.getCountStatus())
							.withFinalManualCount(finalOverview.isManualCount())
							.withRejectedBallots(finalOverview.getRejectedBallots())
							.withRejectedBallotsProcessed(finalOverview.isRejectedBallotsProcessed()));
        }

        return combinedMap.values();
    }

	public boolean pollingDistrictCountAndNoChildPollingDistricts() {
		return countingModeForCategory.get(CountCategory.VO) == CountingMode.BY_POLLING_DISTRICT && !childPollingDistricts;
	}

    public String getAreaName() {
        return areaName;
    }

	public AreaPath getAreaPath() {
		return areaPath;
	}

	public ElectionPath getElectionPath() {
		return electionPath;
	}

    public CountingMode getCountingModeForCategory(CountCategory countCategory) {
        return countingModeForCategory.get(countCategory);
    }

    public boolean isRequiredProtocolCount() {
        return requiredProtocolCount;
    }

	public boolean isChildPollingDistricts() {
		return childPollingDistricts;
	}
}
