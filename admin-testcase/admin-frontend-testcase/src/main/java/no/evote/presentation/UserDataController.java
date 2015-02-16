package no.evote.presentation;

import static java.util.Collections.EMPTY_MAP;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.constants.SecObjWeb;
import no.evote.i18n.PreLoginMessageWrapper;
import no.evote.model.ElectionEvent;
import no.evote.presentation.util.Menu;
import no.evote.security.UserData;
import no.valg.eva.admin.common.rbac.service.ContactInfo;
import no.valg.eva.admin.frontend.common.dialog.DialogUtils;

import com.google.common.collect.ImmutableMap;

@Named("UserDataController")
@SessionScoped
@SuppressWarnings({ "PMD.NcssMethodCount", "PMD.AvoidDuplicateLiterals" })
public class UserDataController extends BaseController {
	private static final ImmutableMap<String, Object> CONTACT_DIALOG_OPTS = ImmutableMap.<String, Object> of("modal", "true");
	private static final String CONTACT_DIALOG_PATH = "/secure/includes/editCurrentUserDialog";

	@Inject
	private UserData userData;


	@Inject
	private MenuController menuController;

	private List<Menu> accordionMenus;
	private List<Menu> menus;
	private ContactInfo contactInfo;

	public boolean isRenderAccordion() {
		return !getAccordionMenus().isEmpty();
	}

	public List<Menu> getAccordionMenus() {
		return new ArrayList<>();
	}

	public List<Menu> getMenus() {
		if (menus == null) {
			menus = new ArrayList<>();
			createPreliminariesMenu();
			createVotingMenu();
			createCountingMenu();
			createSettlementMenu();
		}
		return menus;
	}

	private void createAccordionConfigMenu() {
		Menu root = new Menu("");
		Menu configMenu = new Menu("@menu.config.header", true);
		if (hasAccess(SecObjWeb.CONFIGURATION_ELECTION_ELECTION_EVENT)) {
			configMenu.addChild(new Menu("@menu.config.edit_election_event", "/secure/electionEvent.xhtml"));
		}
		if (hasAnyConfElection()) {
			configMenu.addChild(new Menu("@menu.config.election", "/secure/listElections.xhtml"));
		}
		if (hasAccess(SecObjWeb.PARTY)) {
			configMenu.addChild(new Menu("@menu.config.partyregister", "/secure/editPartyList.xhtml"));
		}
		if (hasAccess(SecObjWeb.CONFIGURATION_ELECTION_VOTE_COUNT_CATEGORY)) {
			configMenu.addChild(new Menu("@menu.config.election_vote_count_category", "/secure/categories/configureCentralCountCategories.xhtml"));
		}
		if (hasAccess(SecObjWeb.CONFIGURATION_CENTRAL_CONFIGURATION_OVERVIEW)) {
			configMenu.addChild(new Menu("@menu.config.central_overview", "centralConfirmDialog"));
		}
		if (hasAccess(SecObjWeb.CONFIGURATION_DOWNLOAD)) {
			configMenu.addChild(new Menu("@menu.config.generate_download_EML", "/secure/generateEML.xhtml"));
		}
		if (hasAccess(SecObjWeb.CREATE_ELECTION_EVENT)) {
			configMenu.addChild(new Menu("@menu.config.election_event_administration", "/secure/listElectionEvents.xhtml"));
		}
		if (hasAccess(SecObjWeb.COPY_ROLES)) {
			configMenu.addChild(new Menu("@menu.config.copy_roles", "/secure/copyRoles.xhtml"));
		}
		if (hasAccess(SecObjWeb.CERTIFICATES_MANAGEMENT)) {
			configMenu.addChild(new Menu("@menu.config.certificate_management", "/secure/signingKeys.xhtml"));
		}
		if (hasAccess(SecObjWeb.CONFIGURATION_REPORTING_UNIT_TYPE)) {
			configMenu.addChild(new Menu("@menu.config.reporting_unit_type", "/secure/reportingUnitType.xhtml"));
		}

		if (!configMenu.getChildren().isEmpty()) {
			root.addChild(configMenu);
			getAccordionMenus().add(root);
		}
	}

