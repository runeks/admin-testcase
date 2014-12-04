package no.evote.dto;

import java.io.Serializable;

import no.evote.model.MvElection;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class MvElectionMinimal implements Serializable {

	private Long pk;
	private String name;
	private String path;
	private int electionLevel;
	private String id;
	private boolean isContestOnMyLevelOrBelow;
	private int contestStatusId;

	public MvElectionMinimal(final MvElection mvElection, final boolean isContestOnMyLevelOrBelow) {
		this.pk = mvElection.getPk();
		this.name = mvElection.toString();
		this.path = mvElection.getPath();
		this.electionLevel = mvElection.getElectionLevel();
		this.id = mvElection.getElectionLevelId();
		this.isContestOnMyLevelOrBelow = isContestOnMyLevelOrBelow;
		if (mvElection.getContest() != null) {
			this.contestStatusId = mvElection.getContest().getContestStatus().getId();
		}
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(final Long pk) {
		this.pk = pk;
	}

	public int getElectionLevel() {
		return electionLevel;
	}

	public void setElectionLevel(final int electionLevel) {
		this.electionLevel = electionLevel;
	}

	public String getPath() {
		return path;
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public boolean isContestOnMyLevelOrBelow() {
		return isContestOnMyLevelOrBelow;
	}

	public void setContestOnMyLevelOrBelow(final boolean isContestOnMyLevelOrBelow) {
		this.isContestOnMyLevelOrBelow = isContestOnMyLevelOrBelow;
	}

	public int getContestStatusId() {
		return contestStatusId;
	}

	public void setContestStatusId(final int contestStatusId) {
		this.contestStatusId = contestStatusId;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(path).toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		MvElectionMinimal other = (MvElectionMinimal) obj;
		return new EqualsBuilder().append(path, other.path).isEquals();
	}

}
