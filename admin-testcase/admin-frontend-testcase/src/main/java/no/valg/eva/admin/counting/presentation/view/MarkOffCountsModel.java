package no.valg.eva.admin.counting.presentation.view;

import java.io.Serializable;
import java.util.ArrayList;

import no.valg.eva.admin.counting.presentation.CountController;
import no.valg.eva.admin.frontend.common.DiffUtil;

public class MarkOffCountsModel extends ArrayList<MarkOffCountsModel.MarkOffCountsRow> implements Serializable {

	private CountController ctrl;

	public MarkOffCountsModel(CountController ctrl) {
		this.ctrl = ctrl;
		add(new MarkOffCountsRow(ctrl));
	}

	public boolean isShowProtocolCount() {
		return ctrl.isIncludeProtocolCount();
	}

	public static class MarkOffCountsRow {
		private CountController ctrl;

		public MarkOffCountsRow(CountController ctrl) {
			this.ctrl = ctrl;
		}

		public int getTotalProtocolCount() {
			return ctrl.getCounts().getTotalBallotCountForProtocolCounts();
		}

		public int getTotalMarkOffCount() {
			return ctrl.getTotalMarkOffCount();
		}

		public int getTotalBallotCount() {
			return ctrl.getCount().getTotalBallotCount();
		}

		public int getTotalBallotCountDifferenceFromPreviousCount() {
			return ctrl.getTotalBallotCountDifferenceFromPreviousCount();
		}

		public String getDiffStyleClass() {
			return DiffUtil.getClass(getTotalBallotCountDifferenceFromPreviousCount());
		}
	}
}
