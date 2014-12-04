package no.valg.eva.admin.counting.presentation;

public interface TopInfoProvider {
	/**
	 * @return Area name intended for display - taking into account that the 0000 polling districts represents the municipality
	 */
	String getDisplayAreaName();

	String getMunicipalityName();

	String getElectionName();

	String getCategoryName();
}
