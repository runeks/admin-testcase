package no.evote.persistence;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.naming.NamingException;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import no.evote.model.BaseEntity;
import no.evote.util.AntiSamyFilter;
import no.evote.validation.AntiSamy;

/**
 * Apply antiSamy filter when storing and retrieving data. See http://www.owasp.org/index.php/Category:OWASP_AntiSamy_Project for more information on AntiSamy.
 */
public class AntiSamyEntityListener implements Serializable {

	@PrePersist
	public void doPrePersist(final BaseEntity entity) throws NamingException, IllegalAccessException {
		applyAntiSamyFilter(entity);
	}

	@PreUpdate
	public void doPreUpdate(final BaseEntity entity) throws NamingException, IllegalAccessException {
		applyAntiSamyFilter(entity);
	}

	@PostLoad
	public void doPostLoad(final BaseEntity entity) throws IllegalAccessException {
		applyAntiSamyFilter(entity);
	}

	private void applyAntiSamyFilter(final BaseEntity entity) throws IllegalAccessException {
		for (Field field : entity.getClass().getDeclaredFields()) {
			// Only filter fields annotated with @AntiSamy
			if (field.getAnnotation(AntiSamy.class) != null) {
				field.setAccessible(true);
				field.set(entity, AntiSamyFilter.filter((String) field.get(entity)));
			}
		}
	}

}
