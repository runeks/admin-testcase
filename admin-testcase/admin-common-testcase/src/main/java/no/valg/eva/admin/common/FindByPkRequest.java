package no.valg.eva.admin.common;

import java.io.Serializable;

/**
 * General service request for finding data by primary key
 */
public class FindByPkRequest implements Serializable {

	private final long pk;

	public FindByPkRequest(long pk) {
		this.pk = pk;
	}

	public long getPk() {
		return pk;
	}
}
