package no.valg.eva.admin.common.configuration.model;

import no.valg.eva.admin.common.ElectionPath;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ElectionPickerRow implements PickerRow {
	private ElectionPath electionPath;
	private String name;

	public ElectionPickerRow(ElectionPath electionPath, String name) {
		this.electionPath = electionPath;
		this.name = name;
	}

	public ElectionPath getElectionPath() {
		return electionPath;
	}

	@Override
	public String getId() {
		return electionPath.toString();
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
		ElectionPickerRow rhs = (ElectionPickerRow) obj;
		return new EqualsBuilder()
				.append(this.electionPath, rhs.electionPath)
				.append(this.name, rhs.name)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(electionPath)
				.append(name)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("electionPath", electionPath)
				.append("name", name)
				.toString();
	}
}
