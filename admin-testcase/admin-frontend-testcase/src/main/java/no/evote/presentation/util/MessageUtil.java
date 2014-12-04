package no.evote.presentation.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;

import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;

import no.evote.exception.EvoteException;
import no.evote.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;

public final class MessageUtil {

	public static Map<String, String> getMessageProvider() {
		return new HashMap();
	}

	public enum Action {
		CREATE, UPDATE, LIST, GET_DETAILS, DELETE, RESET
	}

	public static final String CREATE_SUCCESSFUL_KEY = "@common.message.create.successful";
	public static final String CREATE_SUBLEVEL_SUCCESSFUL_KEY = "@common.message.sub_create.successful";
	public static final String DUPLICATE_ID = "@common.message.evote_application_exception.DUPLICATE_ID";
	public static final String CHOOSE_UNIQUE_ID = "@common.message.create.CHOOSE_UNIQUE_ID";
	public static final String CHOOSE_UNIQUE_NAME = "@common.message.create.CHOOSE_UNIQUE_NAME";
	public static final String UPDATE_SUCCESSFUL_KEY = "@common.message.update.successful";
	public static final String DELETE_SUCCESSFUL_KEY = "@common.message.delete.successful";
	public static final String DELETE_FROM_LEVEL_SUCCESSFUL_KEY = "@common.message.sub_delete.successful";
	public static final String DELETE_SUCCESSFUL_WITH_COUNT_KEY = "@common.message.delete.successful_with_count";
	public static final String DELETE_NOT_SUCCESSFUL_KEY = "@common.message.delete.not_successful";
	public static final String DELETE_NOT_SUCCESSFUL_WITH_COUNT_KEY = "@common.message.delete.not_successful_with_count";
	public static final String DELETE_NOT_ALLOWED_KEY = "@common.message.delete.not_allowed";
	public static final String DELETE_NOT_PERFORMED_AS_NONE_SELECTED_KEY = "@common.message.delete.not_performed_as_none_selected";
	public static final String REMOVE_SUCCESSFUL_KEY = "@common.message.remove.successful";
	public static final String REMOVE_SUCCESSFUL_WITH_COUNT_KEY = "@common.message.remove.successful_with_count";
	public static final String REMOVE_NOT_SUCCESSFUL_KEY = "@common.message.remove.not_successful";
	public static final String REMOVE_NOT_SUCCESSFUL_WITH_COUNT_KEY = "@common.message.remove.not_successful_with_count";
	public static final String REMOVE_NOT_ALLOWED_KEY = "@common.message.remove.not_allowed";
	public static final String REMOVE_NOT_PERFORMED_AS_NONE_SELECTED_KEY = "@common.message.remove.not_performed_as_none_selected";
	public static final String FIELD_IS_REQUIRED = "@common.message.required";
	public static final String EDIT_ID_NOT_ALLOWED = "@common.message.edit.id.not.allowed";

	public static final String AREA_ID_MUST_CONFORM = "@area.list_areas.message.id_must_conform";
	public static final String AREA_SELECT_NOT_COMPLETED_KEY = "@area.list_areas.message.select_not_completed";
	public static final String ELECTION_SELECT_NOT_COMPLETED_KEY = "@election.list_elections.message.select_not_completed";

	public static final String EXCEPTION_GENERAL = "@common.message.exception.general";
	public static final String EVOTE_APPLICATION_EXCEPTION_NO_MESSAGE_DEFINED = "@common.message.evote_application_exception.no_message_defined";
	public static final String EXCEPTION_END_DATE_BEFORE_START_DATE_AT_ELECTION_DAY = "@area.polling_place.opening_hours.validate.time_interval.starttime_after_endtime";
	public static final String EXCEPTION_INVALID_TIME = "@area.polling_place.opening_hours.validate.time_interval.INVALID_TIME";

	public static final String VOTING_VOTER_MUST_SPECIAL_COVER = "@voting.search.voterMustCastSpecialCover";
	public static final String VOTING_NUMBER_ENVELOPE = "@voting.markOff.votingNumberEnvelope";
	public static final String VOTER_NOT_APPROVED = "@voting.search.voterNotApproved";
	public static final String INVALID_TIME_INTERVAL = "@area.polling_place.opening_hours.validate.time_interval.illegal";
	public static final String EXCEEDING_TIME_INTERVAL = "@area.polling_place.opening_hours.validate.time_interval.exceed";
	public static final String OVERLAP_TIME_INTERVAL = "@area.polling_place.opening_hours.validate.time_interval.overlap";
	public static final String COMMON_TIME_PATTERN = "@common.date.time_pattern";
	public static final String COMMON_DATE_PATTERN = "@common.date.date_pattern";
	public static final String INCOMPLETE_TIME_INTERVAL = "@area.polling_place.opening_hours.validate.time_interval.incomplete";
	private static FacesContext context;

