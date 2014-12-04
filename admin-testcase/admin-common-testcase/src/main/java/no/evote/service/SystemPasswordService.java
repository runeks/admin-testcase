package no.evote.service;

public interface SystemPasswordService {
	void setSystemPassword(String password);
	boolean isPasswordSet();
	boolean isPasswordCorrect(String systemPassword);
}
