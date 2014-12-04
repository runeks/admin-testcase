package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import no.evote.logging.AuditLogUtil;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * RBAC: Role granted to an operator, with additional constraints specified by links into the election and area hierarchies
 */
@Entity
@Table(name = "operator_role", uniqueConstraints = @UniqueConstraint(columnNames = { "operator_pk", "role_pk", "mv_election_pk", "mv_area_pk" }))
@AttributeOverride(name = "pk", column = @Column(name = "operator_role_pk"))
@NamedQueries({
		@NamedQuery(name = "OperatorRole.findOperatorRoles", query = "SELECT o FROM OperatorRole o WHERE o.operator = :operator"),
		@NamedQuery(name = "OperatorRole.findUnique", query = "SELECT o FROM OperatorRole o WHERE o.role.pk = :role AND o.operator.pk = :operator "
				+ "AND o.mvArea.pk = :mvArea AND o.mvElection.pk = :mvElection"),
		@NamedQuery(name = "OperatorRole.findUserCountForRole", query = "SELECT COUNT(DISTINCT operator_pk) FROM OperatorRole o WHERE o.role.pk = :role"),
		@NamedQuery(name = "OperatorRole.findOperatorRolesAtOwnLevel", query = "SELECT o FROM OperatorRole o WHERE o.mvArea = :mvArea"),
		@NamedQuery(name = "OperatorRole.findOperatorRolesForOperatorAtOwnLevel",
				query = "SELECT o FROM OperatorRole o WHERE o.operator = :operator AND o.mvArea = :mvArea") })
// @formatter:off
@NamedNativeQueries({
		@NamedNativeQuery(name = "OperatorRole.findOperatorRolesGivingOperatorAccess", query = "SELECT DISTINCT opr2.* FROM mv_area mva3 "
				+ "JOIN mv_area mva on mva.mv_area_pk = mva3.mv_area_pk "
				+ "JOIN mv_area mva2 ON ((public.text2ltree(mva.area_path) OPERATOR(public.@>) public.text2ltree(mva2.area_path))) "
				+ "JOIN operator_role opr2 on mva2.mv_area_pk = opr2.mv_area_pk "
				+ "WHERE mva3.mv_area_pk = ?1 AND opr2.operator_pk = ?4 AND opr2.role_pk IN (SELECT role_pk FROM role_access_all WHERE "
				+ "election_event_pk = ?2 AND access_pk = ?3)", resultClass = OperatorRole.class),
		@NamedNativeQuery(name = "OperatorRole.findOperatorsWithAccess", query = "SELECT * FROM operator WHERE operator_pk IN "
			    + "(SELECT DISTINCT opr2.operator_pk FROM mv_area mva3 "
				+ "JOIN mv_area mva on mva.mv_area_pk = mva3.mv_area_pk "
				+ "JOIN mv_area mva2 ON ((public.text2ltree(mva.area_path) OPERATOR(public.@>) public.text2ltree(mva2.area_path))) "
				+ "JOIN operator_role opr2 on mva2.mv_area_pk = opr2.mv_area_pk "
				+ "WHERE mva3.mv_area_pk = ?1 AND opr2.role_pk IN (SELECT role_pk FROM role_access_all WHERE "
				+ "election_event_pk = ?2 AND access_pk = ?3))", resultClass = Operator.class),
		@NamedNativeQuery(name = "OperatorRole.findDescOperatorsRoles", query = "SELECT DISTINCT opr2.* FROM mv_area mva3 "
				+ "JOIN mv_area mva on mva.mv_area_pk = mva3.mv_area_pk "
				+ "JOIN mv_area mva2 ON ((public.text2ltree(mva.area_path) OPERATOR(public.@>) public.text2ltree(mva2.area_path))) "
				+ "JOIN operator_role opr2 on mva2.mv_area_pk = opr2.mv_area_pk " + "WHERE mva3.mv_area_pk = ?1", resultClass = OperatorRole.class),
		@NamedNativeQuery(name = "OperatorRole.findOperatorRolesForOperatorAtOrBelowMvArea", query = "SELECT DISTINCT opr2.* FROM mv_area mva3 "
				+ "JOIN mv_area mva on mva.mv_area_pk = mva3.mv_area_pk "
				+ "JOIN mv_area mva2 ON ((public.text2ltree(mva.area_path) OPERATOR(public.@>) public.text2ltree(mva2.area_path))) "
				+ "JOIN operator_role opr2 on mva2.mv_area_pk = opr2.mv_area_pk "
				+ "WHERE mva3.mv_area_pk = ?1 AND opr2.operator_pk = ?2", resultClass = OperatorRole.class),
		@NamedNativeQuery(name = "OperatorRole.findNotOwnPathOperatorRolesForOperator", query = "select opr.* FROM operator_role opr "
				+ "JOIN mv_election e ON (e.mv_election_pk = opr.mv_election_pk) "
				+ "JOIN mv_area a ON (a.mv_area_pk = opr.mv_area_pk) WHERE opr.operator_pk = ?1 "
				+ "AND NOT (text2ltree(e.election_path) <@ text2ltree(?2) AND text2ltree(a.area_path) <@ text2ltree(?3))", resultClass = OperatorRole.class),
		@NamedNativeQuery(name = "OperatorRole.decideIfOperatorRoleIsInOwnPath", query = "select opr.* FROM operator_role opr "
				+ "JOIN mv_election e ON (e.mv_election_pk = opr.mv_election_pk) "
				+ "JOIN mv_area a ON (a.mv_area_pk = opr.mv_area_pk) WHERE opr.operator_role_pk = ?1 "
				+ "AND NOT (text2ltree(e.election_path) <@ text2ltree(?2) AND text2ltree(a.area_path) <@ text2ltree(?3))", resultClass = OperatorRole.class) })