	private MessageUtil() {
	}

	public static void buildDetailMessage(final FacesContext context, final String message, final Severity severity) {
		String summary = buildTranslatedMessage(context, message, null);

		FacesMessage facesMessage = new FacesMessage();
		facesMessage.setSeverity(severity);
		facesMessage.setSummary(summary);
		context.addMessage(null, facesMessage);
		context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages. Should be controlled by a parameter?
	}

	public static void buildDetailMessage(final String message, final Severity severity) {
		String summary = buildTranslatedMessage(message, null);

		FacesMessage facesMessage = new FacesMessage();
		FacesContext context = getContext();
		facesMessage.setSeverity(severity);
		facesMessage.setSummary(summary);
		context.addMessage(null, facesMessage);
		context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages. Should be controlled by a parameter?
	}

	public static void buildDetailMessage(final Severity serverity, final String... messages) {
		buildDetailMessage(buildString(messages), serverity);
	}
	
	public static void buildMessageForClientId(final String clientId, final String message, final Severity severity) {
		FacesMessage facesMessage = new FacesMessage();
		FacesContext context = getContext();
		facesMessage.setSeverity(severity);
		facesMessage.setSummary(message);
		context.addMessage(clientId, facesMessage);
		context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages. Should be controlled by a parameter?
	}

	public static void buildMessageForClientId(final String clientId, final Severity serverity, final String... messages) {
		buildMessageForClientId(clientId, buildString(messages), serverity);
	}

	private static String buildString(final String... messages) {
		StringBuilder builder = new StringBuilder();
		for (String msg : messages) {
			builder.append(msg);
		}
		return builder.toString();
	}

	/**
	 * Deprecated, use buildFacesMessage(javax.faces.context.FacesContext, java.lang.String, java.lang.String, java.lang.String[],
	 * javax.faces.application.FacesMessage.Severity)()
	 * Inject FacesBroker, use facesBroker.getContext() for finding FacesContext.
	 */
	@Deprecated
	public static void buildFacesMessage(final String clientId, final String summary, final String[] summaryParams, final Severity serverity) {
		if (summaryParams != null) {
			for (int i = 0; i < summaryParams.length; i++) {
				summaryParams[i] = buildTranslatedMessage(summaryParams[i], null);
			}
		}

		String summaryMessage = buildTranslatedMessage(summary, summaryParams);

		FacesMessage facesMessage = new FacesMessage();
		FacesContext context = getContext();
		facesMessage.setSeverity(serverity);
		facesMessage.setSummary(summaryMessage);
		context.addMessage(clientId, facesMessage);
	}

	public static void buildFacesMessage(final FacesContext context, final String clientId, final String summary, final String[] summaryParams,
			final Severity serverity) {
		if (summaryParams != null) {
			for (int i = 0; i < summaryParams.length; i++) {
				summaryParams[i] = buildTranslatedMessage(context, summaryParams[i], null);
			}
		}
		String summaryMessage = buildTranslatedMessage(context, summary, summaryParams);
		FacesMessage facesMessage = new FacesMessage();
		facesMessage.setSeverity(serverity);
		facesMessage.setSummary(summaryMessage);
		context.addMessage(clientId, facesMessage);
	}


	private static String buildTranslatedMessage(final String messageKey, final Object[] params) {
		if (!StringUtils.isEmpty(messageKey) && messageKey.charAt(0) == '@') {
			String translatedMessage = getMessageProvider().get(messageKey);
			if (translatedMessage != null && translatedMessage.trim().length() > 0) {
				return translatedMessage;
			} else {
				return messageKey;
			}
		} else {
			return messageKey;
		}
	}

	private static String buildTranslatedMessage(final FacesContext context, final String messageKey, final Object[] params) {
		if (!StringUtils.isEmpty(messageKey) && messageKey.charAt(0) == '@') {
			String translatedMessage = getMessageProvider().get(messageKey);
			if (translatedMessage != null && translatedMessage.trim().length() > 0) {
				return translatedMessage;
			} else {
				return messageKey;
			}
		} else {
			return messageKey;
		}
	}


