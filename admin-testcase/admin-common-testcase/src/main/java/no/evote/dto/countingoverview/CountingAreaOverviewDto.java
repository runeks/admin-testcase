package no.evote.dto.countingoverview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import no.evote.constants.VoteCountStatusEnum;
import no.valg.eva.admin.common.counting.constants.CountingMode;

/**
 * Overview of counts for municipality or borough.
 */
public class CountingAreaOverviewDto implements Serializable {
	private List<CountingCategoryOverviewDto> countingCategoryOverviewDtos = new ArrayList<>();
	private String name;
	private VoteCountStatusEnum voteCountStatus;
	private CountingMode countingMode;
	private boolean protocolColumnToBeRendered;

	public CountingAreaOverviewDto() {
	}

	public List<CountingCategoryOverviewDto> getCountingCategoryOverviewDtos() {
		return countingCategoryOverviewDtos;
	}

	public void setCountingCategoryOverviewDtos(final List<CountingCategoryOverviewDto> countingCategoryOverviewDtos) {
		this.countingCategoryOverviewDtos = countingCategoryOverviewDtos;
	}

	public void addCountingCategoryOverviewDto(final CountingCategoryOverviewDto categoryOverviewDto) {
		countingCategoryOverviewDtos.add(categoryOverviewDto);
	}

	public String getName() {
		return name;
	}

	public void setName(final String municipalityname) {
		this.name = municipalityname;
	}

	public VoteCountStatusEnum getVoteCountStatus() {
		return voteCountStatus;
	}

	public void setVoteCountStatus(final VoteCountStatusEnum voteCountStatus) {
		this.voteCountStatus = voteCountStatus;
	}

	public CountingMode getCountingMode() {
		return countingMode;
	}

	public void setCountingMode(final CountingMode countingMode) {
		this.countingMode = countingMode;
	}

	public boolean isProtocolColumnToBeRendered() {
		return protocolColumnToBeRendered;
	}

	public void setProtocolColumnToBeRendered(boolean protocolColumnToBeRendered) {
		this.protocolColumnToBeRendered = protocolColumnToBeRendered;
	}
}
