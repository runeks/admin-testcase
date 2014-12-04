package no.evote.security;

import java.io.Serializable;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.enterprise.inject.Typed;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.EvoteConstants;
import no.evote.model.Operator;
import no.evote.model.OperatorRole;
import no.valg.eva.admin.common.AreaPath;

import org.apache.log4j.Logger;

@Typed()
/**
 * UserData contains info about user such as id, selected role, all available roles and accesses and so on.
 * Currently, it has two main concerns (should it be split..?):
 *  - security check on server side
 *  - usability: only render menu items user has access to, navigate to correct page given from context without user interaction
 */
public class UserData implements Serializable {

	private static final Logger LOGGER = Logger.getLogger(UserData.class);
	private String uid;
	private OperatorRole operatorRole;
	private String csrfToken = "123";
	private Integer securityLevel = 3;
	
	public UserData() {
		operatorRole = new OperatorRole();
		Operator operator = new Operator();
		operator.setId("testOperator");
		operatorRole.setOperator(operator);
	}

	public UserData(String uid) {

		this.uid = uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

	public OperatorRole getOperatorRole() {
		return operatorRole;
	}

	public String getCsrfToken() {
		return csrfToken;
	}

	public Integer getSecurityLevel() {
		return securityLevel;
	}
}