	public static void buildDeleteRemoveSelectedDetailMessage(final Object[] selectedArray, final int deleteRemoveCount, final boolean isDelete) {
		String message = "";
		Severity severity;
		String[] params;
		if (selectedArray != null) {
			if (selectedArray.length == deleteRemoveCount) {
				switch (selectedArray.length) {
				case 0:
					if (isDelete) {
						message = buildTranslatedMessage(DELETE_NOT_PERFORMED_AS_NONE_SELECTED_KEY, null);
					} else {
						message = buildTranslatedMessage(REMOVE_NOT_PERFORMED_AS_NONE_SELECTED_KEY, null);
					}
					break;
				case 1:
					if (isDelete) {
						message = buildTranslatedMessage(DELETE_SUCCESSFUL_KEY, null);
					} else {
						message = buildTranslatedMessage(REMOVE_SUCCESSFUL_KEY, null);
					}
					break;
				default:
					params = new String[1];
					params[0] = String.valueOf(selectedArray.length);
					if (isDelete) {
						message = buildTranslatedMessage(DELETE_SUCCESSFUL_WITH_COUNT_KEY, params);
					} else {
						message = buildTranslatedMessage(REMOVE_SUCCESSFUL_WITH_COUNT_KEY, params);
					}
					break;
				}
				severity = FacesMessage.SEVERITY_INFO;
			} else {
				params = new String[2];
				params[0] = String.valueOf(deleteRemoveCount);
				params[1] = String.valueOf(selectedArray.length);
				if (isDelete) {
					message = buildTranslatedMessage(DELETE_NOT_SUCCESSFUL_WITH_COUNT_KEY, params);
				} else {
					message = buildTranslatedMessage(REMOVE_NOT_SUCCESSFUL_WITH_COUNT_KEY, params);
				}
				severity = FacesMessage.SEVERITY_WARN;
			}
		} else {
			if (isDelete) {
				message = buildTranslatedMessage(DELETE_NOT_PERFORMED_AS_NONE_SELECTED_KEY, null);
			} else {
				message = buildTranslatedMessage(REMOVE_NOT_PERFORMED_AS_NONE_SELECTED_KEY, null);
			}
			message = buildTranslatedMessage(DELETE_NOT_PERFORMED_AS_NONE_SELECTED_KEY, null);
			severity = FacesMessage.SEVERITY_INFO;
		}

		buildDetailMessage(message, severity);
	}

	public static void buildMessageFromException(final Exception e, final Action action) {
		Exception exception = e;

		// If we have an EJB exception, get the exception that caused it
		if (exception instanceof EJBException) {
			exception = ((EJBException) exception).getCausedByException();
		}

		Severity severity = null;
		String messageKey = "";
		if (exception instanceof EvoteException) {
			severity = FacesMessage.SEVERITY_ERROR;
			messageKey = exception.getMessage();
		} else if (exception instanceof PersistenceException && exception.getCause() instanceof ConstraintViolationException) {
			MessageUtil.buildDetailMessage(DUPLICATE_ID, FacesMessage.SEVERITY_ERROR);
		} else {
			severity = FacesMessage.SEVERITY_FATAL;
			messageKey = EXCEPTION_GENERAL;
		}

		buildDetailMessage(messageKey, severity);
	}

	public static void buildFacesMessageFromException(final String clientId, final Exception e) {
		Exception exception = e;

		// If we have an EJB exception, get the exception that caused it
		if (exception instanceof EJBException) {
			exception = ((EJBException) exception).getCausedByException();
		}

		Severity severity;
		String messageKey = "";
		if (exception instanceof EvoteException) {
			severity = FacesMessage.SEVERITY_ERROR;
			messageKey = exception.getMessage();

			// Provide pretty messages for known errors
			if (messageKey != null && messageKey.contains("violates foreign key constraint \"fk_voting_x_")) {
				messageKey = "@common.message.voting_constraint_error";
			} else if (messageKey != null && messageKey.contains("violates foreign key constraint \"fk_vote_count_x_")) {
				messageKey = "@common.message.vote_count_constraint_error";
			} else if (messageKey != null && messageKey.contains("violates foreign key constraint \"fk_ballot_count_x_")) {
				messageKey = "@common.message.vote_count_constraint_error";
			} else if (messageKey != null && messageKey.contains("violates foreign key constraint \"fk_voter_x_")) {
				messageKey = "@common.message.voter_constraint_error";
			} else if (messageKey != null && messageKey.contains("violates foreign key constraint \"fk_affiliation_vote_count_x_affiliation")) {
				messageKey = "@common.message.affiliation_count_constraint_error";
			}
		} else if (exception instanceof PersistenceException && exception.getCause() instanceof ConstraintViolationException) {
			severity = FacesMessage.SEVERITY_ERROR;
			messageKey = DUPLICATE_ID;
		} else {
			severity = FacesMessage.SEVERITY_FATAL;
			messageKey = EXCEPTION_GENERAL;
		}
		String[] messageParams = { "" };
		buildFacesMessage(clientId, messageKey, messageParams, severity);
	}

	public static String timeString(final Date dateTime) {
		return "";
	}

	public static String dateString(final Date dateTime) {
		return "";
	}

	public static void clearMessages() {
		Iterator<FacesMessage> messages = getContext().getMessages();
		while (messages.hasNext()) {
			messages.next();
			messages.remove();
		}
	}

	private static FacesContext getContext() {
		if (context != null) {
			return context;
		}
		return FacesContext.getCurrentInstance();
	}

	/*
	 * Setters only used for mocking in tests
	 */
	public static void setContext(final FacesContext context) {
		MessageUtil.context = context;
	}
}
