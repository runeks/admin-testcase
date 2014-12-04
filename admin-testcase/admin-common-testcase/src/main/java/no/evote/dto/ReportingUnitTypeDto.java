package no.evote.dto;

import java.io.Serializable;
import java.util.List;

import no.evote.model.MvElection;

public class ReportingUnitTypeDto implements Serializable {

	private long reportingUnitTypePk;
	private int id;
	private int electionLevel;
	private String name;
	private List<MvElection> elections;
	private List<MvElection> selectedElections;

	public String getMvElectionNameString() {
		StringBuffer mvElectionNameString = new StringBuffer("");
		List<MvElection> mveru = getSelectedElections();
		if (mveru != null && !mveru.isEmpty()) {
			for (MvElection mvElection : mveru) {
				if (mvElectionNameString.length() > 0) {
					mvElectionNameString.append(", ");
				}
				mvElectionNameString.append(mvElection.getNamedPath());
			}
		}
		return mvElectionNameString.toString();
	}

	public int getElectionLevel() {
		return electionLevel;
	}

	public void setElectionLevel(final int electionLevel) {
		this.electionLevel = electionLevel;
	}

	public String getElectionLevelName() {
		String electionLevelName = "";
		if (electionLevel == 0) {
			electionLevelName = "@election_level[0].name";
		}
		if (electionLevel == 1) {
			electionLevelName = "@election_level[1].name";
		}
		if (electionLevel == 2) {
			electionLevelName = "@election_level[2].name";
		}
		return electionLevelName;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public List<MvElection> getElections() {
		return elections;
	}

	public void setElections(final List<MvElection> elections) {
		this.elections = elections;
	}

	public List<MvElection> getSelectedElections() {
		return selectedElections;
	}

	public void setSelectedElections(final List<MvElection> selectedElections) {
		this.selectedElections = selectedElections;
	}

	public long getReportingUnitTypePk() {
		return reportingUnitTypePk;
	}

	public void setReportingUnitTypePk(final long reportingUnitTypePk) {
		this.reportingUnitTypePk = reportingUnitTypePk;
	}

}
