package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.logging.AuditLogUtil;
import no.evote.security.ContextSecurable;

/**
 * Scanned candidate lists
 */
@Entity
@Table(name = "proposer_list", uniqueConstraints = @UniqueConstraint(columnNames = { "ballot_pk", "proposer_list_number" }))
@AttributeOverride(name = "pk", column = @Column(name = "proposer_list_pk"))
@NamedQueries({
		@NamedQuery(name = "ProposerList.findByBallot", query = "SELECT pl FROM ProposerList pl WHERE pl.ballot.pk = :ballotPk"),
		@NamedQuery(name = "ProposerList.findByBinaryDataByScanBinaryData", query = "SELECT pl FROM ProposerList pl "
				+ "WHERE pl.binaryDataByScanBinaryData.fileName LIKE :fileName") })
public class ProposerList extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private BinaryData binaryDataByIcrBinaryData;
	private BinaryData binaryDataByScanBinaryData;
	private Ballot ballot;
	private ProposerListStatus proposerListStatus;
	private int number;

	public ProposerList() {
		super();
	}

	public ProposerList(ProposerList proposerList) {
		super();
		this.number = proposerList.getNumber();
		this.ballot = proposerList.getBallot();
		this.binaryDataByIcrBinaryData = proposerList.getBinaryDataByIcrBinaryData();
		this.binaryDataByScanBinaryData = proposerList.getBinaryDataByScanBinaryData();
		this.proposerListStatus = proposerList.getProposerListStatus();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "icr_binary_data_pk")
	public BinaryData getBinaryDataByIcrBinaryData() {
		return this.binaryDataByIcrBinaryData;
	}

	public void setBinaryDataByIcrBinaryData(final BinaryData binaryDataByIcrBinaryData) {
		this.binaryDataByIcrBinaryData = binaryDataByIcrBinaryData;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "scan_binary_data_pk")
	public BinaryData getBinaryDataByScanBinaryData() {
		return this.binaryDataByScanBinaryData;
	}

	public void setBinaryDataByScanBinaryData(final BinaryData binaryDataByScanBinaryData) {
		this.binaryDataByScanBinaryData = binaryDataByScanBinaryData;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ballot_pk", nullable = false)
	public Ballot getBallot() {
		return this.ballot;
	}

	public void setBallot(final Ballot ballot) {
		this.ballot = ballot;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "proposer_list_status_pk", nullable = false)
	public ProposerListStatus getProposerListStatus() {
		return this.proposerListStatus;
	}

	public void setProposerListStatus(final ProposerListStatus proposerListStatus) {
		this.proposerListStatus = proposerListStatus;
	}

	@Column(name = "proposer_list_number", insertable = false, updatable = false, nullable = false)
	public int getNumber() {
		return this.number;
	}

	public void setNumber(final int number) {
		this.number = number;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		return null;
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		if (level.equals(ElectionLevelEnum.CONTEST)) {
			return ballot.getContest().getPk();
		}
		return null;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(ballot, number);
	}
}
