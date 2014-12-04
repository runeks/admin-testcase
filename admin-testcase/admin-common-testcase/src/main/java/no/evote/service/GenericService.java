package no.evote.service;

import no.evote.security.UserData;

public interface GenericService {

	<T> T findByPk(UserData userData, Class<T> clazz, Long pk);

}
