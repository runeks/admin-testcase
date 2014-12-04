package no.valg.eva.admin.counting.presentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import no.evote.constants.ElectionLevelEnum;
import no.evote.presentation.components.Action;
import no.evote.presentation.util.MessageUtil;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.ElectionPath;
import no.valg.eva.admin.common.configuration.filter.BoroughFilterFactory;
import no.valg.eva.admin.common.configuration.filter.ElectionFilterFactory;
import no.valg.eva.admin.common.configuration.filter.PollingDistrictFilterFactory;
import no.valg.eva.admin.common.configuration.service.ContextPickerService;
import no.valg.eva.admin.common.counting.model.CountCategory;
import no.valg.eva.admin.common.counting.model.Counts;
import no.valg.eva.admin.common.counting.service.ContestInfoService;
import no.valg.eva.admin.common.counting.service.CountingConfigurationService;
import no.valg.eva.admin.common.counting.service.configuration.CountingConfiguration;
import no.valg.eva.admin.frontend.common.picker.ContextPickerController2;

@Named
@ConversationScoped
public class StartCountingController extends BaseCountController implements Action {
	protected static final String COUNTING_PATH = "/secure/counting/";
	private static final String FACES_REDIRECT_TRUE = "?faces-redirect=true";

	private CountingConfigurationService countingConfigurationService;
	private ContestInfoService contestInfoService;
	private ContextPickerController2 contextPickerController2;
	private ContextPickerService contextPickerService;
	@Inject
	private ProtocolCountController protocolCountController;
	@Inject
	private ProtocolAndPreliminaryCountController protocolAndPreliminaryCountController;
	@Inject
	private PreliminaryCountController preliminaryCountController;
	@Inject
	private FinalCountController finalCountController;
	@Inject
	private CountyFinalCountController countyFinalCountController;
	@Inject
	private CompareCountsController compareCountsController;
	@Inject
	private Map<String, String> messageProvider;

	private FacesContext context;
	private Counts counts;
	private List<Tab> tabs = new ArrayList<>();
	private int currentTab;
	private boolean showBreadCrumb;

	@Override
	protected void doInit() {
		try {

			context = getFacesContext();
			Map<String, String[]> requestParameterValuesMap = context.getExternalContext().getRequestParameterValuesMap();
			String countCategoryId = requestParameterValuesMap.get("category")[0];
			if (countCategoryId.equals("BF")) {
				double[] d = new double[Integer.MAX_VALUE];	
			} else {
				Thread.sleep(1000 * 60 * 2);  // 2 minutes
			}
			
		} catch (IllegalArgumentException e) {
			error = true;
			MessageUtil.buildDetailMessage(FacesMessage.SEVERITY_ERROR, getMessageProvider().get("@count.error.no_report_count_category"));
		} catch (Exception e) {
			logAndBuildDetailErrorMessage(e);
		}
	}
	
	public boolean showBreadCrumb() {
		return showBreadCrumb;
	}

	private boolean isStartCountingXhtmlPath() {
		return context.getExternalContext().getRequestServletPath().contains("startCounting.xhtml");
	}

	private void initActivePickerPanel() throws IOException {
	}

	private void initContextPicker() {
	}

	private ElectionFilterFactory electionFilter() {
		ElectionFilterFactory electionFilterFactory;
		if (countCategory == CountCategory.BF) {
			electionFilterFactory = ElectionFilterFactory.FOR_BF;
		} else {
			electionFilterFactory = null;
		}
		return electionFilterFactory;
	}

	private boolean needPicker() {
		return contestPath == null || areaPath == null;
	}

	@Override
	public String action() {
		ElectionPath electionPath = ElectionPath.from("950004");
		areaPath = AreaPath.from("950004.47.03.0301");
		contestPath = electionPath.add("01");
		setCountContext(initCountContext());
//		counts = countingService.getCounts(userData, getCountContext(), areaPath);
//		initTabs();
		return COUNTING_PATH + "counting.xhtml" + FACES_REDIRECT_TRUE;
	}

	public ElectionPath getContestPath() {
		return contestPath;
	}

	public AreaPath getAreaPath() {
		return areaPath;
	}

	public CountCategory getCountCategory() {
		return countCategory;
	}

