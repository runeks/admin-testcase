package no.evote.dto.countingoverview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.evote.constants.VoteCountStatusEnum;
import no.valg.eva.admin.common.Area;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.ElectionPath;
import no.valg.eva.admin.common.counting.constants.CountingMode;
import no.valg.eva.admin.common.counting.model.CountCategory;

public class CountingOverviewDto implements Serializable {

    private List<CountingOverviewDto> childPollingDistricts  = new ArrayList<>();

	protected String name;
	protected AreaPath areaPath;
	protected ElectionPath electionPath;

	protected CountingMode countingMode;
	protected CountCategory voteCountCategory;
	
	protected VoteCountStatusEnum voteCountStatusProtocol = VoteCountStatusEnum.NONE;
	protected VoteCountStatusEnum voteCountStatusPreliminary = VoteCountStatusEnum.NONE;
	protected VoteCountStatusEnum voteCountStatusFinal = VoteCountStatusEnum.NONE;
	protected boolean isRejectedBallotsProcessedFinal;
	protected boolean isRejectedBallotsProcessedPreliminary;
	protected int rejectedBallotsFinal;
	protected int rejectedBallotsPreliminary;
	protected boolean isParent;
	protected boolean isParentPollingDistrict;
	protected boolean isChildPollingDistrict;
	protected boolean isCategory = false;
	protected boolean isRequiredProtocolCount;
	protected boolean isManualCountFinal;
	
	protected boolean isManualCountPreliminary;

    public CountingOverviewDto() {
		// comment for PMD
		super();
	}

	public CountingOverviewDto(final String name,
							   final boolean requiredProtocolCount,
							   CountingMode countingMode, AreaPath areaPath) {
		super();
		this.name = name;
		this.isRequiredProtocolCount = requiredProtocolCount;
		this.countingMode = countingMode;
        this.areaPath = areaPath;
	}

	public CountingOverviewDto(Area area, ElectionPath electionPath, CountCategory countCategory, VoteCountStatusEnum countStatusProtocol,
							   VoteCountStatusEnum countStatusPreliminary, VoteCountStatusEnum countStatusFinal, int rejectedBallots, boolean rejectedBallotsProcessed,
							   CountingMode countingMode, boolean requiredProtocolCount, List<CountingOverviewDto> countingOverviewDtos, boolean manualCountPreliminary,
							   boolean manualCountFinal) {
        this.name = area.getName();
        this.areaPath = area.getAreaPath();
        this.electionPath = electionPath;
        this.voteCountCategory = countCategory;
        this.voteCountStatusProtocol = countStatusProtocol;
        this.voteCountStatusPreliminary = countStatusPreliminary;
        this.voteCountStatusFinal = countStatusFinal;
        this.rejectedBallotsFinal = rejectedBallots;
        this.isRejectedBallotsProcessedFinal = rejectedBallotsProcessed;
        this.countingMode = countingMode;
		this.isRequiredProtocolCount = requiredProtocolCount;
		this.childPollingDistricts = countingOverviewDtos;
		this.isManualCountPreliminary = manualCountPreliminary;
		this.isManualCountFinal = manualCountFinal;
    }

	public CountingOverviewDto(Area area, ElectionPath electionPath, CountCategory countCategory, VoteCountStatusEnum countStatusProtocol,
							   CountingMode countingMode, boolean requiredProtocolCount, boolean isChildPollingDistrict) {
        this.name = area.getName();
        this.areaPath = area.getAreaPath();
        this.electionPath = electionPath;
        this.voteCountCategory = countCategory;
        this.voteCountStatusProtocol = countStatusProtocol;
        this.voteCountStatusPreliminary = null;
        this.voteCountStatusFinal = null;
        this.rejectedBallotsFinal = 0;
        this.isRejectedBallotsProcessedFinal = false;
        this.countingMode = countingMode;
		this.isRequiredProtocolCount = requiredProtocolCount;
		this.isChildPollingDistrict = isChildPollingDistrict;
    }

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public VoteCountStatusEnum getVoteCountStatusProtocol() {
		return voteCountStatusProtocol;
	}

	public void setVoteCountStatusProtocol(final VoteCountStatusEnum voteCountStatusProtocol) {
		this.voteCountStatusProtocol = voteCountStatusProtocol;
	}

	public VoteCountStatusEnum getVoteCountStatusPreliminary() {
		return voteCountStatusPreliminary;
	}

	public void setVoteCountStatusPreliminary(final VoteCountStatusEnum voteCountStatusPreliminary) {
		this.voteCountStatusPreliminary = voteCountStatusPreliminary;
	}

	public VoteCountStatusEnum getVoteCountStatusFinal() {
		return voteCountStatusFinal;
	}

	public void setVoteCountStatusFinal(final VoteCountStatusEnum voteCountStatusFinal) {
		this.voteCountStatusFinal = voteCountStatusFinal;
	}

	public boolean isRejectedBallotsProcessedFinal() {
		return isRejectedBallotsProcessedFinal;
	}

	public void setRejectedBallotsProcessedFinal(final boolean rejectedBallotsProcessed) {
		isRejectedBallotsProcessedFinal = rejectedBallotsProcessed;
	}

	public void setRejectedBallotsProcessedPreliminary(final boolean rejectedBallotsProcessedPreliminary) {
		this.isRejectedBallotsProcessedPreliminary = rejectedBallotsProcessedPreliminary;
	}

