package no.evote.service;

import java.io.IOException;
import java.util.List;

import no.evote.model.BaseEntity;
import no.evote.model.ProposerList;
import no.evote.security.UserData;

public interface ProposerListService {

	int countByBallot(UserData userData, Long ballotPk);

	List<ProposerList> findProposerListsByBallot(UserData userData, Long ballotPk);

	ProposerList createProposerList(UserData userData, ProposerList proposerList);

	BaseEntity updateProposerList(UserData userData, ProposerList proposerList);

	ProposerList createProposerList(UserData userData, byte[] binaryData, Long ballotPk, String fileName) throws IOException;

	void readFolders();

	ProposerList findProposerListByBinaryDataByScanBinaryData(String fileName);

}
