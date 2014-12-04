package no.evote.dto;

import java.io.Serializable;

public class PollingStationDivisionDto implements Serializable {

	private String first;
	private String last;
	private int numberOfVoters;

	public String getFirst() {
		return first;
	}

	public void setFirst(final String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(final String last) {
		this.last = last;
	}

	public int getNumberOfVoters() {
		return numberOfVoters;
	}

	public void setNumberOfVoters(final int numberOfVoters) {
		this.numberOfVoters = numberOfVoters;
	}

	@Override
	public String toString() {
		return first + " - " + last;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((last == null) ? 0 : last.hashCode());
		result = prime * result + numberOfVoters;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		PollingStationDivisionDto other = (PollingStationDivisionDto) obj;

		if (first == null) {
			if (other.first != null) {
				return false;
			}
		} else if (!first.equals(other.first)) {
			return false;
		}
		if (last == null) {
			if (other.last != null) {
				return false;
			}
		} else if (!last.equals(other.last)) {
			return false;
		}
		if (numberOfVoters != other.numberOfVoters) {
			return false;
		}
		return true;
	}

}
