package no.evote.logging;

import java.util.Arrays;
import java.util.Iterator;

import no.evote.model.BaseEntity;
import no.evote.security.UserData;
import no.evote.service.AuditLoggerService;

public final class AuditLogUtil {

	private AuditLogUtil() {
		// Intentionally left empty
	}

	public static String generateId(final Object... args) {
		if (args.length == 0) {
			return "";
		}
		return generateIdWithSeparator(Arrays.asList(args), AuditLoggerService.ID_SEPARATOR);
	}

	public static String generateIdWithSeparator(final Iterable<?> args, final char separator) {
		Iterator<?> iter = args.iterator();
		StringBuilder builder = new StringBuilder();
		builder.append(valueOf(iter.next()));

		while (iter.hasNext()) {
			builder.append(separator).append(valueOf(iter.next()));
		}

		return "<" + builder.toString() + ">";
	}

	private static String valueOf(final Object o) {
		String idToBeLogged;
		if (o instanceof BaseEntity) {
			idToBeLogged = String.valueOf(((BaseEntity) o).getPk());
		} else if (o instanceof UserData) {
			idToBeLogged = UserData.class.getName();
		} else {
			idToBeLogged = String.valueOf(o);
		}
		return idToBeLogged;
	}
}
