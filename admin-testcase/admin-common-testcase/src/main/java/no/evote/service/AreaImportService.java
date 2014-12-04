package no.evote.service;

import java.io.IOException;

import no.evote.security.UserData;

public interface AreaImportService {
	void importAreaHierarchy(UserData userData, byte[] data) throws IOException;
}
