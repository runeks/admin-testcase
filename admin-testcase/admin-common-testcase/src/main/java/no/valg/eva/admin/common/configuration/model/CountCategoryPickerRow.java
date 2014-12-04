package no.valg.eva.admin.common.configuration.model;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import no.valg.eva.admin.common.counting.model.CountCategory;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CountCategoryPickerRow implements PickerRow {
	private CountCategory countCategory;

	public CountCategoryPickerRow(CountCategory countCategory) {
		this.countCategory = countCategory;
	}

	public CountCategory getCountCategory() {
		return countCategory;
	}

	@Override
	public String getId() {
		return countCategory.getId();
	}

	@Override
	public String getName() {
		return countCategory.getName();
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
		CountCategoryPickerRow rhs = (CountCategoryPickerRow) obj;
		return new EqualsBuilder()
				.append(this.countCategory, rhs.countCategory)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(countCategory)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
				.append("countCategory", countCategory)
				.toString();
	}
}
