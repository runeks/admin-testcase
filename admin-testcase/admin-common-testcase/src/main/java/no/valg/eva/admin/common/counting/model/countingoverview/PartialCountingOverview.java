package no.valg.eva.admin.common.counting.model.countingoverview;

import java.util.ArrayList;
import java.util.List;

import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.ElectionPath;
import no.valg.eva.admin.common.counting.model.CountCategory;
import no.valg.eva.admin.common.counting.model.CountQualifier;
import no.valg.eva.admin.common.counting.model.CountStatus;

/**
 * Immutable value object containing count overview for a given qualifier and category. Represents one "cell" in "oversikt over tellinger".
 */
public class PartialCountingOverview {

	private final AreaPath areaPath;
	private final ElectionPath electionPath;
	private final String name;
	private final CountStatus countStatus;
	private final CountCategory countCategory;
	private final CountQualifier countQualifier;
	private final Integer rejectedBallots;
	private final boolean rejectedBallotsProcessed;
	private final boolean childPollingDistrict;
	private final boolean manualCount;
	private final List<PartialCountingOverview> childOverviews = new ArrayList<>();  // VO may have parent and child polling districts, tellekretser

	public PartialCountingOverview(AreaPath areaPath, ElectionPath electionPath, String name, CountStatus countStatus,
								   CountCategory countCategory, CountQualifier countQualifier, Integer rejectedBallots,
								   boolean rejectedBallotsProcessed, boolean childPollingDistrict, boolean manualCount) {
		this.areaPath = areaPath;
		this.electionPath = electionPath;
		this.name = name;
		this.countStatus = countStatus;
		this.countCategory = countCategory;
		this.countQualifier = countQualifier;
		this.rejectedBallots = rejectedBallots;
		this.rejectedBallotsProcessed = rejectedBallotsProcessed;
		this.childPollingDistrict = childPollingDistrict;
		this.manualCount = manualCount;
	}

	public PartialCountingOverview(AreaPath areaPath, ElectionPath electionPath, String name,
								   CountCategory countCategory, CountQualifier countQualifier, PartialCountingOverview partialCountingOverview) {
		this.areaPath = areaPath;
		this.electionPath = electionPath;
		this.name = name;
		this.countStatus = null;
		this.countCategory = countCategory;
		this.countQualifier = countQualifier;
		this.rejectedBallots = null;
		this.rejectedBallotsProcessed = false;
		this.childPollingDistrict = false;
		this.manualCount = partialCountingOverview.isManualCount();
		this.childOverviews.add(partialCountingOverview);
	}

	public PartialCountingOverview(PartialCountingOverview partialCountingOverview, List<PartialCountingOverview> childOverviews) {
        this.areaPath = partialCountingOverview.getAreaPath();
        this.electionPath = partialCountingOverview.getElectionPath();
        this.name = partialCountingOverview.getName();
        this.countCategory = partialCountingOverview.getCountCategory();
        this.countQualifier = partialCountingOverview.getCountQualifier();
        this.rejectedBallots = partialCountingOverview.getRejectedBallots();
        this.rejectedBallotsProcessed = partialCountingOverview.isRejectedBallotsProcessed();
        this.childPollingDistrict = partialCountingOverview.isChildPollingDistrict();
        this.manualCount = partialCountingOverview.isManualCount();
		this.childOverviews.addAll(childOverviews);
		CountStatus aggregatedStatus = null;
		for (PartialCountingOverview childOverview : childOverviews) {
			aggregatedStatus = updateStatus(aggregatedStatus, childOverview.getCountStatus());
		}
		this.countStatus = aggregatedStatus;
    }

	private CountStatus updateStatus(CountStatus aggregatedStatus, CountStatus childStatus) {
		if (aggregatedStatus == null || childStatus == null || childStatus == CountStatus.REJECTED || childStatus == CountStatus.NEW) {
			return childStatus;
		}
		if (aggregatedStatus == CountStatus.REJECTED || aggregatedStatus == CountStatus.NEW || aggregatedStatus == CountStatus.APPROVED) {
			return aggregatedStatus;
		}
		if (childStatus == CountStatus.APPROVED || childStatus == CountStatus.TO_SETTLEMENT) {
			return childStatus;
		}
		return null;
	}

	public AreaPath getAreaPath() {
		return areaPath;
	}

	public CountStatus getCountStatus() {
		return countStatus;
	}

	public CountCategory getCountCategory() {
		return countCategory;
	}

	public CountQualifier getCountQualifier() {
		return countQualifier;
	}

	public String getName() {
		return name;
	}

	public ElectionPath getElectionPath() {
		return electionPath;
	}

	public Integer getRejectedBallots() {
		return rejectedBallots;
	}

	public boolean isRejectedBallotsProcessed() {
		return rejectedBallotsProcessed;
	}

	public boolean isChildPollingDistrict() {
		return childPollingDistrict;
	}

    public List<PartialCountingOverview> getChildOverviews() {
        return childOverviews;
    }

    public boolean isParent() {
        return childOverviews.size() > 0;
    }

    public PartialCountingOverview withChildren(List<PartialCountingOverview> childOverviews) {
        List<PartialCountingOverview> updatedChildOverviews = new ArrayList<>(childOverviews);
        updatedChildOverviews.addAll(this.childOverviews);
        return new PartialCountingOverview(this, updatedChildOverviews);
    }

	public boolean isManualCount() {
		return manualCount;
	}
}
