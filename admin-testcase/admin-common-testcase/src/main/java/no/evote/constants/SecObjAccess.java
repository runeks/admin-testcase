package no.evote.constants;

/**
 * @since 13.03.14.
 */
// CSOFF: InterfaceIsTypeCheck
public interface SecObjAccess {
	String COUNT_ADVANCE_VOTES_READ = "a.count.ballot_early_voting.read";
	String COUNT_ADVANCE_VOTES_EDIT = "a.count.ballot_early_voting.edit";
	String COUNT_ELECTION_DAY_VOTES_READ = "a.count.ballot_election_day.read";
	String COUNT_ELECTION_DAY_VOTES_EDIT = "a.count.ballot_election_day.edit";
	String COUNT_REGISTER_CORRECTED = "a.count.ballot_register_corrected";
	String COUNT_REVOKE_PRELIMINARY = "a.count.revoke_preliminary";
	String COUNT_REVOKE_FINAL = "a.count.revoke_final";
	
	String REPORT_CATEGORY_CONFIGURATION = "w.reporting.links.configuration";
	String REPORT_CATEGORY_EARLY_VOTES = "w.reporting.links.early_votes";
	String REPORT_CATEGORY_ELECTORAL_ROLL = "w.reporting.links.electoral_roll";
	String REPORT_CATEGORY_LIST_PROPOSAL = "w.reporting.links.list_proposal";
	String REPORT_CATEGORY_PROTOCOLS = "w.reporting.links.protocols";
	String REPORT_CATEGORY_SETTLEMENT = "w.reporting.links.settlement";
	String REPORT_CATEGORY_USERS_AND_ROLES = "w.reporting.links.users_and_roles";
	String REPORT_CATEGORY_VOTE_COUNT = "w.reporting.links.vote_count";
	String REPORT_CATEGORY_MISC = "w.reporting.links.misc";
}
// CSON: InterfaceIsTypeCheck
