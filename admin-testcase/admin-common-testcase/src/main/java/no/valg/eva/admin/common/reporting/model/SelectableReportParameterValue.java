package no.valg.eva.admin.common.reporting.model;

import java.io.Serializable;

/**
 * @since 21.02.14.
 */
public class SelectableReportParameterValue implements Serializable {
	private String valueId;
	private String label;

	public SelectableReportParameterValue(String valueId, String label) {
		this.valueId = valueId;
		this.label = label;
	}

	public String getValueId() {
		return valueId;
	}

	public String getLabel() {
		return label;
	}
}
