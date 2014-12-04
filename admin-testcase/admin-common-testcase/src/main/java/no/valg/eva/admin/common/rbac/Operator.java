package no.valg.eva.admin.common.rbac;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import no.evote.constants.AreaLevelEnum;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.Person;
import no.valg.eva.admin.common.PersonId;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class Operator extends Person {

	public static final String FILTER_NOT_SET = "0";

	private List<RoleAssociation> roleAssociations = new ArrayList<>();

	public Operator(String operatorId, String firstName, String lastName, String email, String telephoneNumber) {
		super(new PersonId(operatorId), null, firstName, null, lastName, null);
		this.email = email;
		this.telephoneNumber = telephoneNumber;
	}

	public Operator(Operator person) {
		super(person);
		if (person.roleAssociations != null) {
			this.roleAssociations = new ArrayList<>();
			for (RoleAssociation roleAssociation : person.roleAssociations) {
				this.roleAssociations.add(roleAssociation);
			}
		}
	}

	public Operator(Person person) {
		super(person);
	}

	public String name() {
		return nameLine();
	}

	public String getName() {
		return name();
	}

	public List<RoleAssociation> getRoleAssociations() {
		return roleAssociations;
	}

	public void addRoleAssociation(RoleAssociation roleAssociation) {
		roleAssociations.add(roleAssociation);
	}

	/**
	 * @param roleId
	 *            getId of role
	 * @return true if role with role getId is associated with operator, else false
	 */
	public boolean hasRole(String roleId) {
		for (RoleAssociation ra : roleAssociations) {
			if (ra.getRole().getRoleId().equals(roleId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param areaPathFilter
	 *            areaPath for Role
	 * @return true if there is a role on given area
	 */
	public boolean hasRoleOnArea(String areaPathFilter) {
		for (RoleAssociation ra : roleAssociations) {
			if (ra.getArea().getAreaPath().path().equals(areaPathFilter)) {
				return true;
			}
		}
		return false;
	}

	public List<RoleAssociation> filterRoleAssociations(final String roleIdFilter, final String areaPathFilter) {
		return filterRoleAssociationsMatchingDownToLevel(roleIdFilter, areaPathFilter, null);
	}

	public List<RoleAssociation> filterRoleAssociationsMatchingDownToLevel(final String roleIdFilter, final String areaPathFilter, final AreaLevelEnum downToLevelLevel) {
		return new ArrayList<>(Collections2.filter(roleAssociations, new Predicate<RoleAssociation>() {
			@Override
			public boolean apply(RoleAssociation operatorWithRoleAssociations) {
				AreaPath rolesAreaPath = downToLevelLevel == null ? operatorWithRoleAssociations.getArea().getAreaPath()
                        : stripAreaPathUpToLevel(operatorWithRoleAssociations.getArea().getAreaPath().path(), downToLevelLevel);
				return (roleIdFilter.equals(FILTER_NOT_SET) || operatorWithRoleAssociations.getRole().getRoleId().equals(roleIdFilter))
                        && (areaPathFilter.equals(FILTER_NOT_SET) || rolesAreaPath.path().equals(downToLevelLevel == null
                        ? areaPathFilter : stripAreaPathUpToLevel(areaPathFilter, downToLevelLevel).path()));
			}
		}));
	}

	private AreaPath stripAreaPathUpToLevel(String areaPathString, AreaLevelEnum downToLevelLevel) {
		AreaPath areaPath = new AreaPath(areaPathString);
		while (areaPath.getLevel().lowerThan(downToLevelLevel)) {
			areaPath = areaPath.getParentPath();
		}
		return areaPath;
	}

	/**
	 * Removes roles associations
	 * @param deletedRoleAssociations
	 *            role associations to remove
	 */
	public void removeRoleAssociations(Set<RoleAssociation> deletedRoleAssociations) {
		roleAssociations.removeAll(deletedRoleAssociations);
	}
}
