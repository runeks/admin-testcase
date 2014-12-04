package no.valg.eva.admin.common.rbac;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import no.evote.constants.AreaLevelEnum;

import static no.evote.constants.AreaLevelEnum.COUNTY;
import static no.evote.constants.AreaLevelEnum.MUNICIPALITY;
import static no.evote.constants.AreaLevelEnum.POLLING_DISTRICT;
import static no.evote.constants.AreaLevelEnum.POLLING_PLACE;

/**
 * RBAC: Role
 */
public class Role implements Serializable {

	private Long electionEventPk;
	private String id;
	private String name;
	private boolean mutuallyExclusive;
	private int securityLevel;
	private boolean active;
	private boolean isUserSupport;

	private Set<Access> accesses = new HashSet<>();
	private Set<Role> includedRoles = new HashSet<>();

	private Set<AreaLevelEnum> assignableAreaLevels = new HashSet<>();
	private Long pk;

	private boolean countyAreaLevel;
	private boolean municipalityAreaLevel;
	private boolean pollingDistrictAreaLevel;
	private boolean pollingPlaceAreaLevel;
	/**
	 * PollingPlaceType if role has pollingPlaceAreaLevel
	 * True:	pollingPlaceType.ELECTION_DAY_VOTING
	 * False:	pollingPlaceType.ADVANCE_VOTING
	 * null: 	pollingPlaceAreaLevel and electionDayVotingPollingPlaceType is not selected for this role
 	 */
	private Boolean electionDayVotingPollingPlaceType;
    private boolean mapAccesses;


    public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean isMutuallyExclusive() {
		return this.mutuallyExclusive;
	}

	public void setMutuallyExclusive(final boolean mutuallyExclusive) {
		this.mutuallyExclusive = mutuallyExclusive;
	}

	public int getSecurityLevel() {
		return this.securityLevel;
	}

	public void setSecurityLevel(final int securityLevel) {
		this.securityLevel = securityLevel;
	}

	public Set<Access> getAccesses() {
		return accesses;
	}

	public void setAccesses(final Set<Access> accesses) {
		this.accesses = accesses;
	}

	public Set<Role> getIncludedRoles() {
		return includedRoles;
	}

	public void setIncludedRoles(final Set<Role> includedRoles) {
		this.includedRoles = includedRoles;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public boolean includesRole(final Role role) {
		return includedRoles.contains(role);
	}

	public void setElectionEventPk(final Long electionEventPk) {
		this.electionEventPk = electionEventPk;
	}

	public boolean isUserSupport() {
		return this.isUserSupport;
	}

	public void setUserSupport(final boolean isUserSupport) {
		this.isUserSupport = isUserSupport;
	}

	public boolean isCountyAreaLevel() {
		return countyAreaLevel;
	}

	public Boolean getElectionDayVotingPollingPlaceType() {
		return electionDayVotingPollingPlaceType;
	}

	public void setElectionDayVotingPollingPlaceType(Boolean electionDayVotingPollingPlaceType) {
		this.electionDayVotingPollingPlaceType = electionDayVotingPollingPlaceType;
	}

	/**
	 * Sets flag and updates set of assignable area levels
	 */
	public void setCountyAreaLevel(final boolean countyAreaLevel) {
		this.countyAreaLevel = countyAreaLevel;
		if (countyAreaLevel) {
			addAssignableAreaLevel(COUNTY);
		} else {
			removeAssignableAreaLevel(COUNTY);
		}
	}

	public boolean isMunicipalityAreaLevel() {
		return municipalityAreaLevel;
	}

	/**
	 * Sets flag and updates set of assignable area levels
	 */
	public void setMunicipalityAreaLevel(final boolean municipalityAreaLevel) {
		this.municipalityAreaLevel = municipalityAreaLevel;
		if (municipalityAreaLevel) {
			addAssignableAreaLevel(MUNICIPALITY);
		} else {
			removeAssignableAreaLevel(MUNICIPALITY);
		}
	}

	public boolean isPollingPlaceAreaLevel() {
		return pollingPlaceAreaLevel;
	}

	public boolean isPollingDistrictAreaLevel() {
		return pollingDistrictAreaLevel;
	}


	/**
	 * Sets flag and updates set of assignable area levels
	 */
	public void setPollingDistrictAreaLevel(final boolean pollingDistrictAreaLevel) {
		this.pollingDistrictAreaLevel = pollingDistrictAreaLevel;
		if (pollingDistrictAreaLevel) {
			addAssignableAreaLevel(POLLING_DISTRICT);
		} else {
			removeAssignableAreaLevel(POLLING_DISTRICT);
		}
	}


	/**
	 * Sets flag and updates set of assignable area levels
	 */
	public void setPollingPlaceAreaLevel(final boolean pollingPlaceAreaLevel) {
		this.pollingPlaceAreaLevel = pollingPlaceAreaLevel;
		if (pollingPlaceAreaLevel) {
			addAssignableAreaLevel(POLLING_PLACE);
		} else {
			removeAssignableAreaLevel(POLLING_PLACE);
			this.electionDayVotingPollingPlaceType = null;
		}
	}

	/**
	 * Adds role area level.
	 */
	public void addAssignableAreaLevel(final AreaLevelEnum areaLevel) {
		this.assignableAreaLevels.add(areaLevel);
	}

	/**
	 * Removes given area level.
	 */
	public void removeAssignableAreaLevel(final AreaLevelEnum areaLevelEnum) {
		this.assignableAreaLevels.remove(areaLevelEnum);
	}

	/**
	 * @return true if role can be assigned to the given area level
	 */
	public boolean canBeAssignedToAreaLevel(final AreaLevelEnum areaLevelEnum) {
		return this.assignableAreaLevels.contains(areaLevelEnum);
	}

	public Set<AreaLevelEnum> getAssignableAreaLevels() {
		return assignableAreaLevels;
	}

	public Long getElectionEventPk() {
		return electionEventPk;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

    public void setMapAccesses(boolean mapAccesses) {
        this.mapAccesses = mapAccesses;
    }

	/**
	 * @return true if this contains Access instances that need to be mapped to an entity.
	 */
	public boolean containsAccessesToMap() {
        return mapAccesses;
    }
}
