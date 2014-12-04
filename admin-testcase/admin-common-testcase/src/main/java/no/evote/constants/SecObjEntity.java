package no.evote.constants;

public final class SecObjEntity {
	public static final String ANY_USER = "AnyUser";

	public static final String CONFIGURATION_AREA = "e.configuration.area";
	public static final String CONFIGURATION_AREA_COUNTRY = "e.configuration.area.country";
	public static final String CONFIGURATION_AREA_COUNTY = "e.configuration.area.county";
	public static final String CONFIGURATION_AREA_MUNICIPALITY = "e.configuration.area.municipality";
	public static final String CONFIGURATION_AREA_MUNICIPALITY_READ = "e.configuration.area.municipality.read";
	public static final String CONFIGURATION_AREA_MUNICIPALITY_FINISH = "e.configuration.area.municipality.finish";
	public static final String CONFIGURATION_AREA_MUNICIPALITY_CLOSE = "e.configuration.area.municipality.close";
	public static final String CONFIGURATION_AREA_BOROUGH = "e.configuration.area.borough";
	public static final String CONFIGURATION_AREA_POLLING_DISTRICT = "e.configuration.area.polling_district";
	public static final String CONFIGURATION_AREA_POLLING_DISTRICT_LIST_MISSING_VOTERS = "e.configuration.area.polling_district.list_missing_voters";
	public static final String CONFIGURATION_AREA_POLLING_COUNT_DISTRICT = "e.configuration.area.polling_count_district";
	public static final String CONFIGURATION_AREA_POLLING_PLACE = "e.configuration.area.polling_place";
	public static final String CONFIGURATION_AREA_POLLING_STATION = "e.configuration.area.polling_station";
	public static final String CONFIGURATION_CHANGE_AGE_LIMIT = "e.configuration.change_age_limit";
	public static final String CONFIGURATION_DOWNLOAD = "e.configuration.download";
	public static final String CONFIGURATION_GENERATE = "e.configuration.generate";
	public static final String CONFIGURATION_ELECTION = "e.configuration.election";
	public static final String CONFIGURATION_ELECTION_ELECTION_EVENT = "e.configuration.election.election_event";
	public static final String CONFIGURATION_ELECTION_ELECTION_GROUP = "e.configuration.election.election_group";
	public static final String CONFIGURATION_ELECTION_ELECTION = "e.configuration.election.election";
	public static final String CONFIGURATION_ELECTION_CONTEST = "e.configuration.election.contest";
	public static final String CONFIGURATION_ELECTION_CONTEST_READ = "e.configuration.election.contest.read";
	public static final String CONFIGURATION_ELECTION_CONTEST_FINISH = "e.configuration.election.contest.finish";
	public static final String CONFIGURATION_REJECT_CONTEST = "e.configuration.reject_contest";
	public static final String CONFIGURATION_REJECT_AREA = "e.configuration.reject_area";
	public static final String CONFIGURATION_REPORTING_UNIT = "e.configuration.reporting_unit";
	public static final String CONFIGURATION_REPORTING_UNIT_TYPE = "e.configuration.reporting_unit_type";
	public static final String CONFIGURATION_REPORTING_COUNT_CATEGORY = "e.configuration.reporting_count_category";
	public static final String CONFIGURATION_ELECTION_VOTE_COUNT_CATEGORY = "e.configuration.election_vote_count_category";
	public static final String CONFIGURATION_POLLING_DISTRICT_ELECTION_BOARD = "e.configuration.polling_district_election_board";
	public static final String CERTIFICATE_MANAGEMENT = "e.certificates.management";

	public static final String LEVELING_SEATS = "e.count.leveling_seats";

	public static final String STATISTIC_REPORTING_ALL = "e.reporting.statistic.data, e.reporting.statistic.election, e.reporting.statistic.corrected_result";
	public static final String STATISTIC_REPORTING_ELECTION_DATA = "e.reporting.statistic.data";
	public static final String STATISTIC_REPORTING_COUNT_DATA = "e.reporting.statistic.election";
	public static final String STATISTIC_REPORTING_CORRECTED_RESULT = "e.reporting.statistic.corrected_result";

	public static final String BALLOT = "e.ballot";
	public static final String BALLOT_CREATE = "e.ballot.create";
	public static final String BALLOT_READ = "e.ballot.read";
	public static final String BALLOT_UPDATE = "e.ballot.update";
	public static final String BALLOT_STATUS = "e.ballot.status";
	public static final String BALLOT_IMPORT = "e.ballot.import";

