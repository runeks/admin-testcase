package no.evote.model.views;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * View for report on foreign early voting
 */
@Entity
@Table(name = "foreign_early_voting")
@NamedQueries({ @NamedQuery(name = "ForeignEarlyVoting.findForeignEarlyVotingsFromMunicipality", query = "SELECT fev FROM ForeignEarlyVoting fev WHERE "
		+ "fev.electionGroupPk = :electionGroupPk AND fev.municipalityId = :municipalityId ORDER BY fev.nameLine ASC") })
public class ForeignEarlyVoting implements java.io.Serializable {

	private Long votingPk;
	private Integer municipalityPk;
	private Long electionGroupPk;
	private String voterId;
	private Date dateOfBirth;
	private String nameLine;
	private Date castTimestamp;
	private String vMunicipalityId;
	private String vMunicipalityName;
	private String municipalityId;
	private String municipalityName;
	private String electionGroupName;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "voting_pk", unique = true, nullable = false)
	public Long getVotingPk() {
		return votingPk;
	}

	public void setVotingPk(final Long votingPk) {
		this.votingPk = votingPk;
	}

	@Column(name = "municipality_pk")
	public Integer getMunicipalityPk() {
		return municipalityPk;
	}

	public void setMunicipalityPk(final Integer municipalityPk) {
		this.municipalityPk = municipalityPk;
	}

	@Column(name = "election_group_pk")
	public Long getElectionGroupPk() {
		return electionGroupPk;
	}

	public void setElectionGroupPk(final Long electionGroupPk) {
		this.electionGroupPk = electionGroupPk;
	}

	@Column(name = "voter_id", length = 11)
	public String getVoterId() {
		return voterId;
	}

	public void setVoterId(final String voterId) {
		this.voterId = voterId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_birth", length = 13)
	public Date getDateOfBirth() {
		Date returnDate = null;
		if (dateOfBirth != null) {
			returnDate = new Date(dateOfBirth.getTime());
		}
		return returnDate;
	}

	public void setDateOfBirth(final Date dateOfBirth) {
		if (dateOfBirth != null) {
			this.dateOfBirth = new Date(dateOfBirth.getTime());
		} else {
			this.dateOfBirth = null;
		}
	}

	@Column(name = "name_line", length = 152)
	public String getNameLine() {
		return nameLine;
	}

	public void setNameLine(final String nameLine) {
		this.nameLine = nameLine;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cast_timestamp", length = 29)
	public Date getCastTimestamp() {
		Date returnDate = null;
		if (castTimestamp != null) {
			returnDate = new Date(castTimestamp.getTime());
		}
		return returnDate;
	}

	public void setCastTimestamp(final Date castTimestamp) {
		if (castTimestamp != null) {
			this.castTimestamp = new Date(castTimestamp.getTime());
		} else {
			this.castTimestamp = null;
		}
	}

	@Column(name = "v_municipality_id", length = 4)
	public String getVMunicipalityId() {
		return vMunicipalityId;
	}

	public void setVMunicipalityId(final String vMunicipalityId) {
		this.vMunicipalityId = vMunicipalityId;
	}

	@Column(name = "v_municipality_name", length = 50)
	public String getVMunicipalityName() {
		return vMunicipalityName;
	}

	public void setVMunicipalityName(final String vMunicipalityName) {
		this.vMunicipalityName = vMunicipalityName;
	}

	@Column(name = "municipality_id", length = 4)
	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(final String municipalityId) {
		this.municipalityId = municipalityId;
	}

	@Column(name = "municipality_name", length = 50)
	public String getMunicipalityName() {
		return municipalityName;
	}

	public void setMunicipalityName(final String municipalityName) {
		this.municipalityName = municipalityName;
	}

	@Column(name = "election_group_name", length = 100)
	public String getElectionGroupName() {
		return electionGroupName;
	}

	public void setElectionGroupName(final String electionGroupName) {
		this.electionGroupName = electionGroupName;
	}

}
