package no.valg.eva.admin.counting.presentation.view.ballotcount;

public class HeaderRow implements BallotCountsWithSplitModelRow {

	private String title;

	public HeaderRow(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getStyleClass() {
		return "bold";
	}

	@Override
	public boolean isModifiedCountInput() {
		return false;
	}

	@Override
	public void setModifiedCount(Integer modifiedCount) {
		;
	}

	@Override
	public Integer getModifiedCount() {
		return null;
	}

	@Override
	public boolean isUnmodifiedCountInput() {
		return false;
	}

	@Override
	public void setUnmodifiedCount(Integer unmodifiedCount) {
		;
	}

	@Override
	public Integer getUnmodifiedCount() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public Integer getProtocolCount() {
		return null;
	}

	@Override
	public Integer getDiff() {
		return null;
	}

	@Override
	public Long getCount() {
		return null;
	}

	@Override
	public void setCount(Long count) {
		;
	}

	@Override
	public boolean isCountInput() {
		return false;
	}
}
