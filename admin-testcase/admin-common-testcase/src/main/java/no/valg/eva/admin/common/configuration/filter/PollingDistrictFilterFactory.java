package no.valg.eva.admin.common.configuration.filter;

import no.evote.model.MvArea;
import no.evote.model.PollingDistrict;

public enum PollingDistrictFilterFactory implements FilterFactory<MvArea> {
	FOR_BY_TECHNICAL_POLLING_DISTRICT(new Filter<MvArea>() {
		@Override
		public boolean filter(MvArea mvArea) {
			return mvArea.getPollingDistrict().isTechnicalPollingDistrict();
		}
	}),
	FOR_CENTRAL_AND_OPERATOR_NOT_ON_POLLING_DISTRICT(new Filter<MvArea>() {
		@Override
		public boolean filter(MvArea mvArea) {
			return mvArea.getPollingDistrict().isMunicipality();
		}
	}),
	FOR_ELECTRONIC_VOTING(new Filter<MvArea>() {
		@Override
		public boolean filter(MvArea mvArea) {
			PollingDistrict pollingDistrict = mvArea.getPollingDistrict();
			return !(pollingDistrict.isParentPollingDistrict() || pollingDistrict.isTechnicalPollingDistrict());
		}
	}),
	DEFAULT(new Filter<MvArea>() {
		@Override
		public boolean filter(MvArea mvArea) {
			PollingDistrict pollingDistrict = mvArea.getPollingDistrict();
			return !(pollingDistrict.isMunicipality() || pollingDistrict.isParentPollingDistrict() || pollingDistrict.isTechnicalPollingDistrict());
		}
	});
	private Filter<MvArea> filter;

	PollingDistrictFilterFactory(Filter<MvArea> filter) {
		this.filter = filter;
	}

	public Filter<MvArea> buildFilter() {
		return filter;
	}
}
