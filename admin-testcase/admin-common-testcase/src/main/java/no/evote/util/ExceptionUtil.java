package no.evote.util;

public final class ExceptionUtil {

	private ExceptionUtil() {
	}

	public static Exception getRootCause(final Exception e) {
		Exception eCurrentException = e;
		Exception eRootException = null;

		while (eCurrentException != null) {
			Exception eNextLevelUp = (Exception) eCurrentException.getCause();
			if (eNextLevelUp == null) {
				eRootException = eCurrentException;
				break;
			} else {
				eCurrentException = eNextLevelUp;
			}
		}
		return eRootException;
	}

	public static String getRootMessage(final Exception e) {
		String rootMessage = getRootCause(e).getMessage();
		return rootMessage == null ? "" : rootMessage;
	}

	public static String buildErrorMessage(final Exception e) {
		Exception eRootException = getRootCause(e);
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(eRootException.getClass());
		stringBuilder.append(" ");
		stringBuilder.append(eRootException.getStackTrace()[0]);
		stringBuilder.append(" ");
		stringBuilder.append(getRootMessage(e));
		return stringBuilder.toString();
	}
}
