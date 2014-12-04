package no.valg.eva.admin.common.configuration.model;

import java.io.Serializable;
import java.util.List;

public interface Picker<R extends PickerRow> extends Serializable {
	String getId();
	String getHeaderName();
	List<R> getPickerRows();
}
