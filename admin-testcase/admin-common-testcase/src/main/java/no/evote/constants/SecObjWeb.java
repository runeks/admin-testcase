package no.evote.constants;

public final class SecObjWeb {
	public static final String CONFIGURATION_AREA_COUNTRY = "w.configuration.area.country";
	public static final String CONFIGURATION_AREA_COUNTY = "w.configuration.area.county";
	public static final String CONFIGURATION_AREA_MUNICIPALITY = "w.configuration.area.municipality";
	public static final String CONFIGURATION_AREA_BOROUGH = "w.configuration.area.borough";
	public static final String CONFIGURATION_AREA_POLLING_DISTRICT = "w.configuration.area.polling_district";
	public static final String CONFIGURATION_AREA_POLLING_COUNT_DISTRICT = "w.configuration.area.polling_count_district";
	public static final String CONFIGURATION_AREA_POLLING_PLACE = "w.configuration.area.polling_place";
	public static final String CONFIGURATION_CHANGE_AGE_LIMIT = "w.configuration.change_age_limit";
	public static final String CONFIGURATION_ELECTION_ELECTION_GROUP = "w.configuration.election.election_group";
	public static final String CONFIGURATION_ELECTION_ELECTION = "w.configuration.election.election";
	public static final String CONFIGURATION_ELECTION_CONTEST = "w.configuration.election.contest";
    public static final String CONFIGURATION_ELECTION_ELECTION_EVENT = "w.configuration.election.election_event";
    public static final String CONFIGURATION_CENTRAL_CONFIGURATION_OVERVIEW = "w.configuration.central_configuration_overview";
    public static final String CONFIGURATION_ELECTION_VOTE_COUNT_CATEGORY = "w.configuration.election_vote_count_category";
    public static final String CONFIGURATION_DOWNLOAD = "w.configuration.download";
    public static final String CONFIGURATION_AREA_IMPORT = "w.configuration.area_import";
    public static final String CONFIGURATION_REPORTING_COUNT_CATEGORY = "w.configuration.reporting_count_category";
    public static final String CONFIGURATION_REPORTING_UNIT = "w.configuration.reporting_unit";
	public static final String CONFIGURATION_REPORTING_UNIT_TYPE = "w.configuration.reporting_unit_type";
    public static final String CONFIGURATION_LOCAL_CONFIGURATION_OVERVIEW = "w.configuration.local_configuration_overview";
    public static final String CONFIGURATION_LOCAL_CONFIGURATION_OVERVIEW_ELECTION = "w.configuration.local_configuration_overview.election";
    public static final String CONFIGURATION_AREA_POLLING_STATION = "w.configuration.area.polling_station";
    public static final String CONFIGURATION_ENABLE_ELECTION_EVENT = "w.configuration.enable_election_event";

	public static final String COUNT_COUNTY_VOTE_COUNT = "w.count.county_vote_count";
    public static final String COUNT_OVERVIEW = "w.count.overview";
    public static final String COUNT_BARCODE = "e.count.barcode";
    public static final String COUNT_BALLOT_EARLY_VOTING_READ = "a.count.ballot_early_voting.read";
    public static final String COUNT_BALLOT_ELECTION_DAY_READ = "a.count.ballot_election_day.read";
    public static final String COUNT_LEVELING_SEATS = "w.count.leveling_seats";
    public static final String COUNT_APPROVE_REJECTED = "w.count.approve_rejected";

    public static final String COUNT_BALLOT_COUNT_EARLY_VOTING_READ = "e.count.ballot_count.early_voting.read";
    public static final String COUNT_BALLOT_COUNT_ELECTION_DAY_READ = "e.count.ballot_count.election_day.read";
    public static final String BALLOT = "w.ballot";
    public static final String BALLOT_CREATE = "w.ballot.create";

    public static final String VOTING_APPROVE_ADVANCE = "w.voting.approve_advance";
    public static final String VOTING_APPROVE_SPECIAL = "w.voting.approve_special";
    public static final String VOTING_RECEIVE_EARLY_VOTE = "w.voting.receive.early_vote";
    public static final String VOTING_RECEIVE_EMPTY_ELECTION_CARD = "w.voting.receive.emptyElectionCard";
    public static final String VOTING_RECEIVE_ELECTION_DAY_VOTE = "w.voting.receive.election_day_vote";
    public static final String VOTING_RECEIVE_ELECTION_DAY_VOTE_SPECIAL = "w.voting.receive.election_day_vote_special";
    public static final String VOTING_RECEIVE_ELECTION_DAY_EMERGENCY = "w.voting.receive.election_day_emergency";
    public static final String VOTING_RECEIVE_CENTRAL_REGISTRATION = "w.voting.receive.central_registration";

    public static final String ELECTORAL_ROLL_CREATE = "w.electoral_roll.create";
    
    public static final String RBAC_INSPECT = "w.rbac.inspect";
    public static final String RBAC_ROLE = "w.rbac.role";
    public static final String RBAC_IMPORT_EXPORT = "w.rbac.import_export";
    public static final String RBAC_ROLE_EXPORT_IMPORT = "w.rbac.role_export_import";
    public static final String RBAC_OPERATOR = "w.rbac.operator";

    public static final String DELETE_VOTE_COUNTS = "w.delete.vote_counts";
    public static final String DELETE_VOTERS = "w.delete.voters";
    public static final String DELETE_VOTINGS = "w.delete.votings";
    public static final String DELETE_SETTLEMENT = "w.delete.settlement";

    public static final String VOTER_CREATE = "w.voter.create";
    public static final String VOTER_UPDATE = "w.voter.update";
    public static final String VOTER_SEARCH = "w.voter.search";

    public static final String REPORTING_STATISTIC_DATA = "e.reporting.statistic.data";
    public static final String REPORTING_STATISTIC_ELECTION = "e.reporting.statistic.election";
    public static final String REPORTING_STATISTIC_CORRECTED_RESULT = "e.reporting.statistic.corrected_result";
    public static final String REPORTING_REPORT = "e.reporting.report";
    
    public static final String TRANSLATION_LOCAL = "w.translation.local";
    public static final String BATCH_BATCH = "w.batch.batch";
    public static final String CREATE_ELECTION_EVENT = "w.create_election_event";
    public static final String COPY_ROLES = "w.copy_roles";
    public static final String CERTIFICATES_MANAGEMENT = "w.certificates.management";
    public static final String SETTLEMENT_READ = "w.settlement.read";
    public static final String PARTY = "w.party";

    private SecObjWeb() {
		throw new AssertionError();
	}
}
