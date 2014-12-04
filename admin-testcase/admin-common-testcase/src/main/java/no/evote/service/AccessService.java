package no.evote.service;

import java.util.List;
import java.util.Set;

import no.evote.model.Access;
import no.evote.model.Role;
import no.evote.security.AccessCache;
import no.evote.security.UserData;
import no.evote.service.cache.Cacheable;

public interface AccessService {
	Set<Access> getIncludedAccesses(final Role role);

	@Cacheable
	Access findAccessByPath(final String accessId);

	@Cacheable
	Set<Access> findAccessesAtLevelByPath(final int level, final String path);

	@Cacheable
	Access findAccessByPk(final Long pk);

	@Cacheable
	List<Access> findAll(UserData userData);

	/**
	 * Finds accesses and converts to view model objects
	 * @param userData
	 * @return list of accesses
	 */
	@Cacheable
	List<no.valg.eva.admin.common.rbac.Access> findAllForView(UserData userData);

	Set<Access> getIncludedAccessesNoTask(Role role);

	Set<String> getIncludedAccessesNoDisabledRoles(Role role);

	AccessCache getAccessCache(UserData userData);
}
