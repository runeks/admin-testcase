package no.valg.eva.admin.counting.presentation.view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.faces.context.FacesContext;

import no.valg.eva.admin.common.counting.model.DailyMarkOffCount;
import no.valg.eva.admin.counting.presentation.CountController;
import no.valg.eva.admin.counting.presentation.ProtocolAndPreliminaryCountController;

public class DailyMarkOffCountsModel extends ArrayList<ModelRow> implements Serializable {

	private static final String DATE_PATTERN = "EEEE d MMM. yyyy";

	private final CountController ctrl;

	public DailyMarkOffCountsModel(final CountController ctrl) {
		this.ctrl = ctrl;
		for (final DailyMarkOffCount dailyMarkOffCount : ctrl.getDailyMarkOffCounts()) {
			add(new ModelRow() {
				@Override
				public String getTitle() {
					return new SimpleDateFormat(DATE_PATTERN, getLocale()).format(dailyMarkOffCount.getDate().toDate());
				}

				@Override
				public Long getCount() {
					return dailyMarkOffCount.getMarkOffCount();
				}

				@Override
				public void setCount(Long count) {
					if (isCountInput() && count != null) {
						dailyMarkOffCount.setMarkOffCount(count.longValue());
					}
				}

				@Override
				public boolean isCountInput() {
					return !ctrl.isElectronicMarkOffs();
				}
			});
		}
	}

	public long getSumMarkOffCount() {
		long sum = 0L;
		for (DailyMarkOffCount dailyMarkOffCount : ctrl.getDailyMarkOffCounts()) {
			sum += dailyMarkOffCount.getMarkOffCount();
		}
		return sum;
	}

	public String getPreviousTabTitle() {
		if (ctrl instanceof ProtocolAndPreliminaryCountController) {
			return "@count.tab.type[P].approved";
		}
		return ctrl.getPreviousTab().getTitle();
	}

	Locale getLocale() {
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}
}
