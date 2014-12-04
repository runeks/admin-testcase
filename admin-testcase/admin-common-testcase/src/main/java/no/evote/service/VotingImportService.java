package no.evote.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import no.evote.security.UserData;

/**
 * Defines API for voting import.
 */
public interface VotingImportService {

	/**
	 * Imports votings or mark offs (kryss) from  eVote, using same file format as the electoral roll export.
	 * 
	 * @param userData
	 * @param zipFile fil
	 * @return
	 * @throws java.io.FileNotFoundException
	 */
	String[] importEvotingMarkoffs(UserData userData, File zipFile) throws FileNotFoundException;

	/**
	 * Validates import files.
	 * 
	 * @param userData
	 * @param files files to validate
	 * @throws java.io.IOException
	 */
	void validateFiles(UserData userData, Map<String, File> files) throws IOException;

}
