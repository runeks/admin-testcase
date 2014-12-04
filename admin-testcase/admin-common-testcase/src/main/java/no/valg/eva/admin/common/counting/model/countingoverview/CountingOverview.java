package no.valg.eva.admin.common.counting.model.countingoverview;

import java.util.ArrayList;
import java.util.List;

import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.ElectionPath;
import no.valg.eva.admin.common.counting.model.CountCategory;
import no.valg.eva.admin.common.counting.model.CountStatus;

/**
 * Immutable value object containing count status and number of rejected ballots for the different qualifiers (protocol, preliminary and final).
 * Represents one row in "oversikt over tellinger".
 */
public class CountingOverview {

    private final AreaPath areaPath;
	private final ElectionPath electionPath;
	private final String name;
	private final CountCategory countCategory;
	/** protocolCountStatus is null when it is not required */
	private final CountStatus protocolCountStatus;
	private final CountStatus preliminaryCountStatus;
	private final CountStatus finalCountStatus;
	private final Integer rejectedBallots;
	private final boolean rejectedBallotsProcessed;
	private final boolean manualCountPreliminary;
	private final boolean manualCountFinal;
	private final List<PartialCountingOverview> childOverviews = new ArrayList<>();  // VO may have parent and child polling districts, tellekretser

	public CountingOverview(AreaPath areaPath, ElectionPath electionPath, String name, CountCategory countCategory, boolean manualCountPreliminary,
							boolean manualCountFinal) {
        this.areaPath = areaPath;
		this.electionPath = electionPath;
        this.name = name;
        this.countCategory = countCategory;
        protocolCountStatus = null;
        preliminaryCountStatus = CountStatus.NEW;
        finalCountStatus = CountStatus.NEW;
        rejectedBallots = null;
        rejectedBallotsProcessed = false;
		this.manualCountPreliminary = manualCountPreliminary;
		this.manualCountFinal = manualCountFinal;
    }

    public CountingOverview(AreaPath areaPath, ElectionPath electionPath, String name, CountCategory countCategory, CountStatus protocolCountStatus,
							CountStatus preliminaryCountStatus, CountStatus finalCountStatus, Integer rejectedBallots, boolean rejectedBallotsProcessed,
							boolean manualCountPreliminary, boolean manualCountFinal) {
        this.areaPath = areaPath;
        this.electionPath = electionPath;
		this.name = name;
        this.countCategory = countCategory;
        this.protocolCountStatus = protocolCountStatus;
        this.preliminaryCountStatus = preliminaryCountStatus;
        this.finalCountStatus = finalCountStatus;
		this.rejectedBallots = rejectedBallots;
		this.rejectedBallotsProcessed = rejectedBallotsProcessed;
		this.manualCountPreliminary = manualCountPreliminary;
		this.manualCountFinal = manualCountFinal;
    }

	public CountingOverview withProtocolCountStatusAndChildren(CountStatus countStatus, List<PartialCountingOverview> childOverviews) {
		CountingOverview countingOverview = new CountingOverview(this.areaPath, this.electionPath, this.name, this.countCategory,
                countStatus, this.preliminaryCountStatus, this.finalCountStatus, this.rejectedBallots, this.rejectedBallotsProcessed, this.manualCountPreliminary,
				this.manualCountFinal);
		countingOverview.childOverviews.addAll(childOverviews);
		return countingOverview;
	}

	public CountingOverview withPreliminaryCountStatus(CountStatus countStatus) {
		return new CountingOverview(this.areaPath, this.electionPath, this.name, this.countCategory, this.protocolCountStatus, countStatus,
				this.finalCountStatus, this.rejectedBallots, this.rejectedBallotsProcessed, this.manualCountPreliminary, this.manualCountFinal);
	}

	public CountingOverview withFinalCountStatus(CountStatus countStatus) {
		return new CountingOverview(this.areaPath, this.electionPath, this.name, this.countCategory, this.protocolCountStatus,
				this.preliminaryCountStatus, countStatus, this.rejectedBallots, this.rejectedBallotsProcessed, this.manualCountPreliminary, this.manualCountFinal);
	}

	public CountingOverview withRejectedBallots(Integer rejectedBallots) {
		return new CountingOverview(this.areaPath, this.electionPath, this.name, this.countCategory, this.protocolCountStatus,
				this.preliminaryCountStatus, this.finalCountStatus, rejectedBallots, this.rejectedBallotsProcessed, this.manualCountPreliminary, this.manualCountFinal);
	}

	public CountingOverview withRejectedBallotsProcessed(boolean rejectedBallotsProcessed) {
		return new CountingOverview(this.areaPath, this.electionPath, this.name, this.countCategory, this.protocolCountStatus,
				this.preliminaryCountStatus, this.finalCountStatus, this.rejectedBallots, rejectedBallotsProcessed, this.manualCountPreliminary, this.manualCountFinal);
	}

	public CountingOverview withPreliminaryManualCount(boolean manualCount) {
		return new CountingOverview(this.areaPath, this.electionPath, this.name, this.countCategory, this.protocolCountStatus,
				this.preliminaryCountStatus, this.finalCountStatus, this.rejectedBallots, this.rejectedBallotsProcessed, manualCount, this.manualCountFinal);
	}

	public CountingOverview withFinalManualCount(boolean manualCount) {
		return new CountingOverview(this.areaPath, this.electionPath, this.name, this.countCategory, this.protocolCountStatus,
				this.preliminaryCountStatus, this.finalCountStatus, this.rejectedBallots, this.rejectedBallotsProcessed, this.manualCountPreliminary, manualCount);
	}

    public String getName() {
        return name;
    }

    public CountStatus getProtocolCountStatus() {
        return protocolCountStatus;
    }

    public CountStatus getPreliminaryCountStatus() {
        return preliminaryCountStatus;
    }

    public CountStatus getFinalCountStatus() {
        return finalCountStatus;
    }

	public AreaPath getAreaPath() {
		return areaPath;
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

	public int rejectedBallots() {
		return rejectedBallots != null ? rejectedBallots : 0;
	}

	public List<PartialCountingOverview> getChildOverviews() {
		return childOverviews;
	}

	public boolean isManualCountPreliminary() {
		return manualCountPreliminary;
	}

	public boolean isManualCountFinal() {
		return manualCountFinal;
	}
}
