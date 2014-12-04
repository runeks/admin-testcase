package no.evote.constants;

import no.valg.eva.admin.common.configuration.constants.ElectionEventStatusEnum;
import no.valg.eva.admin.common.configuration.constants.MunicipalityStatusEnum;

public final class EvoteConstants {

	public static final String NOT_IN_ELECTORAL_ROLL = "1";
	public static final String DEAD_VOTER = "2";
	public static final String MULTIPLE_VOTES = "3";

	public static final String WARNING_UNCHECKED = "unchecked";
	public static final String WARNING_UNUSED = "unused";

	// public static final String REGEXP_VOTER_ADDRESS = "(([^\\s]+)([A-ZÆØÅÄÖÜa-zæøåäöéü`0-9\\' -/]{0,30}))?";
	public static final String REGEXP_PARTY_NAME = "(([A-ZÆØÅÄÖÜÁČĐŊŠŦŽa-zæøåäöéüáčđŋšŧž _]+)([A-ZÆØÅÄÖÜÁČĐŊŠŦŽa-zæøåäöéüáčđŋšŧž _]{1,50})?)?";
	public static final String REGEXP_PARTY_ID = "(([A-ZÆØÅÄÖÜa-zæøåäöéü_]+)([A-ZÆØÅÄÖÜa-zæøåäöéü_]{1,50})?)?";
	public static final String REGEXP_REMOVE_SEARCH = "['|\"]";

	public static final String CHARACTER_SET = "UTF-8";

	// 60 minutes conversation timeout, needs to be longer than session timeout (30 mins)
	public static final long CONVERSATION_TIMEOUT = (long) 60 * 60 * 1000;

	public static final String POLLING_DISTRICT_EL_BOARD_ROLE = "polling_district_el_board";

	public static final String PUBLIC_GUEST_ROLE = "public_guest";
	public static final String PUBLIC_GUEST_OPERATOR_ID = "00000000004";

	public static final String BALLOT_IMPORT_OPERATOR_ID = "00000000001";
	public static final String BALLOT_IMPORT_ROLE = "ballot_import";
	public static final String WEB_SERVICE_OPERATOR_ID = "00000000002";
	public static final String WEB_SERVICE_ROLE = "web_service";
	public static final String SCHEDULED_IMPORT_OPERATOR_ID = "00000000003";
	public static final String SCHEDULED_IMPORT_ROLE = "scheduled_import";

	public static final String SCHEDULED_PROPOSER_IMPORT_OPERATOR_ID = "00000000001";
	public static final String SCHEDULED_PROPOSER_IMPORT_ROLE = "ballot_import";

	public static final int MAX_CANDIDATES_IN_AFFILIATION = 97;
	public static final int MIN_CANDIDATES_IN_AFFILIATION = 0;
	public static final int MIN_PROPOSERS_NEW_PARTY = 500;
	public static final int MIN_PROPOSERS_OLD_PARTY = 2;
	public static final int MAX_CANDIDATES_NAME_LENGTH = 25;

	public static final int MIN_VOTES = 1;
	public static final int MAX_VOTES = 1;

	public static final int FREEZE_LEVEL_AREA = 2;

	public static final int BALLOT_STATUS_UNDERCONSTRUCTION = 0;
	public static final int BALLOT_STATUS_WITHDRAWN = 1;
	public static final int BALLOT_STATUS_PENDING = 2;
	public static final int BALLOT_STATUS_APPROVED = 3;
	public static final int BALLOT_STATUS_REJECTED = 4;

	public static final Integer ELECTION_EVENT_STATUS_CENTRAL_CONFIGURATION = ElectionEventStatusEnum.CENTRAL_CONFIGURATION.id();
	public static final Integer ELECTION_EVENT_STATUS_LOCAL_CONFIGURATION = ElectionEventStatusEnum.LOCAL_CONFIGURATION.id();
	public static final Integer ELECTION_EVENT_STATUS_FINISHED_CONFIGURATION = ElectionEventStatusEnum.FINISHED_CONFIGURATION.id();
	public static final Integer ELECTION_EVENT_STATUS_APPROVED_CONFIGURATION = ElectionEventStatusEnum.APPROVED_CONFIGURATION.id();
	public static final Integer ELECTION_EVENT_STATUS_CLOSED = ElectionEventStatusEnum.CLOSED.id();

