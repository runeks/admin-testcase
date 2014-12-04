package no.valg.eva.admin.common.configuration.filter;

import no.evote.model.Borough;
import no.evote.model.MvArea;

public enum BoroughFilterFactory implements FilterFactory<MvArea> {
	FOR_BOROUGH_ELECTION(new Filter<MvArea>() {
		@Override
		public boolean filter(MvArea mvArea) {
			return !mvArea.getBorough().isMunicipality1();
		}
	}),
	FOR_NOT_VO_AND_CENTRAL(new Filter<MvArea>() {
		@Override
		public boolean filter(MvArea mvArea) {
			return mvArea.getBorough().isMunicipality1();
		}
	}),
	FOR_VO_AND_BY_POLLING_DISTRICT_OR_CENTRAL_AND_BY_POLLING_DISTRICT(new Filter<MvArea>() {
		@Override
		public boolean filter(MvArea mvArea) {
			Borough borough = mvArea.getBorough();
			return !borough.isMunicipality1() || borough.hasRegularPollingDistricts();
		}
	}),
	FOR_BY_TECHNICAL_POLLING_DISTRICT(new Filter<MvArea>() {
		@Override
		public boolean filter(MvArea mvArea) {
			Borough borough = mvArea.getBorough();
			return borough.isMunicipality1();
		}
	});
	private Filter<MvArea> filter;

	BoroughFilterFactory(Filter<MvArea> filter) {
		this.filter = filter;
	}

	@Override
	public Filter<MvArea> buildFilter() {
		return filter;
	}
}
