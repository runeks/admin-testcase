package no.valg.eva.admin.common.counting.model.settlement;

import java.io.Serializable;

import no.valg.eva.admin.common.counting.model.CountCategory;

public interface BallotCount extends Serializable {
	CountCategory getCountCategory();

	Integer getModifiedBallotCount();

	Integer getUnmodifiedBallotCount();

	int getBallotCount();
}
