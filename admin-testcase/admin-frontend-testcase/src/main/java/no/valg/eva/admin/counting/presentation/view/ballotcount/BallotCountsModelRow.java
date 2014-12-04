package no.valg.eva.admin.counting.presentation.view.ballotcount;

import no.valg.eva.admin.counting.presentation.view.ModelRow;

public interface BallotCountsModelRow extends ModelRow {

	String getId();

	Integer getProtocolCount();

	Integer getDiff();

	String getStyleClass();

}
