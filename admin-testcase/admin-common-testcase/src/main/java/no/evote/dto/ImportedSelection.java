package no.evote.dto;

import java.io.Serializable;

public class ImportedSelection implements Serializable {

	private String ballotIdentifier;
	private String category;
	private int unmodified;
	private int modified;

	public ImportedSelection(final String ballotIdentifier, final String category, final int unmodified, final int modified) {
		super();
		this.ballotIdentifier = ballotIdentifier;
		this.category = category;
		this.unmodified = unmodified;
		this.modified = modified;
	}

	public String getBallotIdentifier() {
		return ballotIdentifier;
	}

	public void setBallotIdentifier(final String ballotIdentifier) {
		this.ballotIdentifier = ballotIdentifier;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

	public int getUnmodified() {
		return unmodified;
	}

	public void setUnmodified(final int unmodified) {
		this.unmodified = unmodified;
	}

	public int getModified() {
		return modified;
	}

	public void setModified(final int modified) {
		this.modified = modified;
	}

}