	public static final String PARTY = "e.party";
	public static final String PARTY_EDIT = "e.party.edit";
	public static final String PARTY_CREATE = "e.party.create";
	public static final String PARTY_DELETE = "e.party.delete";

	public static final String VOTER_SEARCH = "e.voter.search";
	public static final String VOTER_UPDATE = "e.voter.update";
	public static final String VOTER_CREATE = "e.voter.create";

	public static final String ELECTORAL_ROLL_LOAD_INITIAL_FILE = "e.electoral_roll.load_initial_file";
	public static final String ELECTORAL_ROLL_GENERATE_VOTER_NUMBER = "e.electoral_roll.generate_voter_number";
	public static final String ELECTORAL_ROLL_SCHEDULED_IMPORT = "e.electoral_roll.scheduled_import";
	public static final String ELECTORAL_ROLL_DOWNLOAD = "e.electoral_roll.download";
	public static final String ELECTORAL_ROLL_GENERATE = "e.electoral_roll.generate";

	public static final String VOTING_RECEIVE_CENTRAL_REGISTRATION = "e.voting.receive.central_registration";
	public static final String VOTING_RECEIVE_EMPTY_ELECTIONCARD = "e.voting.receive.emptyElectionCard";
	public static final String VOTING_RECEIVE_EARLY_VOTE = "e.voting.receive.early_vote";
	public static final String VOTING_RECEIVE_ELECTION_DAY_SPECIAL = "e.voting.receive.election_day_vote_special";
	public static final String VOTING_RECEIVE_ELECTION_DAY = "e.voting.receive.election_day_vote";
	public static final String VOTING_APPROVE = "e.voting.approve";
	public static final String VOTING_REVOKE = "e.voting.revoke";

	public static final String COUNT_VOTING_COUNT_ELECTION_DAY_COUNT_READ = "e.count.voting_count.election_day.read";
	public static final String COUNT_VOTING_COUNT_ELECTION_DAY_COUNT_CREATE = "e.count.voting_count.election_day.create";
	public static final String COUNT_VOTING_COUNT_ELECTION_DAY_COUNT_TO_APPROVAL = "e.count.voting_count.election_day.send_to_approval";
	public static final String COUNT_VOTING_COUNT_ELECTION_DAY_COUNT_APPROVE = "e.count.voting_count.election_day.approve";

	public static final String COUNT_BALLOT_COUNT_EARLY_VOTING = "e.count.ballot_count.early_voting";
	public static final String COUNT_BALLOT_COUNT_EARLY_VOTING_READ = "e.count.ballot_count.early_voting.read";
	public static final String COUNT_BALLOT_COUNT_EARLY_VOTING_CREATE = "e.count.ballot_count.early_voting.create";
	public static final String COUNT_BALLOT_COUNT_EARLY_VOTING_TO_APPROVAL = "e.count.ballot_count.early_voting.send_to_approval";
	public static final String COUNT_BALLOT_COUNT_EARLY_VOTING_APPROVE = "e.count.ballot_count.early_voting.approve";

	public static final String COUNT_BALLOT_COUNT_ELECTION_DAY_READ = "e.count.ballot_count.election_day.read";
	public static final String COUNT_BALLOT_COUNT_ELECTION_DAY_CREATE = "e.count.ballot_count.election_day.create";
	public static final String COUNT_BALLOT_COUNT_ELECTION_DAY_TO_APPROVAL = "e.count.ballot_count.election_day.send_to_approval";
	public static final String COUNT_BALLOT_COUNT_ELECTION_DAY_APPROVE = "e.count.ballot_count.election_day.approve";

	public static final String COUNT_PRELIMINARY_COUNT_RESET_APPROVAL = "e.count.preliminary_count.reset_approval";
	public static final String COUNT_FINAL_COUNT_RESET_APPROVAL = "e.count.final_count.reset_approval";
	public static final String COUNT_PROTOCOL_COUNT_RESET_APPROVAL = "e.count.protocol_count.reset_approval";
	public static final String COUNT_MUNICIPALITY_COUNT_RESET_APPROVAL = "e.count.municipality_count.reset_approval";

	public static final String COUNT_UPLOAD = "e.count.upload";

	public static final String COUNT_APPROVE_REJECTED = "e.count.approve_rejected";
	public static final String COUNT_REGISTER_CORRECTED = "e.count.register_corrected";

	public static final String COUNT_BARCODE = "e.count.barcode";

	public static final String COUNT_REVOKE_VOTE_COUNTS = "e.count.revokeVoteCounts";

