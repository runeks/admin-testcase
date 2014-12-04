package no.valg.eva.admin.common.counting.model;

import java.util.ArrayList;
import java.util.List;

public class DailyMarkOffCounts extends ArrayList<DailyMarkOffCount> {
	private int markOffCount;

	public DailyMarkOffCounts() {
		super();
	}

	public DailyMarkOffCounts(List<DailyMarkOffCount> dailyMarkOffCountList) {
		super();
		for (DailyMarkOffCount dailyMarkOffCount : dailyMarkOffCountList) {
			add(dailyMarkOffCount);
			this.markOffCount += dailyMarkOffCount.getMarkOffCount();
		}
	}

	public int getMarkOffCount() {
		return markOffCount;
	}

	public void setMarkOffCount(int markOffCount) {
		this.markOffCount = markOffCount;
	}
}
