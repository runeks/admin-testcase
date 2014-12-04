package no.valg.eva.admin.frontend.common.dialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.context.RequestContext;

/**
 * Utility class for handling of front-end dialogs
 */
public final class DialogUtils {

	private DialogUtils() {
		;
	}

	public static void openModalDialog(String dialogPath, Map<String, List<String>> dialogParams) {
		Map<String, Object> dialogOptions = getDefaultModalDialogOptions();
		openDialog(dialogPath, dialogOptions, dialogParams);
	}

	public static Map<String, Object> getDefaultModalDialogOptions() {
		Map<String, Object> dialogOptions = new HashMap<>();
		dialogOptions.put("modal", true);
		dialogOptions.put("draggable", true);
		dialogOptions.put("resizable", true);
		return dialogOptions;
	}

	public static void openDialog(String dialogPath, Map<String, Object> dialogOptions, Map<String, List<String>> dialogParams) {
		RequestContext.getCurrentInstance().openDialog(dialogPath, dialogOptions, dialogParams);
	}
}
