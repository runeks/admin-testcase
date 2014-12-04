package no.evote.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImportedContest implements Serializable {
	private List<ImportedSelection> importedSelections = new ArrayList<ImportedSelection>();
	private String contestIdentifier;

	public ImportedContest(final String contestIdentifier) {
		super();
		this.contestIdentifier = contestIdentifier;
	}

	public String getContestIdentifier() {
		return contestIdentifier;
	}

	public void setContestIdentifier(final String contestIdentifier) {
		this.contestIdentifier = contestIdentifier;
	}

	public List<ImportedSelection> getImportedSelections() {
		return importedSelections;
	}

	public void setImporteSelections(final List<ImportedSelection> importedSelections) {
		this.importedSelections = importedSelections;
	}

	public void addSelection(final ImportedSelection importedSelection) {
		importedSelections.add(importedSelection);
	}

}
