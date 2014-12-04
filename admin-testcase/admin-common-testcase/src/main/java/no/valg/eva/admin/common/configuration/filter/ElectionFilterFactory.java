package no.valg.eva.admin.common.configuration.filter;

import static no.evote.constants.AreaLevelEnum.BOROUGH;
import static no.evote.constants.AreaLevelEnum.MUNICIPALITY;

import no.evote.model.MvElection;

public enum ElectionFilterFactory implements FilterFactory<MvElection> {
	FOR_BF(new Filter<MvElection>() {
		@Override
		public boolean filter(MvElection mvElection) {
			return mvElection.getAreaLevel() == BOROUGH.getLevel();
		}
	}),
	FOR_MUNICIPALITY_LIST_PROPOSALS(new Filter<MvElection>() {
		@Override
		public boolean filter(MvElection mvElection) {
			return mvElection.getAreaLevel() == MUNICIPALITY.getLevel() || mvElection.getAreaLevel() == BOROUGH.getLevel();
		}
	});

	private Filter<MvElection> filter;

	ElectionFilterFactory(Filter<MvElection> filter) {
		this.filter = filter;
	}

	public Filter<MvElection> buildFilter() {
		return filter;
	}
}
