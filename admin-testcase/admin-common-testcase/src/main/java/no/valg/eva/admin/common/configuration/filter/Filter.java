package no.valg.eva.admin.common.configuration.filter;

import java.io.Serializable;

public interface Filter<T> extends Serializable {
	boolean filter(T t);
}