	private void createAccordionElectoralRollMenu() {
		Menu root = new Menu("");
		Menu electoralRollMenu = new Menu("@menu.electoralRoll.header", true);
		if (hasAccess(SecObjWeb.ELECTORAL_ROLL_CREATE)) {
			electoralRollMenu.addChild(new Menu("@menu.electoralRoll.import", "/secure/importElectoralRoll.xhtml"));
			electoralRollMenu.addChild(new Menu("@menu.electoralRoll.listImports", "/secure/electoralRollBatches.xhtml"));
		}
		if (hasAccess(SecObjWeb.CONFIGURATION_AREA_IMPORT)) {
			electoralRollMenu.addChild(new Menu("@menu.config.area_import", "/secure/import_area_hierarchy.xhtml"));
		}
		if (hasAccess(SecObjWeb.ELECTORAL_ROLL_CREATE)) {
			electoralRollMenu.addChild(new Menu("@menu.electoralRoll.voterNumbers", "/secure/generateVoterNumbers.xhtml"));
			electoralRollMenu.addChild(new Menu("@menu.electoralRoll.listVoterNumbers", "/secure/generateVoterNumberBatches.xhtml"));
		}
		if (!electoralRollMenu.getChildren().isEmpty()) {
			root.addChild(electoralRollMenu);
			getAccordionMenus().add(root);
		}

	}

	private void createAccordionRbacMenu() {
		Menu root = new Menu("");
		Menu menu1 = new Menu("@menu.rbac", true);
		if (hasAccess(SecObjWeb.RBAC_ROLE)) {
			menu1.addChild(new Menu("@menu.rbac.roles", "/secure/adminRoles.xhtml"));
		}
		if (hasAccess(SecObjWeb.RBAC_IMPORT_EXPORT)) {
			menu1.addChild(new Menu("@menu.rbac.import_export", "/secure/exportImportOperators.xhtml"));
		}
		if (hasAccess(SecObjWeb.RBAC_ROLE_EXPORT_IMPORT)) {
			menu1.addChild(new Menu("@menu.rbac.roles_export_import", "/secure/exportImportRoles.xhtml"));
		}
		if (hasAccess(SecObjWeb.RBAC_INSPECT)) {
			menu1.addChild(new Menu("@rbac.inspect.inspectTitle", "/secure/inspectSecObj.xhtml"));
		}
		if (!menu1.getChildren().isEmpty()) {
			root.addChild(menu1);
			getAccordionMenus().add(root);
		}
	}

	private void createAccordionTranslationMenu() {
		if (hasAccess(SecObjWeb.TRANSLATION_LOCAL)) {
			Menu root = new Menu("");
			Menu menu1 = new Menu("@menu.translation.heading", true);
			menu1.addChild(new Menu("@translation.add.heading", "/secure/translationAdd.xhtml"));
			menu1.addChild(new Menu("@translation.edit.heading", "/secure/translation.xhtml"));
			menu1.addChild(new Menu("@translation.import.heading", "/secure/translationImport.xhtml"));
			menu1.addChild(new Menu("@translation.delete.heading", "/secure/translationDelete.xhtml"));
			root.addChild(menu1);
			getAccordionMenus().add(root);
		}
	}

	private void createAccordionApproveAndBatchMenu() {
		Menu root = new Menu("");
		if (hasAccess(SecObjWeb.BATCH_BATCH)) {
			Menu menu2 = new Menu("@menu.import.heading", true);
			menu2.addChild(new Menu("@menu.import.heading", "/secure/batches.xhtml"));
			root.addChild(menu2);
		}
		if (!root.getChildren().isEmpty()) {
			getAccordionMenus().add(root);
		}
	}

	private void createAccordionDeleteMenu() {
		Menu root = new Menu("");
		Menu menu1 = new Menu("@menu.delete", true);
		if (hasAccess(SecObjWeb.DELETE_VOTE_COUNTS)) {
			menu1.addChild(new Menu("@delete.vote_counts.header", "/secure/deleteVoteCounts.xhtml"));
		}
		if (hasAccess(SecObjWeb.DELETE_VOTINGS)) {
			menu1.addChild(new Menu("@delete.votings.header", "/secure/deleteVotings.xhtml"));
		}
		if (hasAccess(SecObjWeb.DELETE_SETTLEMENT)) {
			menu1.addChild(new Menu("@delete.settlement.header", "/secure/deleteSettlement.xhtml"));
		}
		if (hasAccess(SecObjWeb.DELETE_SETTLEMENT)) {
			menu1.addChild(new Menu("@delete.levelingSeatSettlement.header", "/secure/deleteLevelingSeatSettlement.xhtml"));
		}
		if (hasAccess(SecObjWeb.DELETE_VOTERS)) {
			if (hasMinAreaLevel(AreaLevelEnum.ROOT)) {
				menu1.addChild(new Menu("@delete.votersWithoutMvArea.header", "/secure/deleteVotersWithoutMvArea.xhtml"));
			}
			menu1.addChild(new Menu("@delete.voters.header", "/secure/deleteVoters.xhtml"));
			menu1.addChild(new Menu("@delete.listDeleteVoters.header", "/secure/deleteVotersBatches.xhtml"));
			menu1.addChild(new Menu("@delete.prepareInitialLoad.header", "/secure/prepareNewInitialLoad.xhtml"));
		}
		if (!menu1.getChildren().isEmpty()) {
			root.addChild(menu1);
			getAccordionMenus().add(root);
		}
	}

