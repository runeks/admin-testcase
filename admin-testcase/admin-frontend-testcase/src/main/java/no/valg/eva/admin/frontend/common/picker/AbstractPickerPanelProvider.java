package no.valg.eva.admin.frontend.common.picker;

import java.util.ArrayList;
import java.util.List;

import no.valg.eva.admin.common.configuration.model.AbstractPicker;
import no.valg.eva.admin.common.configuration.model.PickerRow;
import no.valg.eva.admin.frontend.common.PickerTable;

public abstract class AbstractPickerPanelProvider implements PickerPanelProvider {
	private boolean pickerPanelRendered;
	private PickerPanelProvider previousPickerPanelProvider;
	private PickerPanelProvider nextPickerPanelProvider;

	@Override
	public boolean isPickerPanelRendered() {
		return pickerPanelRendered;
	}

	@Override
	public void setPickerPanelRendered(boolean pickerPanelRendered) {
		this.pickerPanelRendered = pickerPanelRendered;
	}

	@Override
	public PickerPanelProvider getPreviousPickerPanelProvider() {
		return previousPickerPanelProvider;
	}

	@Override
	public void setPreviousPickerPanelProvider(PickerPanelProvider previousPickerPanelProvider) {
		this.previousPickerPanelProvider = previousPickerPanelProvider;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends PickerPanelProvider> T findPreviousPickerPanelProvider(Class<T> expectedPickerPanelProviderType) {
		if (previousPickerPanelProvider == null) {
			return null;
		}
		if (expectedPickerPanelProviderType.isAssignableFrom(previousPickerPanelProvider.getClass())) {
			return (T) previousPickerPanelProvider;
		}
		return previousPickerPanelProvider.findPreviousPickerPanelProvider(expectedPickerPanelProviderType);
	}

	@Override
	public PickerPanelProvider getNextPickerPanelProvider() {
		return nextPickerPanelProvider;
	}

	@Override
	public void setNextPickerPanelProvider(PickerPanelProvider nextPickerPanelProvider) {
		this.nextPickerPanelProvider = nextPickerPanelProvider;
	}

	@Override
	public void initPickerTables() {
		updatePickerTables(0);
	}

	@Override
	public void initPickerTablesAndSelectedDataRow() {
		initPickerTables();
		selectDataRow();
	}

	protected <R extends PickerRow, P extends AbstractPicker<R>> PickerTable buildPickerTable(P picker, int level, boolean renderSelectButton) {
		List<PickerTable.DataRow> dataRows = buildDataRows(picker.getPickerRows());
		PickerTable.DataRow selectedDataRow = dataRows.size() == 1 ? dataRows.get(0) : null;
		return new PickerTable(this, level, dataRows, selectedDataRow, picker.getId(), picker.getHeaderName(), renderSelectButton);
	}

	protected List<PickerTable.DataRow> buildDataRows(List<? extends PickerRow> pickerRows) {
		List<PickerTable.DataRow> dataRows = new ArrayList<>();
		for (PickerRow pickerRow : pickerRows) {
			dataRows.add(buildDataRow(pickerRow));
		}
		return dataRows;
	}

	private PickerTable.DataRow buildDataRow(PickerRow pickerRow) {
		return new PickerTable.DataRow(pickerRow.getId(), pickerRow.getName());
	}
}
