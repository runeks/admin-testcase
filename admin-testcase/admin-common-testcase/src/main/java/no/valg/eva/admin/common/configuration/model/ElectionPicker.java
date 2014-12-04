package no.valg.eva.admin.common.configuration.model;

import java.util.List;

import no.evote.constants.ElectionLevelEnum;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ElectionPicker extends AbstractPicker<ElectionPickerRow> {
	private ElectionLevelEnum electionLevel;

	public ElectionPicker(ElectionLevelEnum electionLevel, List<ElectionPickerRow> electionPickerRows) {
		super(electionPickerRows);
		this.electionLevel = electionLevel;
	}

	@Override
	public String getId() {
		return electionLevel.name();
	}

	@Override
	public String getHeaderName() {
		return "@election_level[" + electionLevel.getLevel() + "].name";
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
		ElectionPicker rhs = (ElectionPicker) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(this.electionLevel, rhs.electionLevel)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(electionLevel)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.appendSuper(super.toString())
				.append("electionLevel", electionLevel)
				.toString();
	}
}
