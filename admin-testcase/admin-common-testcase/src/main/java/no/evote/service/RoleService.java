package no.evote.service;

import java.util.List;
import java.util.Set;

import no.evote.model.Access;
import no.evote.model.ElectionEvent;
import no.evote.model.OperatorRole;
import no.evote.model.Role;
import no.evote.security.UserData;
import no.valg.eva.admin.common.FindByPkRequest;
import no.valg.eva.admin.common.rbac.CircularReferenceCheckRequest;
import no.valg.eva.admin.common.rbac.PersistRoleResponse;

public interface RoleService {

	List<Role> findAllRolesInElectionEvent(UserData userData, ElectionEvent event);

	/**
	 * @param userData
	 * @return all roles as view objects for user/election event
	 */
	List<no.valg.eva.admin.common.rbac.Role> findAllRolesWithoutAccessesForView(UserData userData);

	Set<Role> getRoleWithIncludedRoles(Role role);

	Integer getAccumulatedSecLevel(Role role);

	Integer getAccumulatedSecLevelFor(UserData userData, FindByPkRequest findByPkRequest);

	Role findByElectionEventAndId(ElectionEvent e, String id);

	List<Role> findAssignableRolesForOperatorRole(UserData userData, OperatorRole operatorRole);

	Role create(UserData userData, Role role, boolean electionEventSpecific);

	Role update(UserData userData, Role role);

	/**
	 * Validates and updates Role
	 * 
	 * @param userData
	 * @param role
	 * @return list of validation feedback
	 */
	List<String> updateRole(UserData userData, no.valg.eva.admin.common.rbac.Role role);

	/**
	 * Validates and saves Role
	 * @param userData
	 * @param role
	 * @return list of validation feedback and persisted role
	 */
	PersistRoleResponse persistRole(UserData userData, no.valg.eva.admin.common.rbac.Role role);

	void delete(UserData userData, Long pk);

	Role findByPk(UserData userData, Long pk);

	Set<Access> getAccesses(Role role);

	/**
	 * @param userData
	 * @param findByPkRequest
	 * @return accesses for given role
	 */
	no.valg.eva.admin.common.rbac.Role findRoleWithAccessesForView(UserData userData, FindByPkRequest findByPkRequest);

	Set<Role> getIncludedRoles(Role role);

	/**
	 * @param userData
	 * @param findByPkRequest
	 * @return included roles for given role pk
	 */
	Set<no.valg.eva.admin.common.rbac.Role> findIncludedRolesForView(UserData userData, FindByPkRequest findByPkRequest);

	/**
	 * Export roles and accesses. The resulting string contains one line for each role, on the following format:
	 * 
	 * <pre>
	 * ROLE_ID	ROLE_NAME	SECURITY_LEVEL	MUTUALLY_EXCLUSIVE	ACTIVE	INCLUDED_ROLE1;INCLUDED_ROLE2;...	ACCESS_ID1;ACCESS_ID2;...
	 * </pre>
	 * @param userData
	 * @return
	 */
	String exportRoles(UserData userData, boolean excludeDefaultRoles);

	/**
	 * Import roles given a string on the format as exported by <code>exportRoles</code>
	 * @param userData
	 * @param string
	 * @param deleteExisting
	 *            TODO
	 * @return TODO
	 */
	int importRoles(UserData userData, String string, boolean deleteExisting);

	/**
	 * @param userData
	 * @param circularReferenceCheckRequest
	 * @return true if circular reference, else false
	 */
	boolean isCircularReference(UserData userData, CircularReferenceCheckRequest circularReferenceCheckRequest);

	/**
	 * 
	 * @param userData
	 * @param findByPkRequest
	 * @return view object for role with given pk
	 */
	no.valg.eva.admin.common.rbac.Role findForViewByPk(UserData userData, FindByPkRequest findByPkRequest);
}
