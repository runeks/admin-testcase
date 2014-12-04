package no.evote.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import no.evote.security.ContextSecurable;
import no.evote.util.StringUtil;

import org.apache.commons.lang.StringUtils;

public abstract class ProposalPerson extends VersionedEntity implements ContextSecurable {
	public abstract String getId();

	public abstract void setId(String string);

	public abstract boolean isIdSet();

	/**
	 * @return true if invalid else false
	 */
	public abstract boolean isInValid();

	public abstract Date getDateOfBirth();

	public abstract Ballot getBallot();

	public abstract int getDisplayOrder();

	public abstract String getPostalCode();

	public abstract void setPostTown(String postTown);

	public abstract void setDisplayOrder(int i);

	public abstract void setNameLine(String nameline);

	public abstract String getFirstName();

	public abstract String getMiddleName();

	public abstract String getLastName();

	public abstract void addValidationMessage(String string);

	public abstract List<String> getValidationMessageList();

	public abstract void clearValidationMessages();

	public abstract void setApproved(boolean approved);

	public String getValidationMessage() {
		StringBuffer validationMsg = new StringBuffer("");
		Iterator<String> it = getValidationMessageList().iterator();
		while (it.hasNext()) {
			validationMsg.append(it.next());
			if (it.hasNext()) {
				validationMsg.append(", ");
			}
		}
		return StringUtil.capitalize(validationMsg.toString());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		appendName(builder, getFirstName());
		appendName(builder, getMiddleName());
		appendName(builder, getLastName());
		return builder.toString();
	}

	private void appendName(final StringBuilder builder, final String name) {
		if (!StringUtils.isEmpty(name)) {
			appendSpaceIfNotEmpty(builder);
			builder.append(name);
		}
	}

	private void appendSpaceIfNotEmpty(final StringBuilder builder) {
		if (builder.length() != 0) {
			builder.append(" ");
		}
	}

	public void setNameLine() {
		setNameLine(toString());
	}

	public boolean isCreated() {
		return getPk() != null;
	}

	public boolean hasSearchInformation() {
		return !StringUtils.isEmpty(getFirstName()) || !StringUtils.isEmpty(getLastName()) || getDateOfBirth() != null;
	}

}
