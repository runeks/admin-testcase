package no.valg.eva.admin.common.counting.model;

/**
 * Categories for counting.
 */
public enum CountCategory {
	/** Valgtingsstemmer, ordinære */
	VO(false, false),
	/** Fremmedstemmer */
	VF(false, false),
	/** Stemmer i særskilt omslag */
	VS(false, false),
	/** Stemmer i beredskapskonvolutt */
	VB(false, false),
	/** Forhåndsstemmer, ordinære */
	FO(true, false),
	/** Sent innkomne/lagt til side*/
	FS(true, false),
	/** Elektroniske stemmer, ukontrollerte omgivelser */
	EO(true, true),
	/** Elektroniske stemmer, kontrollert miljø */
	EK(false, true),
	/** Valg til bydelsutvalg, fremmedstemmer (Fra andre bydeler) */
	BF(false, false);

	/**
	 * Convenience method wrapping standard valueOf(String) method.
	 * @return CountCategory matching specified id
	 */
	public static CountCategory fromId(String id) {
		return CountCategory.valueOf(id);
	}

	private final boolean earlyVoting;
	private final boolean electronicVoting;

	private CountCategory(boolean earlyVoting, boolean electronicVoting) {
		this.earlyVoting = earlyVoting;
		this.electronicVoting = electronicVoting;
	}

	/**
	 * @return true if category is for early voting, false otherwise
	 */
	public boolean isEarlyVoting() {
		return earlyVoting;
	}

	/**
	 * @return true if category is for electronic voting, false otherwise
	 */
	public boolean isElectronicVoting() {
		return electronicVoting;
	}

	/**
	 * @return text id for this category used in GUI
	 */
	public String getName() {
		return "@vote_count_category[" + name() + "].name";
	}

	/**
	 * Convenience method for getting the category's id wrapping standard name() method.
	 * @return the category's id mapped in the database
	 */
	public String getId() {
		return name();
	}
}
