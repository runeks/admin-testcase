package no.valg.eva.admin.counting.presentation.view;

import java.util.ArrayList;

import no.valg.eva.admin.counting.presentation.PreliminaryCountController;

public class LateValidationCoversModel extends ArrayList<ModelRow> {

	private final PreliminaryCountController ctrl;

	public LateValidationCoversModel(final PreliminaryCountController ctrl) {
		this.ctrl = ctrl;
		add(new ModelRow() {
			@Override
			public String getTitle() {
				return "@count.ballot.electoral_roll";
			}

			@Override
			public Long getCount() {
				return (long) ctrl.getCounts().getMarkOffCount();
			}

			@Override
			public void setCount(Long count) {
				;
			}

			@Override
			public boolean isCountInput() {
				return false;
			}
		});
		add(new ModelRow() {
			@Override
			public String getTitle() {
				return "@count.ballot.late_validation_covers";
			}

			@Override
			public Long getCount() {
				return (long) ctrl.getCount().getLateValidationCovers();
			}

			@Override
			public void setCount(Long count) {
				if (count == null) {
					ctrl.getPreliminaryCount().setLateValidationCovers(null);
				} else {
					ctrl.getPreliminaryCount().setLateValidationCovers(count.intValue());
				}
			}

			@Override
			public boolean isCountInput() {
				return true;
			}
		});
	}

	public int getTotalMarkOffCount() {
		return ctrl.getTotalMarkOffCount();
	}

}
