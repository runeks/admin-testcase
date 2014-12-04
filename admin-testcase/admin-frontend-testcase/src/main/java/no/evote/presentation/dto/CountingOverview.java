package no.evote.presentation.dto;

import java.io.Serializable;

import no.evote.constants.VoteCountStatusEnum;

import org.primefaces.model.TreeNode;

public class CountingOverview implements Serializable {
	private TreeNode root;
	private String name;
	private VoteCountStatusEnum voteCountStatus;
	private boolean protocolCountColumnShouldBeRendered;

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(final TreeNode root) {
		this.root = root;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public VoteCountStatusEnum getVoteCountStatus() {
		return voteCountStatus;
	}

	public void setVoteCountStatus(final VoteCountStatusEnum voteCountStatus) {
		this.voteCountStatus = voteCountStatus;
	}

	public boolean isProtocolCountColumnShouldBeRendered() {
		return protocolCountColumnShouldBeRendered;
	}

	public void setProtocolCountColumnShouldBeRendered(boolean protocolCountColumnToBeRendered) {
		this.protocolCountColumnShouldBeRendered = protocolCountColumnToBeRendered;
	}
}
