package no.valg.eva.admin.common.counting.model.modifiedballots;

import java.io.Serializable;

public class Candidate implements Serializable, Comparable<Candidate> {

	private String name;
	private String partyName;
	private int displayOrder;
	private CandidateRef candidateRef;

	public Candidate(String name, CandidateRef candidateRef, String partyName, int displayOrder) {
		this(name, candidateRef, partyName);
		this.displayOrder = displayOrder;
	}

	public Candidate(CandidateRef candidateRef) {
		this.candidateRef = candidateRef;
	}

	public Candidate(String name, CandidateRef candidateRef, String partyName) {
		this(candidateRef);
		this.name = name;
		this.partyName = partyName;
	}

	public String getName() {
		return name;
	}

	public String getPartyName() {
		return partyName;
	}

	public CandidateRef getCandidateRef() {
		return candidateRef;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	@Override
	public String toString() {
		return "Candidate{"
				+ "name='" + name + '\''
				+ ", partyName='" + partyName + '\''
				+ ", candidateRef=" + candidateRef
				+ ", displayOrder" + displayOrder
				+ '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Candidate candidate = (Candidate) o;

		if (candidateRef != null ? !candidateRef.equals(candidate.candidateRef) : candidate.candidateRef != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return candidateRef != null ? candidateRef.hashCode() : 0;
	}

	@Override
	public int compareTo(Candidate other) {
		return this.getDisplayOrder() - other.getDisplayOrder();
	}
}