	public static final Integer MUNICIPALITY_STATUS_CENTRAL_CONFIGURATION = MunicipalityStatusEnum.CENTRAL_CONFIGURATION.id();
	public static final Integer MUNICIPALITY_STATUS_LOCAL_CONFIGURATION = MunicipalityStatusEnum.LOCAL_CONFIGURATION.id();
	public static final Integer MUNICIPALITY_STATUS_FINISHED_CONFIGURATION = MunicipalityStatusEnum.FINISHED_CONFIGURATION.id();
	public static final Integer MUNICIPALITY_STATUS_APPROVED_CONFIGURATION = MunicipalityStatusEnum.APPROVED_CONFIGURATION.id();
	public static final Integer MUNICIPALITY_STATUS_CLOSED = MunicipalityStatusEnum.CLOSED.id();

	public static final Integer CONTEST_STATUS_CENTRAL_CONFIGURATION = 0;
	public static final Integer CONTEST_STATUS_LOCAL_CONFIGURATION = 1;
	public static final Integer CONTEST_STATUS_FINISHED_CONFIGURATION = 2;
	public static final Integer CONTEST_STATUS_APPROVED_CONFIGURATION = 3;
	public static final Integer CONTEST_STATUS_CLOSED = 4;

	public static final String VOTING_CATEGORY_VO = "VO";
	public static final String VOTING_CATEGORY_VS = "VS";
	public static final String VOTING_CATEGORY_VF = "VF";
	public static final String VOTING_CATEGORY_FI = "FI";
	public static final String VOTING_CATEGORY_FU = "FU";
	public static final String VOTING_CATEGORY_FA = "FA";
	public static final String VOTING_CATEGORY_FB = "FB";
	public static final String VOTING_CATEGORY_FE = "FE";
	public static final String VOTING_CATEGORY_VB = "VB";
	public static final String VOTING_CATEGORY_EK = "EK";
	public static final String VOTING_CATEGORY_EO = "EO";
	// used to separate late_validation votes from other early votes
	public static final String VOTING_CATEGORY_LATE = "late";

	public static final String VOTE_COUNT_CATEGORY_EO = "EO";
	public static final String VOTE_COUNT_CATEGORY_EK = "EK";
	public static final String VOTE_COUNT_CATEGORY_FO = "FO";
	public static final String VOTE_COUNT_CATEGORY_FS = "FS";
	public static final String VOTE_COUNT_CATEGORY_VB = "VB";
	public static final String VOTE_COUNT_CATEGORY_VO = "VO";
	public static final String VOTE_COUNT_CATEGORY_VS = "VS";
	public static final String VOTE_COUNT_CATEGORY_VF = "VF";

	public static final int VOTE_COUNT_STATUS_COUNTING = 0;
	public static final int VOTE_COUNT_STATUS_TO_APPROVAL = 1;
	public static final int VOTE_COUNT_STATUS_APPROVED = 2;
	public static final int VOTE_COUNT_STATUS_TO_SETTLEMENT = 3;
	public static final int VOTE_COUNT_STATUS_REJECTED = 4;

	public static final String COUNT_QUALIFIER_PROTOCOL = "P";
	public static final String COUNT_QUALIFIER_PRELIMINARY = "F";
	public static final String COUNT_QUALIFIER_FINAL = "E";

	public static final int BATCH_STATUS_IN_QUEUE_ID = 0;
	public static final int BATCH_STATUS_STARTED_ID = 1;
	public static final int BATCH_STATUS_COMPLETED_ID = 2;
	public static final int BATCH_STATUS_FAILED_ID = 3;

	public static final String LOCALE_SEPARATOR = "-";

	public static final long MAX_FILE_SIZE = 250000000;