	private void createPreliminariesMenu() {
		Menu root = new Menu("@menu.preliminaries.header", true, EnumUserMenuIcons.PRELIMINARIES);

		// Config menu
		Menu configMenu = new Menu("@menu.electionConfig", true);
		if (hasAccess(SecObjWeb.CONFIGURATION_LOCAL_CONFIGURATION_OVERVIEW) || hasAccess(SecObjWeb.CONFIGURATION_LOCAL_CONFIGURATION_OVERVIEW_ELECTION)) {
			configMenu.addChild(new Menu("@menu.config.local_overview", "/secure/local_area_configuration.xhtml"));
		}
		if (hasCanEditUsers()) {
			configMenu.addChild(new Menu("@menu.operators", "/secure/rbac/operatorAdmin.xhtml"));
		}
		if (hasAnyConfArea()) {
			configMenu.addChild(new Menu("@menu.config.area", "/secure/listAreas.xhtml"));
		}
		if (hasAccess(SecObjWeb.CONFIGURATION_AREA_POLLING_STATION) && !menuController.isElectronicMarkoffsConfigured()) {
			configMenu.addChild(new Menu("@menu.config.polling_station", "/secure/pollingStationConf.xhtml"));
		}
		if (hasAccess(SecObjWeb.CONFIGURATION_REPORTING_COUNT_CATEGORY)) {
			configMenu.addChild(new Menu("@menu.config.report_count_category", "/secure/categories/listReportCountCategories.xhtml"));
		}
		if (hasAccess(SecObjWeb.CONFIGURATION_REPORTING_UNIT)) {
			configMenu.addChild(new Menu("@menu.config.reporting_unit", "/secure/listReportingUnits.xhtml"));
		}
		if (!configMenu.getChildren().isEmpty()) {
			root.addChild(configMenu);
		}

		// Listeforslag
		Menu listMenu = new Menu("@menu.listProposal", true);
		if (hasCanEditF()) {
			listMenu.addChild(new Menu("@menu.listProposal.config", "/secure/listProposal/configContestListProposal.xhtml"));
			listMenu.addChild(new Menu("@menu.listProposal.create", "/secure/listProposal/createListProposal.xhtml"));
			listMenu.addChild(new Menu("@menu.listProposal.edit", "/secure/listProposal/chooseEditListProposal.xhtml"));
		}
		if (hasCanEditR()) {
			listMenu.addChild(new Menu("@menu.listProposal.optionLists", "/secure/editReferendumOption.xhtml"));
		}
		if (!listMenu.getChildren().isEmpty()) {
			root.addChild(listMenu);
		}

		// Manntall
		Menu electoralRollMenu = new Menu("@menu.electoralRoll.header", true);
		if (hasAccess(SecObjWeb.VOTER_CREATE) || hasAccess(SecObjWeb.VOTER_UPDATE) || hasAccess(SecObjWeb.VOTER_SEARCH)) {
			electoralRollMenu.addChild(new Menu("@menu.electoralRoll.search", "/secure/searchElectoralRoll.xhtml"));
		}
		if (hasAccess(SecObjWeb.VOTER_CREATE) && hasAccess(SecObjWeb.VOTER_UPDATE) && hasMinAreaLevel(AreaLevelEnum.MUNICIPALITY)) {
			electoralRollMenu.addChild(new Menu("@menu.electoralRoll.create", "/secure/newVoterElectoralRoll.xhtml"));
			electoralRollMenu.addChild(new Menu("@menu.electoralRoll.history", "/secure/listVoterAudit.xhtml"));
		}
		if (!electoralRollMenu.getChildren().isEmpty()) {
			root.addChild(electoralRollMenu);
		}
		if (!root.getChildren().isEmpty()) {
			getMenus().add(root);
		}

	}

