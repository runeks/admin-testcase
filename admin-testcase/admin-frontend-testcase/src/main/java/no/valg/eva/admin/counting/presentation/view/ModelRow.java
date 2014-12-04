package no.valg.eva.admin.counting.presentation.view;

public interface ModelRow {
	String getTitle();

	Long getCount();

	void setCount(Long count);

	boolean isCountInput();
}
