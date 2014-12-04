package no.evote.service;

import java.io.Serializable;
import java.util.List;

import no.evote.model.Contest;
import no.evote.model.ReferendumOption;
import no.evote.security.UserData;

public interface ReferendumOptionService extends Serializable {

	List<ReferendumOption> findByContest(UserData userData, Long contestPk);

	ReferendumOption createNewReferendumOptionList(UserData userData, Contest contest);

	List<String> saveReferendumOptions(UserData userData, Contest contest,
			List<ReferendumOption> referendumOptionList, List<ReferendumOption> removedReferendumOptionList);

	List<ReferendumOption> setCorrectDisplayOrder(UserData userData, List<ReferendumOption> createdisReferendumOptionIdInLists);

	ReferendumOption update(UserData userData, ReferendumOption referendumOption);

	String isReferendumOptionValid(UserData userData, ReferendumOption refOptEmpty);

	List<String> validateReferendumOption(UserData userData, List<ReferendumOption> referendumOptionList);

	void delete(UserData userData, Long pk);

	<T> void deleteReferendumOptions(UserData userData, List<ReferendumOption> referendumOptions);

	List<ReferendumOption> updateReferendumOptions(UserData userData, final List<ReferendumOption> refOptionList);

}
