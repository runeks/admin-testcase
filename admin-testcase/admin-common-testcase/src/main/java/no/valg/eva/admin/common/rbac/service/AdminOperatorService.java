package no.valg.eva.admin.common.rbac.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import no.evote.security.UserData;
import no.valg.eva.admin.common.Area;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.Person;
import no.valg.eva.admin.common.PersonId;
import no.valg.eva.admin.common.rbac.Operator;
import no.valg.eva.admin.common.rbac.RoleAssociation;
import no.valg.eva.admin.common.rbac.RoleItem;

public interface AdminOperatorService extends Serializable {

	/**
	 * Gets an operator.
	 * <p/>
	 * If the user invoking this operation has a role with access {@link no.evote.constants.SecObjEntity.OPERATOR_SEE_BELOW}, then all the operator's role
	 * assignments at and below the user's area is returned. If the user does not have this access, only the operators at the user's selected area (from
	 * UserData) is returned.
	 * <p/>
	 * The election event and the user's selected area are derived from {@code UserData} parameter.
	 *
	 * @param userData   context object
	 * @param operatorId the selected area to find operator within
	 * @return all operators in, and below, the selected area.
	 * @throws NullPointerException    if any arguments are null
	 * @throws no.evote.exception.EvoteException if the operator or area is unknown
	 */
	Operator getOperator(UserData userData, String operatorId);

	/**
	 * Finds operator or voter with the given person id.  If operator is found, the operator is returned, else a search for voter is performed.
	 *
	 * @param userData   context object
	 * @param operatorId personId for operator
	 * @return operator instance
	 */
	Operator findOperatorOrVoterById(UserData userData, PersonId operatorId);

	/**
	 * Finds users in operator table or in voter table.  Only adds instances from voter table
	 * if not an operator with same person id already exists.
	 *
	 * @param userData context object
	 * @param name     name to search for
	 * @return list of person instances
	 */
	Collection<Person> usersByName(UserData userData, String name);

	/**
	 * Finds all operators in a selected area.
	 * <p/>
	 * If the user invoking this operation has a role with access {@link no.evote.constants.SecObjEntity.OPERATOR_SEE_BELOW}, then all operators at and below
	 * the area is returned. If the user does not have this access, only the operators at the specified area is returned. This may be used to restrict view for
	 * county users, while allowing municipality users to see users at area levels below the municipality.
	 * <p/>
	 * The election event is derived from {@code AreaPath} parameter.
	 *
	 * @param userData context object
	 * @param area     the selected area to find operators within
	 * @return all operators in, and below, the selected area. Returns empty list if nothing is found.
	 * @throws NullPointerException    if any arguments are null
	 * @throws no.evote.exception.EvoteException if the operator or area is unknown
	 */
	List<Operator> findAllOperatorsInArea(UserData userData, AreaPath area);

	/**
	 * Updates operator, and role associations within an area.  Creates operator if operator doesn't exist.
	 * <p/>
	 * The election event is found in UserData.
	 *
	 * @param userData                context object
	 * @param operator                the operator to update. Allows updating e-mail and phone number.
	 * @param addedRoleAssociations
	 * @param deletedRoleAssociations role associations to delete. If the role association does not exist in the backend, it is ignored. If the association exists in both
	 *                                newRoleAssociations and deletedRoleAssociations parameters, it is ignored.  @throws java.lang.NullPointerException
	 *                                if any arguments are null
	 * @throws no.evote.exception.EvoteException if the operator or area is unknown
	 */
	void updateOperator(
			UserData userData, AreaPath area, Person operator, Collection<RoleAssociation> addedRoleAssociations, Collection<RoleAssociation> deletedRoleAssociations);

	/**
	 * Deletes operator. Only information within the given area is affected.
	 * <p/>
	 * The election event is found in UserData.
	 *
	 * @param userData context object
	 * @param operator the operator to delete
	 * @throws NullPointerException    if any arguments are null
	 * @throws no.evote.exception.EvoteException if the operator is unknown
	 */
	void deleteOperator(UserData userData, Person operator);

	/**
	 * Finds all roles, as RoleItem instances, for a given election event and area.
	 * If area is county, only roles that can be assigned on county level are returned.
	 * If area is municipality, roles that can be assigned on municipality, polling district or polling place level are returned.
	 *
	 * @param userData context object
	 * @param areaPath path to area, used for finding area level
	 * @return collection of role items
	 */
	Collection<RoleItem> assignableRolesForArea(UserData userData, AreaPath areaPath);

	/**
	 * Finds sub areas for area.
	 *
	 * @param userData context object
	 * @param areaPath path to area, used for finding area level
	 * @return list of sub areas
	 */
	List<Area> subAreasForArea(UserData userData, AreaPath areaPath);

	/**
	 * Finds roles that can be assigned to a given area or subareas defined by areaPath.
	 * In addition, subareas each role can be valid for is returned.
	 *
	 * @param userData context object
	 * @param areaPath path defining area
	 * @return map of role to area lists
	 */
	Map<RoleItem, List<Area>> areasForRole(UserData userData, AreaPath areaPath);
	
	ContactInfo contactInfoForUser(UserData userData);

	void updateContactInfoForUser(UserData userData, ContactInfo contactInfo);

}
