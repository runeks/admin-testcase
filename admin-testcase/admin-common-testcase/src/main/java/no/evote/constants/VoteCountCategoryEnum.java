package no.evote.constants;

/**
 * @deprecated Use {@link no.valg.eva.admin.common.counting.model.CountCategory} instead
 */
public enum VoteCountCategoryEnum {

	/** Elektroniske stemmer, ukontrollerte omgivelser */
	EO(true, true),
	/** Elektroniske stemmer, kontrollert miljø */
	EK(true, true),
	/** Forhåndsstemmer ordinære */
	FO(true, false),
	/** Sent innkomne/lagt til side */
	FS(true, false),
	/** Stemmer i beredskapskonvolutt */
	VB(false, false),
	/** Valgtingsstemmer ordinære */
	VO(false, false),
	/** Stemmer i særskilt omslag */
	VS(false, false),
	/** Fremmedstemmer */
	VF(false, false),
	FP(true, false);

	private final boolean earlyVoting;
	private final boolean electronicVoting;

	VoteCountCategoryEnum(final boolean earlyVoting, final boolean electronicVoting) {
		this.earlyVoting = earlyVoting;
		this.electronicVoting = electronicVoting;
	}

	public boolean isEarlyVoting() {
		return earlyVoting;
	}

	public boolean isElectronicVoting() {
		return electronicVoting;
	}

}
