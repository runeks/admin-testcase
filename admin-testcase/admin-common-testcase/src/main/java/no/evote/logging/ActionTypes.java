package no.evote.logging;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public final class ActionTypes {

	private static final Logger LOG = Logger.getLogger(ActionTypes.class);

	private static final String FIND = "FIND";
	private static final String UPDATE = "UPDAT";
	private static final String CREATE = "CREAT";
	private static final String DELETE = "DEL";
	private static final String SEARCH = "SEARC";
	private static final String GENERATE = "GEN";
	private static final String MERGE = "MERGE";
	private static final String SAVE = "SAVE";
	private static final String HAS = "HAS";
	private static final String CHECK = "CHECK";
	private static final String INITIAL = "INIT";
	private static final String APPROVE = "APPRV";
	private static final String BUILD = "BUILD";
	private static final String VALIDATE = "VALID";
	private static final String ADD = "ADD";
	private static final String REMOVE = "REMOV";
	private static final String GET = "GET";
	private static final String EXPORT = "EXPRT";
	private static final String IMPORT = "IMPRT";
	private static final String LIST = "LIST";
	private static final String COUNT = "COUNT";
	private static final String CONVERT = "CNVRT";
	private static final String VERIFY = "VERFY";
	private static final String ACTIVATE = "ACTIV";
	private static final String DEACTIVATE = "DACTI";
	private static final String COPY = "COPY";
	private static final String POPULATE = "POPUL";
	private static final String SET = "SET";
	private static final String SWAP = "SWAP";
	private static final String REACTIVATE = "REACT";
	private static final String EXECUTE = "EXECU";
	private static final String REJECT = "REJCT";
	private static final String CLOSE = "CLOSE";
	private static final String SIGN = "SIGN";
	private static final String SCHEDULED = "SCHED";
	private static final String ENCRYPT = "ENCRY";
	private static final String OPEN = "OPEN";
	private static final String UNAPPROVE = "UNAPR";
	private static final String UPLOAD = "UPLOD";

	/*
	ElectionEventServiceImpl.java: unApproveConfiguration not mapped.
	ReportingTemplateServiceImpl.java: uploadTemplate not mapped.
	*/
	private static final Map<String, String> CUSTOM = new HashMap<String, String>() {
		{
			put("reject", REJECT);
			put("close", CLOSE);
			put("collatedCountResults", COUNT);
			put("requestRemoveAllVotingsForVoter", REMOVE);
			put("decideIfOperatorRoleIsInOwnPath", CHECK);
			put("assignVoterNumbers", GENERATE);
		}
	};

	private ActionTypes() {
		// Intentionally empty
	}

	public static String map(final Method method) {
		String methodName = method.getName();
		String mapped = map(methodName);
		if (mapped.equals(methodName)) {
			LOG.warn("Method " + method.getClass() + "." + methodName + " not mapped to action type.");
		}
		return mapped;
	}

	@SuppressWarnings("PMD.NcssMethodCount")
	public static String map(final String methodName) {
		if (methodName.startsWith("find")) {
			return FIND;
		}

		if (methodName.startsWith("update") || methodName.startsWith("edit")) {
			return UPDATE;
		}

		if (methodName.startsWith("create") || methodName.matches("^persist[A-Z].*") || methodName.startsWith("make")) {
			return CREATE;
		}

		if (methodName.startsWith("delete")) {
			return DELETE;
		}

		if (methodName.startsWith("search")) {
			return SEARCH;
		}

		if (methodName.startsWith("generate")) {
			return GENERATE;
		}

		if (methodName.startsWith("merge")) {
			return MERGE;
		}

		if (methodName.startsWith("save")) {
			return SAVE;
		}

		if (methodName.startsWith("initial")) {
			return INITIAL;
		}

		if (methodName.startsWith("sign")) {
			return SIGN;
		}

		if (methodName.startsWith("populate")) {
			return POPULATE;
		}

		if (methodName.startsWith("build")) {
			return BUILD;
		}

		if (methodName.startsWith("approve") || methodName.endsWith("Approve") || "sendToApproval".equals(methodName)) {
			return APPROVE;
		}

		if (methodName.startsWith("validate")) {
			return VALIDATE;
		}

		if (methodName.startsWith("unApprove")) {
			return UNAPPROVE;
		}

		if (methodName.startsWith("execute")) {
			return EXECUTE;
		}

		if (methodName.startsWith("export")) {
			return EXPORT;
		}

		if (methodName.startsWith("import")) {
			return IMPORT;
		}

		if (methodName.startsWith("scheduled")) {
			return SCHEDULED;
		}

		if (methodName.startsWith("count")) {
			return COUNT;
		}

		if (methodName.startsWith("open")) {
			return OPEN;
		}

		if (methodName.startsWith("list")) {
			return LIST;
		}

		if (methodName.startsWith("convert")) {
			return CONVERT;
		}

		if (methodName.startsWith("verify")) {
			return VERIFY;
		}

		if (methodName.startsWith("activate")) {
			return ACTIVATE;
		}

		if (methodName.startsWith("deactivate")) {
			return DEACTIVATE;
		}

		if (methodName.startsWith("reactivate")) {
			return REACTIVATE;
		}

		if (methodName.startsWith("swap")) {
			return SWAP;
		}

		if (methodName.startsWith("encrypt")) {
			return ENCRYPT;
		}

		if (methodName.startsWith("upload")) {
			return UPLOAD;
		}

		if (methodName.startsWith("copy") || methodName.startsWith("duplicate")) {
			return COPY;
		}

		String mapping = regexMappings(methodName);
		if (mapping != null) {
			return mapping;
		}

		if (CUSTOM.containsKey(methodName)) {
			return CUSTOM.get(methodName);
		}

		return methodName;
	}

	private static String regexMappings(final String methodName) {
		if (methodName.matches("^add[A-Z].*")) {
			return ADD;
		}

		if (methodName.matches("^has[A-Z].*")) {
			return HAS;
		}

		if (methodName.matches("^is[A-Z].*") || methodName.startsWith("check") || methodName.endsWith("Exists") || methodName.endsWith("Exist")) {
			return CHECK;
		}

		if (methodName.matches("^rem[A-Z].*") || methodName.matches("^are[A-Z].*") || methodName.startsWith("remove")) {
			return REMOVE;
		}

		if (methodName.matches("^get[A-Z].*")) {
			return GET;
		}

		if (methodName.matches("^set[A-Z].*")) {
			return SET;
		}

		return null;
	}

}
