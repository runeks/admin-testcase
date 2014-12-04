package no.evote.converters;

import static java.lang.Long.valueOf;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import no.evote.model.BaseEntity;
import no.evote.presentation.util.FacesUtil;
import no.evote.service.GenericService;

@SessionScoped
@FacesConverter(value = "entityConverter")
public class EntityConverter implements Converter, Serializable {
	private static final long serialVersionUID = 4859077490187952361L;

	private transient GenericService genericService;

	@Override
	@SuppressWarnings("unchecked")
	public Object getAsObject(final FacesContext facesContext, final UIComponent component, final String value) {
		Object entity = null;
		if (value != null && value.trim().length() > 0) {
			try {
				entity = genericService.findByPk(FacesUtil.getUserData(), getClazz(facesContext, component), valueOf(value));
			} catch (NumberFormatException e) {
				entity = null;
			}
		}
		return entity;
	}

	@Override
	public String getAsString(final FacesContext facesContext, final UIComponent component, final Object value) {
		if (value != null && !(value instanceof BaseEntity)) {
			throw new IllegalArgumentException("This converter only handles instances of BaseEntity");
		}
		if (value == null) {
			return "";
		}
		BaseEntity entity = (BaseEntity) value;
		return entity.getPk().toString();
	}

	// Gets the class corresponding to the context in jsf page
	@SuppressWarnings("rawtypes")
	private Class getClazz(final FacesContext facesContext, final UIComponent component) {
		return component.getValueExpression("value").getType(facesContext.getELContext());
	}

}