	private void createVotingMenu() {
		Menu root = new Menu("@access.e.count.voting_count", true, EnumUserMenuIcons.VOTING);

		// Forhåndsstemme
		Menu prelimMenu = new Menu("@menu.earlyVoting.header", true);
		if (hasAccess(SecObjWeb.VOTING_RECEIVE_EARLY_VOTE)) {
			prelimMenu.addChild(new Menu("@menu.earlyVoting.manual", "/secure/voting/searchAdvanceVoting.xhtml"));
		}
		if (hasAccess(SecObjWeb.VOTING_RECEIVE_CENTRAL_REGISTRATION)) {
			prelimMenu.addChild(new Menu("@menu.earlyVoting.recievedEnvelopsInMunicipality", "/secure/voting/searchAdvanceEnvelopeVoting.xhtml"));
			prelimMenu.addChild(new Menu("@menu.earlyVoting.lateValidations", "/secure/voting/searchAdvanceLateValidation.xhtml"));
		}
		if (hasAccess(SecObjWeb.VOTING_APPROVE_ADVANCE)) {
			prelimMenu.addChild(new Menu("@menu.earlyVoting.approve", "/secure/voting/searchApproveVoting.xhtml"));
			prelimMenu.addChild(new Menu("@menu.earlyVoting.faVotings", "/secure/voting/faVotingsSentFromMunicipality.xhtml"));
			prelimMenu.addChild(new Menu("@menu.approveVoting.rejectedVotings", "/secure/voting/rejectedVotingsReport.xhtml"));
			prelimMenu.addChild(new Menu("@menu.approveVoting.approveVotingNegative", "/secure/voting/searchApproveNegativeVoting.xhtml"));
		}
		if (hasAccess(SecObjWeb.VOTING_RECEIVE_EMPTY_ELECTION_CARD)) {
			prelimMenu.addChild(new Menu("@menu.report.empty_election_card", "/secure/emptyElectionCard.xhtml"));
		}

		if (!prelimMenu.getChildren().isEmpty()) {
			root.addChild(prelimMenu);
		}

		// Valgting
		Menu votingMenu = new Menu("@menu.electionDay.header", true);
			votingMenu.addChild(new Menu("@menu.electionDay.register", "/secure/voting/searchElectionDay.xhtml"));
		if (hasAccess(SecObjWeb.VOTING_RECEIVE_CENTRAL_REGISTRATION)) {
			votingMenu.addChild(new Menu("@menu.electionDay.central_registration", "/secure/voting/searchCentralRegistration.xhtml"));
		}
		if (hasAccess(SecObjWeb.VOTING_APPROVE_SPECIAL)) {
			votingMenu.addChild(new Menu("@menu.electionDay.approve", "/secure/voting/searchApproveVoting.xhtml"));
			votingMenu
					.addChild(new Menu("@menu.approveVoting.approveVotingNegativeElectionDay", "/secure/voting/searchApproveElectionDayNegativeVoting.xhtml"));
		}

		if (!votingMenu.getChildren().isEmpty()) {
			root.addChild(votingMenu);
		}

		// Voting status
		Menu votingStatus = new Menu("");
		if ((hasAccess(SecObjWeb.VOTING_APPROVE_ADVANCE) || hasAccess(SecObjWeb.VOTING_APPROVE_SPECIAL)) && hasMinimumMunicipalityAndElectionGroup()
				&& menuController.isElectronicMarkoffsConfigured()) {
			votingStatus.addChild(new Menu("@menu.voting.voting_status", "/secure/voting_status.xhtml"));
			root.addChild(votingStatus);
		}

		if (!root.getChildren().isEmpty()) {
			getMenus().add(root);
		}

	}

