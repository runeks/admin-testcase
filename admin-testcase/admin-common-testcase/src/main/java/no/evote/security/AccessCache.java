package no.evote.security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class AccessCache implements Serializable {

	private transient Set<String> securableObjects;
	private final String securableObjectsAsString;
	private final byte[] signature;

	@SuppressWarnings("PMD.ArrayIsStoredDirectly")
	public AccessCache(final Set<String> securableObjects, final byte[] signature) {
		this.securableObjects = securableObjects;
		this.securableObjectsAsString = StringUtils.join(securableObjects.toArray(new String[] {}), ",");
		if (signature != null) {
			this.signature = signature.clone();
		} else {
			this.signature = null;
		}
	}

	/**
	 * Check access on a comma separated list of securable objects
	 */
	public boolean hasAccess(final String secObj) {
		boolean hasAccess = false;

		for (String tmpSecObj : secObj.split(",")) {
			if (hasSingleAccess(tmpSecObj.replaceAll("\\s+", ""))) {
				hasAccess = true;
				break;
			}
		}

		return hasAccess;
	}

	/**
	 * Check access on a string array of securable objects
	 */
	public boolean hasAccess(final String[] secObjs) {
		for (String secObj : secObjs) {
			if (hasSingleAccess(secObj)) {
				return true;
			}
		}

		return false;
	}

	protected boolean hasSingleAccess(final String secObj) {
		return securableObjects.contains(secObj);
	}

	public Set<String> getSecurableObjects() {
		return securableObjects;
	}

	public byte[] getSignature() {
		return signature;
	}

	public String getSecurableObjectsAsString() {
		return securableObjectsAsString;
	}

	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		securableObjects = new HashSet<String>(Arrays.asList(securableObjectsAsString.split(",")));
	}
}
