package no.evote.service;

import java.io.File;

import no.evote.model.VoteCategory;
import no.evote.security.UserData;

public interface CountingImportService {
	void validateCountEmlZip(UserData userData, File zipFile);

	void importCountEmlZip(UserData userData, File zipFile);

	VoteCategory findVoteCategoryById(UserData userData, String id);
}