	private void createCountingMenu() {
		Menu root = new Menu("@menu.counting.heading", true, EnumUserMenuIcons.COUNTING);

		// MENU 1
		Menu menu1 = new Menu("");
		if (hasAccess(SecObjWeb.COUNT_OVERVIEW)) {
			menu1.addChild(new Menu("@menu.counting.overview", "/secure/counting/countingOverview.xhtml"));
		}
		if (hasAccess(SecObjWeb.COUNT_BARCODE)) {
			menu1.addChild(new Menu("@menu.counting.barcode_sticker", "/secure/sticker.xhtml"));
		}
		if (hasAccess(SecObjWeb.COUNT_COUNTY_VOTE_COUNT) && menuController.hasElectionsWithElectionType("F")) {
			menu1.addChild(new Menu("@menu.counting.countyResults", "/secure/countyCollatedCountResults.xhtml"));
		}
		if (!menu1.getChildren().isEmpty()) {
			root.addChild(menu1);
		}

		// MENU 2
		Menu menu2 = new Menu("");
		if (hasAccess(SecObjWeb.COUNT_BALLOT_EARLY_VOTING_READ)) {
			if (menuController.isValidCountCategoryId("FO")) {
				menu2.addChild(new Menu("@menu.counting.ordinary_advance_votes", "/secure/counting/startCounting.xhtml?category=FO"));
			}
			if (menuController.isValidCountCategoryId("FS")) {
				menu2.addChild(new Menu("@menu.counting.late_advance_votes", "/secure/counting/startCounting.xhtml?category=FS"));
			}
		}
		if (hasAccess(SecObjWeb.COUNT_BALLOT_ELECTION_DAY_READ)) {
			if (menuController.isValidCountCategoryId("VO")) {
				menu2.addChild(new Menu("@menu.counting.regular_electionday_votes", "/secure/counting/startCounting.xhtml?category=VO"));
			}
			if (menuController.isValidCountCategoryId("VS")) {
				menu2.addChild(new Menu("@menu.counting.special_cover_votes", "/secure/counting/startCounting.xhtml?category=VS"));
			}
			if (menuController.isValidCountCategoryId("VF") && !menuController.isElectronicMarkoffsConfigured()) {
				menu2.addChild(new Menu("@menu.counting.foreign_votes", "/secure/counting/startCounting.xhtml?category=VF"));
			}
		}
		if (!menu2.getChildren().isEmpty()) {
			root.addChild(menu2);
		}

		// MENU 3
		Menu menu3 = new Menu("");
		if (hasAccess(SecObjWeb.COUNT_APPROVE_REJECTED)) {
			menu3.addChild(new Menu("@menu.counting.approve_rejected.manual", "/secure/counting/approveManualRejectedCount.xhtml"));
			menu3.addChild(new Menu("@menu.counting.approve_rejected.scan", "/secure/counting/approveScannedRejectedCount.xhtml"));
		}
		if (!menu3.getChildren().isEmpty()) {
			root.addChild(menu3);
		}

		// MENU 4
		Menu menu4 = new Menu("");
		if (hasAccess(SecObjWeb.REPORTING_STATISTIC_DATA) || hasAccess(SecObjWeb.REPORTING_STATISTIC_ELECTION)
				|| hasAccess(SecObjWeb.REPORTING_STATISTIC_CORRECTED_RESULT)) {
			menu4.addChild(new Menu("@menu.statistic.reporting", "/secure/ssb2013/statisticReporting2013.xhtml"));
		}
		if (!menu4.getChildren().isEmpty()) {
			root.addChild(menu4);
		}

		Menu menu5 = new Menu("");
		menu5.addChild(new Menu("@menu.counting.emergency_envelopes", "/secure/counting/startCounting.xhtml?category=VB"));
		menu5.addChild(new Menu("@menu.counting.foreign_votes_borough", "/secure/counting/startCounting.xhtml?category=BF"));
		root.addChild(menu5);

		if (!root.getChildren().isEmpty()) {
			getMenus().add(root);
		}

	}

	private void createSettlementMenu() {
		Menu root = new Menu("@menu.settlement.heading", true, EnumUserMenuIcons.SETTLEMENT);

		// Settlement
		Menu settlement = new Menu("");
		if (hasAccess(SecObjWeb.SETTLEMENT_READ)) {
			settlement.addChild(new Menu("@menu.settlement.status", "/secure/settlement/settlementStatus.xhtml?p=1"));
			settlement.addChild(new Menu("@menu.settlement.result", "/secure/settlement/collatedCountResults.xhtml?p=2"));
			settlement.addChild(new Menu("@menu.settlement.mandate_distribution", "/secure/settlement/settlementResult.xhtml?p=3"));
			settlement.addChild(new Menu("@menu.settlement.candidate_announcement", "/secure/settlement/candidateAnnouncement.xhtml?p=4"));
		}
		if (hasAccess(SecObjWeb.COUNT_LEVELING_SEATS)) {
			settlement.addChild(new Menu("@menu.settlement.leveling_seats", "/secure/levelingSeats.xhtml"));
		}
		if (settlement.getChildren().size() > 0) {
			root.addChild(settlement);
		}

		// Reports
		if (hasAccess(SecObjWeb.REPORTING_REPORT)) {
			Menu report = new Menu("@menu.electionDay.reports", true);
			report.addChild(new Menu("@menu.reports.links.heading", "/secure/reporting/jasperReportLinks.xhtml"));
			root.addChild(report);
		}

		if (!root.getChildren().isEmpty()) {
			getMenus().add(root);
		}
		

	}

