package no.valg.eva.admin.frontend.common;

public final class DiffUtil {

	private DiffUtil() {
	}

	public static final String getClass(int diff) {
		if (diff > 0) {
			return "diff-pos";
		} else if (diff < 0) {
			return "diff-neg";
		} else {
			return "diff-zero";
		}
	}

}
