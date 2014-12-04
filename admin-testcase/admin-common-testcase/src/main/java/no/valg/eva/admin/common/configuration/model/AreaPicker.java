package no.valg.eva.admin.common.configuration.model;

import java.util.List;

import no.evote.constants.AreaLevelEnum;

public class AreaPicker extends AbstractPicker<AreaPickerRow> {
	private AreaLevelEnum areaLevel;

	public AreaPicker(AreaLevelEnum areaLevel, List<AreaPickerRow> areaPickerRows) {
		super(areaPickerRows);
		this.areaLevel = areaLevel;
	}

	@Override
	public String getId() {
		return areaLevel.name();
	}

	@Override
	public String getHeaderName() {
		return "@area_level[" + areaLevel.getLevel() + "].name";
	}
}
