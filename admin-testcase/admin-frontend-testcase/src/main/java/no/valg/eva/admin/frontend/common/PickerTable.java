package no.valg.eva.admin.frontend.common;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.util.Collections;
import java.util.List;

import no.valg.eva.admin.frontend.common.picker.PickerPanelProvider;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.primefaces.event.SelectEvent;

public class PickerTable {
	private PickerPanelProvider pickerPanelProvider;
	private int level;
	private List<DataRow> dataRows;
	private DataRow selectedDataRow;
	private String id;
	private String headerName;
	private boolean buttonRendered;

	public PickerTable(PickerPanelProvider pickerPanelProvider,
			int level,
			List<DataRow> dataRows,
			DataRow selectedDataRow,
			String id,
			String headerName,
			boolean buttonRendered) {
		this.pickerPanelProvider = pickerPanelProvider;
		this.level = level;
		this.dataRows = dataRows;
		this.selectedDataRow = selectedDataRow;
		this.id = id;
		this.headerName = headerName;
		this.buttonRendered = buttonRendered;
	}

	public PickerTable() {
		this.dataRows = Collections.emptyList();
	}

	public List<DataRow> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<DataRow> dataRows) {
		this.dataRows = dataRows;
	}

	public DataRow getSelectedDataRow() {
		return selectedDataRow;
	}

	@SuppressWarnings("UnusedParameters")
	public void setSelectedDataRow(DataRow selectedDataRow) {
		// Do nothing. selectedDataRow is set in onRowSelect method
	}

	public void onRowSelect(SelectEvent event) {
		this.selectedDataRow = (DataRow) event.getObject();
		this.pickerPanelProvider.updatePickerTables(level + 1);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public boolean isButtonDisabled() {
		return selectedDataRow == null;
	}

	public boolean isButtonRendered() {
		return buttonRendered;
	}

	public void setButtonRendered(boolean buttonRendered) {
		this.buttonRendered = buttonRendered;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		PickerTable rhs = (PickerTable) obj;
		return new EqualsBuilder()
				.append(this.level, rhs.level)
				.append(this.dataRows, rhs.dataRows)
				.append(this.selectedDataRow, rhs.selectedDataRow)
				.append(this.id, rhs.id)
				.append(this.headerName, rhs.headerName)
				.append(this.buttonRendered, rhs.buttonRendered)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(level)
				.append(dataRows)
				.append(selectedDataRow)
				.append(id)
				.append(headerName)
				.append(buttonRendered)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
				.append("level", level)
				.append("dataRows", dataRows)
				.append("selectedDataRow", selectedDataRow)
				.append("id", id)
				.append("headerName", headerName)
				.append("buttonRendered", buttonRendered)
				.toString();
	}

	public static class DataRow {
		private String key;
		private String value;

		public DataRow(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj.getClass() != getClass()) {
				return false;
			}
			DataRow rhs = (DataRow) obj;
			return new EqualsBuilder()
					.append(this.key, rhs.key)
					.append(this.value, rhs.value)
					.isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder()
					.append(key)
					.append(value)
					.toHashCode();
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
					.append("key", key)
					.append("value", value)
					.toString();
		}
	}
}