@SuppressWarnings({ "PMD.AvoidDuplicateLiterals" })
// @formatter:on
public class OperatorRole extends VersionedEntity implements java.io.Serializable, Comparable<OperatorRole> {

	private MvArea mvArea;
	private Operator operator;
	private MvElection mvElection;
	private Role role;
	private boolean isInOwnHierarchy;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mv_area_pk", nullable = false)
	public MvArea getMvArea() {
		return this.mvArea;
	}

	public void setMvArea(final MvArea mvArea) {
		this.mvArea = mvArea;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "operator_pk", nullable = false)
	public Operator getOperator() {
		return this.operator;
	}

	public void setOperator(final Operator operator) {
		this.operator = operator;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mv_election_pk", nullable = false)
	public MvElection getMvElection() {
		return this.mvElection;
	}

	public void setMvElection(final MvElection mvElection) {
		this.mvElection = mvElection;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_pk", nullable = false)
	public Role getRole() {
		return this.role;
	}

	public void setRole(final Role role) {
		this.role = role;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof OperatorRole) {
			return ((OperatorRole) obj).getRole().getPk().equals(getRole().getPk()) && ((OperatorRole) obj).getOperator().equals(getOperator())
					&& ((OperatorRole) obj).getMvArea().getPk().equals(getMvArea().getPk())
					&& ((OperatorRole) obj).getMvElection().getPk().equals(getMvElection().getPk());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getRole().hashCode() + getOperator().hashCode() + getMvArea().hashCode() + getMvElection().hashCode();
	}

	@Override
	public int compareTo(final OperatorRole o) {
		return this.getRole().compareTo(o.getRole());
	}

	@Transient
	public boolean isInOwnHierarchy() {
		return isInOwnHierarchy;
	}

	public void setInOwnHierarchy(final boolean isInOwnHierarchy) {
		this.isInOwnHierarchy = isInOwnHierarchy;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(operator, role, mvElection, mvArea);
	}

	/**
	 * Checks if electronic voting is configured for this operator role. If this operator role only has access to one municipality, the isElectronicVoting on
	 * this Municipality instance will be used to determine whether electronic voting is configured. If the operator role has access to several municipalities,
	 * the election group of the operator role will be used to determine whether electronic voting is configured.
	 * @return true if electronic voting is configured or if it is not easy to determine this (i.e. no municipality or election group on operator role), else
	 *         false
	 */
	@Transient
	public boolean isEvoteConfigured() {
		Municipality municipality = getMvArea().getMunicipality();
		if (municipality != null) {
			return municipality.isElectronicVoting();
		}
		ElectionGroup electionGroup = getMvElection().getElectionGroup();
		if (electionGroup != null) {
			return electionGroup.isElectronicVoting();
		}
		return true;
	}

	/**
	 * Checks if electronic mark offs is configured for this operator role. If this operator role only has access to one municipality, the isElectronicMarkoffs
	 * on this Municipality instance will be used to determine whether electronic mark offs are configured. If the operator role has access to several
	 * municipalities, we always return true.
	 */
	@Transient
	public boolean isElectronicMarkoffsConfigured() {
		Municipality municipality = getMvArea().getMunicipality();
		if (municipality != null) {
			return municipality.isElectronicMarkoffs();
		}
		return true;
	}

    @Transient
	public Boolean isEnabledForSecurityLevel(int securityLevel) {
		return role.isActive() && role.getAccumulatedSecLevel() <= securityLevel && operator.isActive();
	}
}