	public int getRejectedBallotsFinal() {
		return rejectedBallotsFinal;
	}

	public void setRejectedBallotsFinal(final int rejectedBallots) {
		rejectedBallotsFinal = rejectedBallots;
	}

	public void setRejectedBallotsPreliminary(final int isRejectedBallotsPreliminary) {
		this.rejectedBallotsPreliminary = isRejectedBallotsPreliminary;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setParent(final boolean parent) {
		this.isParent = parent;
	}

	public boolean isParentPollingDistrict() {
		return isParentPollingDistrict;
	}

	public void setParentPollingDistrict(final boolean parentPollingDistrict) {
		this.isParentPollingDistrict = parentPollingDistrict;
	}

	public boolean isCentralPreliminaryCount() {
		return countingMode.isCentralPreliminaryCount();
	}

	public CountCategory getVoteCountCategory() {
		return voteCountCategory;
	}

	public boolean isMunicipalityPollingDistrict() {
		return areaPath.isMunicipalityPollingDistrict();
	}

	public boolean isTechnicalPollingDistrict() {
		return areaPath.isTechnicalPollingDistrict();
	}

	public boolean isPollingDistrictCount() {
        return countingMode.isPollingDistrictCount();
	}

	public boolean isRequiredProtocolCount() {
		return isRequiredProtocolCount;
	}

	public void setRequiredProtocolCount(final boolean requiredProtocolCount) {
		this.isRequiredProtocolCount = requiredProtocolCount;
	}

	public void setVoteCountCategory(final CountCategory voteCountCategory) {
		this.voteCountCategory = voteCountCategory;
	}

	public void setVoteCountCategory(final String voteCountCategory) {
		this.voteCountCategory = CountCategory.valueOf(voteCountCategory);
	}

	public boolean isCategory() {
		return isCategory;
	}

	public boolean isManualCountFinal() {
		return isManualCountFinal;
	}

	public void setManualCountFinal(boolean isManualCountFinal) {
		this.isManualCountFinal = isManualCountFinal;
	}

	public boolean isManualCountPreliminary() {
		return isManualCountPreliminary;
	}

	public void setManualCountPreliminary(boolean isManualCountPreliminary) {
		this.isManualCountPreliminary = isManualCountPreliminary;
	}


	public boolean isPreliminaryApprovedButHasNotRegisteredCorrectedBallots() {
		if (isPreliminaryApproved()) {
			if (rejectedBallotsPreliminary == 0) {
				return false;
			} else if (!isRejectedBallotsProcessedPreliminary) {
				return true;
			}
		}
		return false;
	}

	public boolean isFinalApprovedButHasNotRegisteredCorrectedBallots() {
		if (voteCountStatusFinal == VoteCountStatusEnum.APPROVED) {
			if (rejectedBallotsFinal == 0) {
				return false;
			} else if (!isRejectedBallotsProcessedFinal) {
				return true;
			}
		}
		return false;
	}

	public boolean isPreliminaryApproved() {
		return voteCountStatusPreliminary == VoteCountStatusEnum.APPROVED || voteCountStatusPreliminary == VoteCountStatusEnum.TO_SETTLEMENT;
	}

    public List<CountingOverviewDto> getChildPollingDistricts() {
        return Collections.unmodifiableList(childPollingDistricts);
    }

    public void addChildPollingDistrict(CountingOverviewDto childPollingDistrict) {
        this.childPollingDistricts.add(childPollingDistrict);
    }

    public boolean isChildPollingDistrict() {
        return isChildPollingDistrict;
    }

    public void setChildPollingDistrict(boolean childPollingDistrict) {
        this.isChildPollingDistrict = childPollingDistrict;
    }

	public AreaPath getAreaPath() {
		return areaPath;
	}

	public ElectionPath getElectionPath() {
		return electionPath;
	}

	public void setAreaPath(AreaPath areaPath) {
		this.areaPath = areaPath;
	}

	public void setElectionPath(ElectionPath electionPath) {
		this.electionPath = electionPath;
	}

    public boolean isNoCountNeededProtocol() {
        return getVoteCountCategory() != CountCategory.VO || (isMunicipalityPollingDistrict() || !isRequiredProtocolCount());
    }

	public boolean isVoteCountStatusProtocolCounting() {
		return voteCountStatusProtocol == VoteCountStatusEnum.COUNTING;
	}

	public boolean isVoteCountStatusProtocolApproved() {
		return voteCountStatusProtocol == VoteCountStatusEnum.APPROVED;
	}

	public boolean isVoteCountStatusPreliminaryCounting() {
		return voteCountStatusPreliminary == VoteCountStatusEnum.COUNTING;
	}

	public boolean isVoteCountStatusFinalApproved() {
		return voteCountStatusFinal == VoteCountStatusEnum.APPROVED;
	}

	public boolean isVoteCountStatusFinalCounting() {
		return voteCountStatusFinal == VoteCountStatusEnum.COUNTING;
	}

	public boolean preliminaryCountExists() {
		return voteCountStatusPreliminary != VoteCountStatusEnum.NONE;
	}

	public boolean finalCountExists() {
		return voteCountStatusFinal != VoteCountStatusEnum.NONE;
	}

}
