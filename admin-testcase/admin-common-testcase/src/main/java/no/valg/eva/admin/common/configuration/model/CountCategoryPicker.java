package no.valg.eva.admin.common.configuration.model;

import java.util.List;

public class CountCategoryPicker extends AbstractPicker<CountCategoryPickerRow> {
	public CountCategoryPicker(List<CountCategoryPickerRow> pickerRows) {
		super(pickerRows);
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public String getHeaderName() {
		return null;
	}
}
