package no.evote.util;

import org.apache.commons.lang3.ObjectUtils;

public final class LangUtil {

	private LangUtil() {
		// Intentionally empty
	}

	@SuppressWarnings("unchecked")
	public static <T> T uncheckedCast(final Object o) {
		return (T) o;
	}
	
	public static Integer zeroIfNull(Integer potentiallyNullNumber) {
		return ObjectUtils.defaultIfNull(potentiallyNullNumber, 0);
	}
}
