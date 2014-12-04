package no.evote.presentation.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import no.evote.constants.EvoteConstants;
import no.evote.model.ReportCountCategory;
import no.evote.model.VoteCountCategory;
import no.evote.presentation.util.FacesUtil;

import static no.evote.constants.EvoteConstants.VOTE_COUNT_CATEGORY_VF;

public class ReportingCountCategoryElement {

	public static final int COUNT_TYPE_UNDEFINED = 0;
	public static final int COUNT_TYPE_CENTRAL = 1;
	public static final int COUNT_TYPE_CENTRAL_AND_BY_POLLING_DISTRICT = 2;
	public static final int COUNT_TYPE_BY_POLLING_DISTRICT = 3;
	public static final int COUNT_TYPE_BY_TECHNICAL_POLLING_DISTRICT = 4;

	// @report_count_category.count_mode_select.none kan fjernes
	private static Map<Integer, String> countModeMap = new HashMap<>();

	static {
		if (FacesContext.getCurrentInstance() != null) {
			countModeMap.put(COUNT_TYPE_CENTRAL, (String) FacesUtil.resolveExpression("#{msgs['@report_count_category.count_mode_select.central']}"));
			countModeMap.put(COUNT_TYPE_CENTRAL_AND_BY_POLLING_DISTRICT,
					(String) FacesUtil.resolveExpression("#{msgs['@report_count_category.count_mode_select.central_and_by_polling_district']}"));
			countModeMap.put(COUNT_TYPE_BY_POLLING_DISTRICT,
					(String) FacesUtil.resolveExpression("#{msgs['@report_count_category.count_mode_select.by_polling_district']}"));
			countModeMap.put(COUNT_TYPE_BY_TECHNICAL_POLLING_DISTRICT,
					(String) FacesUtil.resolveExpression("#{msgs['@report_count_category.count_mode_select.by_technical_polling_district']}"));
		}
	}

	private ReportCountCategory reportCountCategory;
	private int countMode;

	/**
	 * Creates instance with countMode and initialCountMode set from reportCountCategory/data in report_count_category table.
	 * 
	 * @param reportCountCategory
	 *            contains data from a row in the report_count_category table.
	 */
	public ReportingCountCategoryElement(final ReportCountCategory reportCountCategory) {
		this.reportCountCategory = reportCountCategory;
		if (reportCountCategory.isCentralPreliminaryCount()) {
			if (reportCountCategory.isPollingDistrictCount()) {
				countMode = COUNT_TYPE_CENTRAL_AND_BY_POLLING_DISTRICT;
			} else if (reportCountCategory.isTechnicalPollingDistrictCount()) {
				countMode = COUNT_TYPE_BY_TECHNICAL_POLLING_DISTRICT;
			} else {
				countMode = COUNT_TYPE_CENTRAL;
			}
		} else {
			if (reportCountCategory.isPollingDistrictCount()) {
				countMode = COUNT_TYPE_BY_POLLING_DISTRICT;
			} else {
				// Should not occur
				countMode = COUNT_TYPE_UNDEFINED;
			}
		}
	}

	/**
	 * @return list of select items
	 */
	public List<SelectItem> getChoices() {
		List<SelectItem> choices = new ArrayList<>();

		SelectItem central = new SelectItem(str(COUNT_TYPE_CENTRAL), text(COUNT_TYPE_CENTRAL));
		SelectItem centralPollingDistrict = new SelectItem(str(COUNT_TYPE_CENTRAL_AND_BY_POLLING_DISTRICT), text(COUNT_TYPE_CENTRAL_AND_BY_POLLING_DISTRICT));
		SelectItem pollingDistrict = new SelectItem(str(COUNT_TYPE_BY_POLLING_DISTRICT), text(COUNT_TYPE_BY_POLLING_DISTRICT));
		SelectItem technicalPollingDistrict = new SelectItem(str(COUNT_TYPE_BY_TECHNICAL_POLLING_DISTRICT), text(COUNT_TYPE_BY_TECHNICAL_POLLING_DISTRICT));

		choices.add(central);
		choices.add(centralPollingDistrict);
		choices.add(pollingDistrict);
		if (allowTechnical()) {
			choices.add(technicalPollingDistrict);
		}

		if (!reportCountCategory.isEditable()) {
			centralPollingDistrict.setDisabled(true);
			central.setDisabled(true);
			pollingDistrict.setDisabled(true);
			technicalPollingDistrict.setDisabled(true);
			return choices;
		}

		if (!useTechnical()) {
			technicalPollingDistrict.setDisabled(true);
		}

		if (reportCountCategory.getVoteCountCategory().isCategoryForOrdinaryAdvanceVotes() && reportCountCategory.getMunicipality().isAdvanceVoteInBallotBox()) {
			centralPollingDistrict.setDisabled(true);
			pollingDistrict.setDisabled(true);
		}

		if (reportCountCategory.getVoteCountCategory().isMandatoryCentralCount()) {
			pollingDistrict.setDisabled(true);
			if (reportCountCategory.getVoteCountCategory().isMandatoryTotalCount()) {
				centralPollingDistrict.setDisabled(true);
			} else {
				if (reportCountCategory.getVoteCountCategory().getId().equalsIgnoreCase(EvoteConstants.VOTE_COUNT_CATEGORY_EK)
						|| reportCountCategory.getVoteCountCategory().getId().equalsIgnoreCase(EvoteConstants.VOTE_COUNT_CATEGORY_EO)) {
					central.setDisabled(true);
				}
			}
		} else {
			if (reportCountCategory.getVoteCountCategory().isMandatoryTotalCount()) {
				centralPollingDistrict.setDisabled(true);
				pollingDistrict.setDisabled(true);
			}
		}
		return choices;
	}