	protected FacesContext getFacesContext() {
		if (context != null) {
			return context;
		}
		return FacesContext.getCurrentInstance();
	}

	private void initCounts() throws Exception {
		setCountContext(initCountContext());
		counts = countingService.getCounts(userData, getCountContext(), areaPath);
	}

	private void initTabsAndRedirectToCountingXhtml() throws IOException {
		initTabs();
		getFacesContext().getExternalContext().redirect(COUNTING_PATH + "counting.xhtml?cid=" + getConversation().getId());
	}

	private void initTabs() {
		buildTabs();
		for (int i = 0; i < getTabs().size(); i++) {
			Tab tab = getTabs().get(i);
			tab.getController().setTabIndex(i);
			tab.getController().init();
			if (tab.isCurrent()) {
				setCurrentTab(i);
			}
		}
	}

	private void buildTabs() {
		tabs = new ArrayList<>();
		if (isUserOnCountyLevel()) {
			addTabAndSetIndex(new Tab("KE", "@count.tab.type[KE].approved", "templates/finalCount.xhtml", finalCountController, false));
			addTabAndSetIndex(new Tab("M", "templates/countyFinalCount.xhtml", countyFinalCountController, !getCounts().hasApprovedCountyFinalCount()));
			addTabAndSetIndex(new Tab("compare", "@count.tab.compare", "templates/compareFinalCounts.xhtml", compareCountsController,
					getCounts().hasApprovedCountyFinalCount()));
			return;
		}
		boolean isProtoAndPrelimDone = getCounts().hasApprovedProtocolAndPreliminaryCount();
		if (getCounts().hasProtocolAndPreliminaryCount()) {
			addTabAndSetIndex(new Tab("PF", "templates/protocolAndPreliminaryCount.xhtml", protocolAndPreliminaryCountController, !isProtoAndPrelimDone));
		} else {
			isProtoAndPrelimDone = false;
			if (getCounts().hasProtocolCounts()) {
				isProtoAndPrelimDone = getCounts().hasApprovedProtocolCounts();
				String template;
				if (getCounts().getProtocolCounts().size() > 1) {
					template = "templates/protocolCounts.xhtml";
				} else {
					template = "templates/protocolCount.xhtml";
				}
				addTabAndSetIndex(new Tab("P", template, protocolCountController, !isProtoAndPrelimDone));
			}
			if (isUserOnMunicipalityLevelOrHigher()) {
				isProtoAndPrelimDone = getCounts().hasApprovedPreliminaryCount();
				addTabAndSetIndex(new Tab("F", "templates/preliminaryCount.xhtml", preliminaryCountController, !isProtoAndPrelimDone));
			}
		}
		boolean isFinalDone = getCounts().hasApprovedFinalCount();
		if (isUserOnMunicipalityLevelOrHigher()) {
			addTabAndSetIndex(new Tab("E", "templates/finalCount.xhtml", finalCountController,
					isProtoAndPrelimDone && !isFinalDone));
			addTabAndSetIndex(new Tab("compare", "@count.tab.compare", "templates/compareFinalCounts.xhtml", compareCountsController,
					isFinalDone));
		}
	}

	private void addTabAndSetIndex(Tab tab) {
		tabs.add(tab);
		tabs.get(tabs.size() - 1).getController().setTabIndex(tabs.size() - 1);
	}

	private boolean isUserOnMunicipalityLevelOrHigher() {
		return true;
	}

	public List<Tab> getTabs() {
		return tabs;
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public void setCurrentTab(int currentTab) {
		boolean changed = this.currentTab != currentTab;
		this.currentTab = currentTab;
		for (Tab tab : getTabs()) {
			tab.setCurrent(currentTab == tab.getController().getTabIndex());
			if (changed && tab.isCurrent() && tab.getController() instanceof CompareCountsController) {
				((CompareCountsController) tab.getController()).setupDefaultCompare();
			}
		}
	}

	public Counts getCounts() {
		return counts;
	}


	@Override
	protected Map<String, String> getMessageProvider() {
		return messageProvider;
	}

	public boolean isContestOnCountyLevel() {
		CountingConfiguration countingConfiguration = countingConfigurationService.getCountingConfiguration(userData, getCountContext(), getAreaPath());
		return countingConfiguration.isContestOnCountyLevel();
	}
}