	public static final String MINID_SECURITY_LEVEL_ATTRIBUTE_NAME = "SecurityLevel";

	public static final String MINID_CULTURE_ATTRIBUTE_NAME = "Culture";

	public static final String MINID_UID_ATTRIBUTE_NAME = "uid";

	public static final String BALLOT_BLANK = "BLANK";

	public static final String ELECTION_TYPE_DIRECT = "D";
	public static final String ELECTION_TYPE_CALCULATED = "F";
	public static final String ELECTION_TYPE_REFERENDUM = "R";

	public static final String SSB_WSDL = "https://evalg.ssb.no:4443/eValg_webservice/eValg.asmx?wsdl";
	public static final String SSB_TARGET_NAMESPACE = "urn:ssb:evalg:service:1.0";
	public static final String SSB_ENDPOINT_NAME = "eValgSoapHttpBinding";
	public static final String SSBREPORT_CODE_ELECTION_DATA = "FG02";
	public static final String SSBREPORT_CODE_ELECTED_CANDIDATES = "FV02";
	public static final String SSBREPORT_CODE_COUNT_MUNICIPALITY = "KV01";
	public static final String SSBREPORT_CODE_COUNT_COUNTY = "CV01";
	public static final String SSBREPORT_CODE_CORRECTED_RESULT = "FV01";
	public static final String SSBREPORT_CODE_SAMI_SSV01 = "SV01";

	public static final String ROOT_ELECTION_EVENT_ID = "000000";

	public static final String PARTY_ID_BLANK = "BLANK";

	public static final String PARTY_CATEGORY_STORTING = "1";
	public static final String PARTY_CATEGORY_LANDSDEKKENDE = "2";
	public static final String PARTY_CATEGORY_LOKALT = "3";

    public static final String VOTE_CATEGORY_WRITEIN = "writein";
    public static final String VOTE_CATEGORY_STRIKEOUT = "strikeout";
	public static final String VOTE_CATEGORY_RENUMBER = "renumber";
    public static final String VOTE_CATEGORY_PERSONAL = "personal";
	public static final String DEFAULT_LOCALE = "nb-NO";

	public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ";
	public static final String ELECTION_EVENT_ADMIN = "election_event_admin";

	public static final int REPORTINGUNIT_TYPE_FYLKESVALGSTYRET = 3;
	public static final int REPORTINGUNIT_TYPE_VALGSTYRET = 4;
	public static final int REPORTINGUNIT_TYPE_STEMMESTYRET = 6;

	public static final String[] DEFAULT_ROLES = new String[] { SCHEDULED_IMPORT_ROLE, PUBLIC_GUEST_ROLE, SCHEDULED_PROPOSER_IMPORT_ROLE, WEB_SERVICE_ROLE,
			ELECTION_EVENT_ADMIN };

	public static final String USER_SUPPORT_ERROR_MSG = "User support does not have write privileges";
	public static final String EXCEL_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.template";
	public static final String EARLY_VOTE_RECEIVER_TEMPLATE_FILE_NAME = "@rbac.uploadAdvanceVoteReceivers.templateFileName";
	public static final String VOTE_RECEIVER_AND_PP_RESPONSIBLE_TEMPLATE_FILE_NAME = "@rbac.uploadVoteReceiversAndPollingPlaceResponsible.templateName";
	public static final String EARLY_VOTE_RECEIVER_TEMPLATE_PATH = "/resources/files/advanceVoteReceiversTemplate.xltx";
	public static final String VOTE_RECEIVER_AND_PP_RESPONSIBLE_TEMPLATE_PATH = "/resources/files/pollingPlaceOperatorsTemplate.xltx";

    public static final int SAML_ASSERTION_VALIDITY_TIME_IN_SECONDS = 60;
	public static final String VALID_EMAIL_REGEXP = "^[A-Za-z0-9_\\-\\.]{2,}@[A-Za-z0-9_\\-\\.]{2,}\\.[a-z]{2,}$";

	private EvoteConstants() {
		throw new AssertionError();
	}
}
