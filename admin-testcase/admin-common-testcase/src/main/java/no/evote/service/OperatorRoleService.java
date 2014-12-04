package no.evote.service;

import java.util.List;
import java.util.Map;

import no.evote.model.Access;
import no.evote.model.ElectionEvent;
import no.evote.model.MvArea;
import no.evote.model.MvElection;
import no.evote.model.Operator;
import no.evote.model.OperatorRole;
import no.evote.security.UserData;

public interface OperatorRoleService {

    /**
     * Returns a list containing all OperatorRoles assigned to the Operator
     */
	List<OperatorRole> getOperatorRoles(final Operator operator);

    /**
     * Returns a list with all OperatorRoles that is linked to MvArea descending from the mvArea parameter.
     */
	List<OperatorRole> findDescOperatorsRoles(UserData userData, MvArea mvArea);

	OperatorRole create(UserData userData, OperatorRole operatorRole);

	OperatorRole update(UserData userData, OperatorRole operatorRole);

	void delete(UserData userData, Long operatorRolePk);

	void delete(UserData userData, List<OperatorRole> operatorRoleList);

	List<String> validateOperatorRole(UserData userData, OperatorRole operatorRole);

    /**
     * Returns the number of Role objects that the Operator has.
     */
	Long findUserCountForRole(final Long rolePk);

    /**
     * Returns the OperatorRoles of an operator that is not the exact or below mvArea and mvElection hierarchy as the arguments. Used to decide if an operator
     * can be deleted by another.
     */
	List<OperatorRole> findNotOwnPathOperatorRolesForOperator(UserData userData, MvArea mvArea, MvElection mvElection, Operator operator);

    /**
     * Returns true if the operatorRole is the exact or below mvArea and mvElection hierarchy as the arguments. Used to decide in a particular operatorRole can
     * be edited/deleted by another operator
     */
	boolean decideIfOperatorRoleIsInOwnPath(UserData userData, MvArea mvArea, MvElection mvElection, OperatorRole operatorRole);

    /**
     * Checks if the combination of the mvElection and mvArea is a legal combination according to election_all_area view
     */
	boolean isContextCombinationIllegal(UserData userData, MvElection mvElection, MvArea mvArea);

	List<OperatorRole> findOperatorRolesGivingOperatorAccess(UserData userData, MvArea mvArea, Operator operator, Access data);

	byte[] exportOperatorRoles(UserData userData, Long electionEventPk);

	List<String> importOperatorRoles(UserData userData, Long electionEventPk, byte[] data);

	/**
	 * Returns a map with every operatorRole bellonging to the operatorId, the roles are mapped to the election event on where theyare assigned.
	 * @param userData
	 *            from where the operator id is found
	 */
	Map<ElectionEvent, List<OperatorRole>> getOperatorRolesPerElectionEvent(final UserData userData);
}
