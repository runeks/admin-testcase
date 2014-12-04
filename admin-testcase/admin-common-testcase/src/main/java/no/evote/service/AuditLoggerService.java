package no.evote.service;

import java.net.InetAddress;

import no.evote.security.UserData;

public interface AuditLoggerService {

	char ID_SEPARATOR = ':';
	String ID_NONE = "null";

	void info(InetAddress clientAddress, String uid, String calledMethod, String calledClass, String info);

	void info(UserData userData, String calledMethod, String calledClass, String info);

	void error(UserData userData, String calledMethod, String calledClass, Exception exc, StackTraceElement stackTraceElement, String info);

	void error(InetAddress clientAddress, String uid, String calledMethod, String calledClass, Exception exc, StackTraceElement stackTraceElement, String info);

	void initializeAuditLogger();

	Boolean isAuditLoggerActive();
}
