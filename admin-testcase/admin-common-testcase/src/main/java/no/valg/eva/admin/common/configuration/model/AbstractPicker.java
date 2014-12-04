package no.valg.eva.admin.common.configuration.model;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class AbstractPicker<R extends PickerRow> implements Picker<R> {
	private List<R> pickerRows;

	protected AbstractPicker(List<R> pickerRows) {
		this.pickerRows = pickerRows;
	}

	public List<R> getPickerRows() {
		return pickerRows;
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
		AbstractPicker rhs = (AbstractPicker) obj;
		return new EqualsBuilder()
				.append(this.pickerRows, rhs.pickerRows)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(pickerRows)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("pickerRows", pickerRows)
				.toString();
	}
}
