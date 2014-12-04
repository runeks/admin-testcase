package no.evote.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public final class StringUtil {

	private StringUtil() {
	}

	public static String capitalize(final String s) {
		if (StringUtils.isEmpty(s)) {
			return s;
		}
		return s.charAt(0) + s.substring(1, s.length()).toLowerCase();
	}

	public static String prefixString(final String formatted, final int length, final char prefixChar) {
		int len = formatted.length();
		StringBuilder sb = new StringBuilder("");
		while (len++ < length) {
			sb.append(prefixChar);
		}
		return sb.append(formatted).toString();
	}

	public static String join(final Object[] arr) {
		StringBuffer s = new StringBuffer();
		int len = arr.length;
		if (len > 0) {
			for (int j = 0; j < len - 1; j++) {
				s.append(arr[j]).append(",");
			}
			s.append(arr[len - 1]);
		}
		return s.toString();
	}

	public static String join(final String... strings) {
		return StringUtils.join(strings, " ");
	}

	public static boolean isInSplittedString(final String name, final String query) {
		if (name.startsWith(query)) {
			return true;
		}

		for (String subString : name.split(" ")) {
			if (subString.startsWith(query)) {
				return true;
			}
		}
		return false;
	}

    /**
     * Make list og strings based on arrays.
     */
    public static List<String> asList(String...args) {
        return Arrays.asList(args);
    }
}
