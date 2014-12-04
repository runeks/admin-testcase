package no.valg.eva.admin.common.counting.model;

import static java.lang.String.format;

import java.io.Serializable;

import no.evote.constants.ElectionLevelEnum;
import no.valg.eva.admin.common.ElectionPath;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Context for the current count.
 */
public final class CountContext implements Serializable {
	private final ElectionPath contestPath;
	private final CountCategory category;

	/**
	 * Instantiates a count context with contest path and count category.
	 * 
	 * @param contestPath
	 *            election path to contest for this count
	 * @param category
	 *            count category for this count
	 * @throws java.lang.NullPointerException
	 *             when contest path or count category is null
	 * @throws java.lang.IllegalArgumentException
	 *             when illegal use of contest path or count category
	 */
	public CountContext(ElectionPath contestPath, CountCategory category) {
		this.contestPath = contestPath;
		this.category = category;
		validate();
	}

	/**
	 * Validates count context and throws exception when invalid.
	 * 
	 * @throws java.lang.NullPointerException
	 *             when contest path or count category is null
	 * @throws java.lang.IllegalArgumentException
	 *             when illegal use of contest path or count category
	 */
	public void validate() {
		validateContestPath();
		validateCategory();
	}

	private void validateContestPath() {
		if (contestPath == null) {
			throw new NullPointerException("expected non-null contest path");
		}
		if (contestPath.getLevel() != ElectionLevelEnum.CONTEST) {
			throw new IllegalArgumentException(
					format("expected <%s> election path, but got <%s>", ElectionLevelEnum.CONTEST, contestPath.getLevel()));
		}
	}

	private void validateCategory() {
		if (category == null) {
			throw new NullPointerException("expected non-null category");
		}
	}

	/**
	 * @return path to the contest for the current count
	 */
	public ElectionPath getContestPath() {
		return contestPath;
	}

	/**
	 * @return category for the current count
	 */
	public CountCategory getCategory() {
		return category;
	}
	
	public String getCategoryName() {
		return category.getName();
	}
	
	public boolean isEarlyVoting() { return category.isEarlyVoting(); }

	public boolean isElectronicVoting() {
		return category.isElectronicVoting();
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
		CountContext rhs = (CountContext) obj;
		return new EqualsBuilder()
				.append(this.contestPath, rhs.contestPath)
				.append(this.category, rhs.category)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(contestPath)
				.append(category)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("contestPath", contestPath)
				.append("category", category)
				.toString();
	}
}