	public boolean hasAccess(final String secObj) {
		return true;
	}

	public Boolean hasMinAreaLevel(final String areaLevel) {
		return true;
	}

	public Boolean hasMinElectionLevel(final String electionLevel) {
		return true;
	}

	public boolean isAreaLevel(final int areaLevel) {
		return true;
	}

	public boolean isElectionLevel(final int electionLevel) {
		return true;
	}


	public UserData getUserData() {
		return userData;
	}

	public ElectionEvent getElectionEvent() {
		return null;
	}

	public String getLocale() {
		return null;
	}

	/**
	 * Returns true if the user has access to any contest at all
	 */
	public boolean hasAccessToContest() {
		return true;
	}

	public void showContactInfoDialog() {
		DialogUtils.openDialog(CONTACT_DIALOG_PATH, CONTACT_DIALOG_OPTS, EMPTY_MAP);
	}

	public void closeEditContactInfoDialog() {
		getRequestContext().closeDialog(null);
	}

	public void saveOperator() {
		closeEditContactInfoDialog();
	}

	private boolean hasAnyConfArea() {
		return hasAccess(SecObjWeb.CONFIGURATION_AREA_COUNTRY) || hasAccess(SecObjWeb.CONFIGURATION_AREA_COUNTY)
				|| hasAccess(SecObjWeb.CONFIGURATION_AREA_MUNICIPALITY)
				|| hasAccess(SecObjWeb.CONFIGURATION_AREA_BOROUGH) || hasAccess(SecObjWeb.CONFIGURATION_AREA_POLLING_DISTRICT)
				|| hasAccess(SecObjWeb.CONFIGURATION_AREA_POLLING_PLACE) || hasAccess(SecObjWeb.CONFIGURATION_AREA_POLLING_STATION);
	}

	private boolean hasAnyConfElection() {
		return hasAccess(SecObjWeb.CONFIGURATION_ELECTION_ELECTION_EVENT) || hasAccess(SecObjWeb.CONFIGURATION_ELECTION_ELECTION_GROUP)
				|| hasAccess(SecObjWeb.CONFIGURATION_ELECTION_ELECTION) || hasAccess(SecObjWeb.CONFIGURATION_ELECTION_CONTEST)
				|| hasAccess(SecObjWeb.CONFIGURATION_ENABLE_ELECTION_EVENT);
	}

	private boolean hasCanEditF() {
		return hasAccess(SecObjWeb.BALLOT) && menuController.hasElectionsWithElectionType("F");
	}

	private boolean hasCanEditR() {
		return hasAccess(SecObjWeb.BALLOT) && menuController.hasElectionsWithElectionType("R");
	}

	private boolean hasCanEditUsers() {
		return hasAccess(SecObjWeb.RBAC_OPERATOR) || hasAccess(SecObjWeb.RBAC_ROLE) || hasAccess(SecObjWeb.RBAC_INSPECT)
				|| hasAccess(SecObjWeb.RBAC_ROLE_EXPORT_IMPORT);
	}

	private boolean hasMinAreaLevel(AreaLevelEnum level) {
		return hasMinAreaLevel(String.valueOf(level.getLevel()));
	}

	private boolean hasMinElectionLevel(ElectionLevelEnum level) {
		return hasMinElectionLevel(String.valueOf(level.getLevel()));
	}

	private boolean hasMinimumMunicipalityAndElectionGroup() {
		return hasMinAreaLevel(AreaLevelEnum.MUNICIPALITY) && hasMinElectionLevel(ElectionLevelEnum.ELECTION_GROUP);
	}

	public ContactInfo getContactInfo() {
		return contactInfo;
	}

}
