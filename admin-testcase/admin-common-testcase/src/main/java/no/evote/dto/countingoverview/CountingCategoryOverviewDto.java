package no.evote.dto.countingoverview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.evote.constants.VoteCountStatusEnum;
import no.valg.eva.admin.common.Area;
import no.valg.eva.admin.common.counting.model.CountCategory;
import no.valg.eva.admin.common.counting.model.CountStatus;
import no.valg.eva.admin.common.counting.model.countingoverview.AreaOverview;
import no.valg.eva.admin.common.counting.model.countingoverview.CountingOverview;
import no.valg.eva.admin.common.counting.model.countingoverview.PartialCountingOverview;

public class CountingCategoryOverviewDto extends CountingOverviewDto implements Serializable {

	private List<CountingOverviewDto> countingOverviewDtos;

	public CountingCategoryOverviewDto() {
		super();
		isCategory = true;
	}

	public CountingCategoryOverviewDto(final CountCategory voteCountCategory, final List<CountingOverviewDto> countingOverviewDtos) {
		this();
		super.voteCountCategory = voteCountCategory;
		this.countingOverviewDtos = countingOverviewDtos;
	}

	public CountingCategoryOverviewDto(final CountCategory voteCountCategory) {
		this();
		super.voteCountCategory = voteCountCategory;
	}

	/**
	 * Creates CountingOverview instances and updates this.
	 * @param areaOverview count overviews for different count categories and qualifiers within a borough
	 * @param countCategory advance vote, election day vote, etc
	 * @return this updated instance
	 */
	public CountingCategoryOverviewDto applyCountingOverviews(AreaOverview areaOverview, CountCategory countCategory) {

		this.areaPath = areaOverview.getAreaPath();
		this.electionPath = areaOverview.getElectionPath();
        this.countingMode = areaOverview.getCountingModeForCategory(countCategory);
        this.isRequiredProtocolCount = areaOverview.isRequiredProtocolCount();

		VoteCountStatusEnum accumulatedVoteCountStatusProtocol = null;
		VoteCountStatusEnum accumulatedVoteCountStatusPreliminary = null;
		VoteCountStatusEnum accumulatedVoteCountStatusFinal = null;

		int accumulatedRejectedBallotsFinal = 0;
		boolean accumulatedRejectedBallotsFinalProcessed = true;

        Collection<CountingOverview> countingOverviews = areaOverview.overviewsFor(countCategory);
        List<CountingOverviewDto> countingOverviewList = new ArrayList<>();

		for (CountingOverview countingOverview : countingOverviews) {

            CountingOverviewDto countingOverviewDto = new CountingOverviewDto(
					new Area(countingOverview.getAreaPath(), countingOverview.getName()),
                    countingOverview.getElectionPath(),
                    countCategory,
                    nullableStatus(countingOverview.getProtocolCountStatus()),
                    VoteCountStatusEnum.getStatus(countingOverview.getPreliminaryCountStatus().getId()),
                    VoteCountStatusEnum.getStatus(countingOverview.getFinalCountStatus().getId()),
                    countingOverview.rejectedBallots(),
                    countingOverview.isRejectedBallotsProcessed(),
                    countingMode,
                    isRequiredProtocolCount(),
					childOverviews(countingOverview.getChildOverviews()),
					countingOverview.isManualCountPreliminary(),
					countingOverview.isManualCountFinal());

            accumulatedVoteCountStatusProtocol = accumulateLowestStatus(accumulatedVoteCountStatusProtocol, countingOverviewDto.getVoteCountStatusProtocol());
            accumulatedVoteCountStatusPreliminary = accumulateLowestStatus(accumulatedVoteCountStatusPreliminary, countingOverviewDto.getVoteCountStatusPreliminary());
            accumulatedVoteCountStatusFinal = accumulateLowestStatus(accumulatedVoteCountStatusFinal, countingOverviewDto.getVoteCountStatusFinal());
            accumulatedRejectedBallotsFinal += countingOverview.rejectedBallots();
            accumulatedRejectedBallotsFinalProcessed &= countingOverview.isRejectedBallotsProcessed();
			
			this.setManualCountPreliminary(countingOverview.isManualCountPreliminary());
			this.setManualCountFinal(countingOverview.isManualCountFinal());
			
			countingOverviewList.add(countingOverviewDto);
		}

		this.setVoteCountStatusProtocol(accumulatedVoteCountStatusProtocol);
		this.setVoteCountStatusPreliminary(accumulatedVoteCountStatusPreliminary);
		this.setVoteCountStatusFinal(accumulatedVoteCountStatusFinal);
		this.setRejectedBallotsFinal(accumulatedRejectedBallotsFinal);
		this.setRejectedBallotsProcessedFinal(accumulatedRejectedBallotsFinalProcessed);

		this.countingOverviewDtos = countingOverviewList;

		return this;
	}

	private List<CountingOverviewDto> childOverviews(List<PartialCountingOverview> partialChildOverviews) {
		List<CountingOverviewDto> childOverviews = new ArrayList<>();
		for (PartialCountingOverview partialCountingOverview : partialChildOverviews) {
			childOverviews.add(new CountingOverviewDto(
					new Area(partialCountingOverview.getAreaPath(), partialCountingOverview.getName()),
					partialCountingOverview.getElectionPath(),
					partialCountingOverview.getCountCategory(),
					nullableStatus(partialCountingOverview.getCountStatus()),
					countingMode,
					isRequiredProtocolCount(),
					true));
		}
		return childOverviews;
	}

	private VoteCountStatusEnum accumulateLowestStatus(VoteCountStatusEnum currentAccumulatedStatus, VoteCountStatusEnum countStatus) {
        if (currentAccumulatedStatus == null || countStatus == null
                || currentAccumulatedStatus.getStatus() > countStatus.getStatus()) {
            return countStatus;
        }
        return currentAccumulatedStatus;
    }

    private VoteCountStatusEnum nullableStatus(CountStatus countStatus) {
		return countStatus == null ? null : VoteCountStatusEnum.getStatus(countStatus.getId());
	}

	public List<CountingOverviewDto> getCountingOverviewDtos() {
		return countingOverviewDtos;
	}

	public void setManualCountFinal(final boolean manualCountFinal) {
		this.isManualCountFinal = manualCountFinal;
	}

}
