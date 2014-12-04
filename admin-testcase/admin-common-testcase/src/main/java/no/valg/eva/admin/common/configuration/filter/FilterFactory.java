package no.valg.eva.admin.common.configuration.filter;

public interface FilterFactory<T> {
	Filter<T> buildFilter();
}