	public static final String EVOTING_MARKOFF_UPLOAD = "e.voting.evotemarkoff.upload";

	public static final String LOCALE_TEXT = "e.text.locale_text";
	public static final String LOCALE_TEXT_CREATE = "e.text.locale_text.create";
	public static final String LOCALE_TEXT_READ = "e.text.locale_text.read";
	public static final String LOCALE_TEXT_UPDATE = "e.text.locale_text.update";

	public static final String OPERATOR_SEE_BELOW = "e.rbac.operator_see_below";
	public static final String OPERATOR = "e.rbac.operator";
	public static final String ROLE = "e.rbac.role";
	public static final String ROLE_IMPORT = "e.rbac.role.import";
	public static final String ROLE_EXPORT = "e.rbac.role.export";
	public static final String INSPECT = "e.rbac.inspect";

	public static final String WEB_SERVICE = "e.web_service";
	public static final String WEB_SERVICE_POLLING_INFO = "e.web_service.polling_info";
	public static final String WEB_SERVICE_RECEIVE_APPLICATION = "e.web_service.receive_application";

	public static final String REPORTING = "e.reporting";
	public static final String REPORTING_KIT = "e.reporting.kit";
	public static final String REPORTING_KIT_DOWNLOAD = "e.reporting.kit.download";
	public static final String REPORTING_TEMPLATE = "e.reporting.template";
	public static final String REPORTING_TEMPLATE_DEACTIVATE = "e.reporting.template.deactivate";
	public static final String REPORTING_TEMPLATE_DELETE = "e.reporting.template.delete";
	public static final String REPORTING_TEMPLATE_DOWNLOAD = "e.reporting.template.download";
	public static final String REPORTING_TEMPLATE_DUPLICATE = "e.reporting.template.duplicate";
	public static final String REPORTING_TEMPLATE_EDIT = "e.reporting.template.edit";
	public static final String REPORTING_TEMPLATE_EXECUTE = "e.reporting.template.execute";
	public static final String REPORTING_TEMPLATE_EXECUTE_IN_PROCESS = "e.reporting.template.execute_in_process";
	public static final String REPORTING_TEMPLATE_REACTIVATE = "e.reporting.template.reactivate";
	public static final String REPORTING_TEMPLATE_UPLOAD = "e.reporting.template.upload";
	public static final String REPORTING_TEMPLATE_VALIDATE = "e.reporting.template.validate";
	public static final String REPORTING_REPORT = "e.reporting.report";
	public static final String REPORTING_REPORT_DEACTIVATE = "e.reporting.report.deactivate";
	public static final String REPORTING_REPORT_DELETE = "e.reporting.report.delete";
	public static final String REPORTING_REPORT_DOWNLOAD = "e.reporting.report.download";
	public static final String REPORTING_REPORT_REACTIVATE = "e.reporting.report.reactivate";
	public static final String REPORTING_REPORT_VALIDATE = "e.reporting.report.validate";
	public static final String REPORTING_LINKS_RUN = "e.reporting.links.run";

	public static final String USER_REPORTS_LIST = "e.reporting.user.list";

	public static final String BATCH_ELECTORAL_ROLL = "e.batch.electoral_roll";
	public static final String BATCH_VOTER_NUMBER = "e.batch.voter_number";
	public static final String BATCH = "e.batch.batch";

	public static final String SETTLEMENT_CREATE = "e.settlement.create";
	public static final String SETTLEMENT_READ = "e.settlement.read";

	public static final String CREATE_ELECTION_EVENT = "e.create_election_event";

	public static final String DELETE_VOTERS = "e.delete.voters";
	public static final String DELETE_VOTINGS = "e.delete.votings";
	public static final String DELETE_VOTE_COUNTS = "e.delete.vote_counts";
	public static final String DELETE_SETTLEMENT = "e.delete.settlement";
	public static final String DELETE_ELECTION_EVENT = "e.delete.election_event";

	public static final String TRANSLATION_LOCAL = "e.translation.local";
	public static final String TRANSLATION_GLOBAL = "e.translation.global";

	public static final String OPERATOR_EXPORT = "e.rbac.import_export.export";
	public static final String OPERATOR_IMPORT = "e.rbac.import_export.import";

	public static final String ISSUING_POINT_CREATE_ASSERTION = "e.voting.issuing_point.assertion_create";

	public static final String OVERRIDE_ACCESS = "e.configuration.override";

	private SecObjEntity() {
		throw new AssertionError();
	}
}
