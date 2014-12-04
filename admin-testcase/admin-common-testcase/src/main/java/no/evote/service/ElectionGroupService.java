package no.evote.service;

import java.util.List;

import no.evote.model.ElectionGroup;
import no.evote.security.UserData;

public interface ElectionGroupService {

	ElectionGroup findElectionGroupById(UserData userData, Long electionEventPk, String id);

	List<ElectionGroup> getElectionGroupsSorted(UserData userData, Long electionEventPk);

	ElectionGroup create(UserData userData, ElectionGroup electionGroup);

	ElectionGroup update(UserData userData, ElectionGroup electionGroup);

	void delete(UserData userData, Long electionGroupPk);

	ElectionGroup findByPk(Long pk);

	boolean hasElectronicControlled(long electionGroupPk);

	boolean isElectronicControlledClosed(long electionGroupPk);

	List<ElectionGroup> getElectionGroupsWithoutElections(UserData userData, Long electionEventPk);
}
