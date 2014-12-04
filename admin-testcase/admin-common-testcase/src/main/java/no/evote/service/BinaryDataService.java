package no.evote.service;

import java.io.IOException;
import java.io.Serializable;

import no.evote.model.BinaryData;
import no.evote.model.ElectionEvent;
import no.evote.security.UserData;

public interface BinaryDataService extends Serializable {

	BinaryData create(UserData userData, BinaryData binaryData);

	BinaryData findByPk(UserData userData, Long pk);

	BinaryData createBinaryData(UserData userData, byte[] bytes, String fileName, ElectionEvent electionEvent, String tableName, String columnName,
			String mimeType) throws IOException;

	BinaryData findModifiedBallotBinaryData(UserData userData, Long modifiedBallotPk);

	void deleteBinaryData(UserData userData, Long pk);

}
