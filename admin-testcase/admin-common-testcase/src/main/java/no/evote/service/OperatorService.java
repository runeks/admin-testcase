package no.evote.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import no.evote.model.Access;
import no.evote.model.ElectionEvent;
import no.evote.model.MvArea;
import no.evote.model.Operator;
import no.evote.model.OperatorRole;
import no.evote.model.Party;
import no.evote.model.Role;
import no.evote.model.views.OperatorTask;
import no.evote.security.UserData;

public interface OperatorService extends Serializable {

	Operator findByElectionEventsAndId(final Long electionEventPk, final String operatorId);

	/**
	 * Returns operators at exact or below mvArea as argument Used to list operators where securable object 'e.rbac.operator_see_below' is present for logged in
	 * role. If the current user has access on the topmost geographical level, all operators are returned, including operators that don't have any roles.
	 */
	List<Operator> findDescOperators(UserData userData, MvArea mvArea);

	List<Operator> findOperatorsById(final String id);

	/**
	 * Deletes all of the operators roles that the user has access to. If this is all of the operators roles the operator itself is then deleted
	 */
	void deleteOperator(UserData userData, Operator currentOperator);

	/**
	 * Updates the data in the row corresponding to the currentOperator. Updates the data for every operatorRole in the operatorRoles. If, after the updates,
	 * the operator turns out with no OperatorRoles, the operator is deleted
	 */
	Operator updateOperator(UserData userData, Operator currentOperator, List<OperatorRole> operatorRoles);

	/**
	 * Persists an operator and all the OperatorRoles assigned to it TODO Change name to persistOperatorAndRoles?
	 */
	void persistOperator(UserData userData, Operator currentOperator, List<OperatorRole> operatorRoles);

	/**
	 * @return true if the operator has a party affiliation
	 */
	boolean hasOperatorsParty(UserData userData, Long partyPk);

	Operator create(UserData userData, Operator operator);

	/**
	 * Returns the party affiliation for an operator
	 */
	Party findPartyForOperator(UserData userData, Long operatorPk);

	/**
	 * Validates operator data. Also checks that OperatorId and reference ElectionEvent is unique.
	 */
	List<String> validateOperator(UserData userData, Operator currentOperator2, ElectionEvent event);

	List<OperatorTask> getOperatorTaskList(UserData userData);

	List<OperatorTask> getOperatorTaskList(UserData userData, Integer first, Integer count);

	/**
	 * Returns operators at exact mvArea as argument Used to list operators where securable object 'e.rbac.operator_see_below' is not present for logged in role
	 * Note: Misleading name?
	 */
	List<Operator> findOperatorsAtOwnLevel(UserData userData, MvArea mvArea);

    List<Operator> findOperatorsWithAccess(UserData userData, MvArea mvArea, Access data);

    /**
	 * @return true if operator for uid exists
	 */
	boolean hasOperator(String uid);

	/**
	 * Returns a set of operators that has operatorRoles that would becoming illegal if the role where to be set as mutually exclusive
	 */
	Set<Operator> getCollisonsIfSetMutEx(UserData userData, Role currentRole);
}
