package no.evote.dto;

import java.io.Serializable;
import java.util.List;

import no.evote.model.Affiliation;
import no.evote.model.Candidate;
import no.evote.model.Proposer;

public class ListProposalValidationData implements Serializable {

	private List<Candidate> candidateList;
	private List<Proposer> proposerList;
	private Affiliation affiliation;
	private boolean isApproved;

	public ListProposalValidationData(final List<Candidate> candidateList, final List<Proposer> proposerList, final Affiliation affiliation) {
		this.candidateList = candidateList;
		this.proposerList = proposerList;
		this.affiliation = affiliation;
		this.isApproved = true;
	}

	public Affiliation getAffiliation() {
		return affiliation;
	}

	public void setAffiliatoin(final Affiliation affiliatoin) {
		this.affiliation = affiliatoin;
	}

	public List<Candidate> getCandidateList() {
		return candidateList;
	}

	public List<Proposer> getProposerList() {
		return proposerList;
	}

	public void setCandidateList(final List<Candidate> candidateList) {
		this.candidateList = candidateList;
	}

	public void setProposerList(final List<Proposer> proposerList) {
		this.proposerList = proposerList;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(final boolean isApproved) {
		this.isApproved = isApproved;
	}
}
