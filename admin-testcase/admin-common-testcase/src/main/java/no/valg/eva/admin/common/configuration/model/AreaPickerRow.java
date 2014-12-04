package no.valg.eva.admin.common.configuration.model;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import no.valg.eva.admin.common.AreaPath;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AreaPickerRow implements PickerRow {
	private AreaPath areaPath;
	private String name;

	public AreaPickerRow(AreaPath areaPath, String name) {
		this.areaPath = areaPath;
		this.name = name;
	}

	public AreaPath getAreaPath() {
		return areaPath;
	}

	@Override
	public String getId() {
		return areaPath.toString();
	}

	@Override
	public String getName() {
		return name;
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
		AreaPickerRow rhs = (AreaPickerRow) obj;
		return new EqualsBuilder()
				.append(this.areaPath, rhs.areaPath)
				.append(this.name, rhs.name)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(areaPath)
				.append(name)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
				.append("areaPath", areaPath)
				.append("name", name)
				.toString();
	}
}
