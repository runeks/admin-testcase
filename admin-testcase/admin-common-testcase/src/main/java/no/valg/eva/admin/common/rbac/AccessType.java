package no.valg.eva.admin.common.rbac;

import no.evote.exception.EvoteException;

/**
 * Contains values from the access_type table.
 */
public enum AccessType {

	E("E"), G("G"), O("O"), W("W"), A("A");

	AccessType(final String id) {
		this.id = id;
	}

	private final String id;

	public String getId() {
		return id;
	}

	public String getName() {
		return "@access_type[" + id + "].name";
	}

	/**
	 * @param id
	 * @return AccessType for the given id
	 */
	public static AccessType fromId(final String id) {
		switch (id) {
		case "E":
			return E;
		case "G":
			return G;
		case "O":
			return O;
		case "W":
			return W;
		case "A":
			return A;
		default:
			throw new EvoteException("unknown id: " + id);
		}
	}
}
