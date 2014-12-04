package no.valg.eva.admin.frontend.context;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * Session object containing information about the logged in user.
 * See also UserData.
 * UserData's concern is primarily security, while UserContext is responsible for providing data for
 * better usability and display of information.  Unlike UserData, UserContext is a front end object.
 */
@Named
@SessionScoped
public class UserContext implements Serializable {
	
	private String nameLine;

	public void setNameLine(String nameLine) {
		this.nameLine = nameLine;
	}

	public String getNameLine() {
		return nameLine;
	}
}