	private boolean useTechnical() {
		return allowTechnical() && reportCountCategory.isTechnicalPollingDistrictCountConfigurable();
	}

	private boolean allowTechnical() {
		return reportCountCategory.getMunicipality().isTechnicalPollingDistrictsAllowed();
	}

	private String str(int countType) {
		return String.valueOf(countType);
	}

	private String text(Integer countType) {
		return countModeMap.get(countType);
	}

	/**
	 * Updates flags on ReportCountCategory based on count mode
	 * @param event
	 *            with new value for count mode
	 */
	public void changeCountMode(final ValueChangeEvent event) {
		countMode = (Integer) event.getNewValue();
		switch (countMode) {
		case COUNT_TYPE_CENTRAL:
			reportCountCategory.setCentralPreliminaryCount(true);
			reportCountCategory.setPollingDistrictCount(false);
			reportCountCategory.setTechnicalPollingDistrictCount(false);
			break;
		case COUNT_TYPE_CENTRAL_AND_BY_POLLING_DISTRICT:
			reportCountCategory.setCentralPreliminaryCount(true);
			reportCountCategory.setPollingDistrictCount(true);
			reportCountCategory.setTechnicalPollingDistrictCount(false);
			break;
		case COUNT_TYPE_BY_POLLING_DISTRICT:
			reportCountCategory.setCentralPreliminaryCount(false);
			reportCountCategory.setPollingDistrictCount(true);
			reportCountCategory.setTechnicalPollingDistrictCount(false);
			break;
		case COUNT_TYPE_BY_TECHNICAL_POLLING_DISTRICT:
			reportCountCategory.setCentralPreliminaryCount(true);
			reportCountCategory.setPollingDistrictCount(false);
			reportCountCategory.setTechnicalPollingDistrictCount(true);
			break;
		default:
			break;
		}
	}

	public void setCountMode(final int mode) {
		countMode = mode;
	}

	public int getCountMode() {
		return countMode;
	}

	public String getCountModeString() {
		return countModeMap.get(countMode);
	}

	public VoteCountCategory getVotingCountCategory() {
		return reportCountCategory.getVoteCountCategory();
	}

	public void setVotingCountCategory(final VoteCountCategory votingCountCategory) {
		reportCountCategory.setVoteCountCategory(votingCountCategory);
	}

	public ReportCountCategory getReportCountCategory() {
		return reportCountCategory;
	}

	public void setReportCountCategory(final ReportCountCategory reportCountCategory) {
		this.reportCountCategory = reportCountCategory;
	}

	/**
	 * @return true if VF, else false
	 */
	public boolean renderSpecialCoverSelect() {
		return VOTE_COUNT_CATEGORY_VF.equals(reportCountCategory.getVoteCountCategory().getId());
	}

	/**
	 * @return true if VF and a choice is enabled, else false
	 */
	public boolean enableSpecialCoverSelect() {
		return renderSpecialCoverSelect() && containsEnabled(getChoices());
	}

	private boolean containsEnabled(final List<SelectItem> choices) {
		for (SelectItem choice : choices) {
			if (!choice.isDisabled()) {
				return true;
			}
		}
		return false;
	}

	public boolean isEditable() {
		return reportCountCategory.isEditable();
	}
}
