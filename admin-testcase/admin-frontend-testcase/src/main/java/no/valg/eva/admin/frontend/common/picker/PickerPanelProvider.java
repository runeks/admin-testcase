package no.valg.eva.admin.frontend.common.picker;

import java.io.Serializable;
import java.util.List;

import no.valg.eva.admin.frontend.common.PickerTable;

public interface PickerPanelProvider extends Serializable {
	boolean isPickerPanelRendered();

	void setPickerPanelRendered(boolean pickerPanelRendered);

	String getId();

	String getHeaderName();

	List<PickerTable> getPickerTables();

	void updatePickerTables(int level);

	void initPickerTables();

	void initPickerTablesAndSelectedDataRow();

	PickerPanelProvider getPreviousPickerPanelProvider();

	void setPreviousPickerPanelProvider(PickerPanelProvider previousPickerPanelProvider);

	<T extends PickerPanelProvider> T findPreviousPickerPanelProvider(Class<T> expectedPickerPanelProviderType);

	PickerPanelProvider getNextPickerPanelProvider();

	void setNextPickerPanelProvider(PickerPanelProvider nextPickerPanelProvider);

	void selectDataRow();
}
