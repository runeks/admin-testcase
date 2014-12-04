package no.evote.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.constants.SQLConstants;
import no.evote.security.ContextSecurable;
import no.evote.util.EqualsHashCodeUtil;
import no.evote.validation.ID;
import no.evote.validation.LettersOrDigits;
import no.evote.validation.StringNotNullEmptyOrBlanks;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.ElectionPath;
import no.valg.eva.admin.common.configuration.constants.ElectionEventStatusEnum;

/**
 * Parents all data for one or more elections run within the same time frame
 */
@Entity
@Table(name = "election_event", uniqueConstraints = @UniqueConstraint(columnNames = "election_event_id"))
@AttributeOverride(name = "pk", column = @Column(name = SQLConstants.ELECTION_EVENT_PK))
@NamedQueries({
		@NamedQuery(name = "ElectionEvent.findById", query = "SELECT ev FROM ElectionEvent ev WHERE ev.id = :id"),
		@NamedQuery(name = "ElectionEvent.getElectionGroupSorted", query = "SELECT eg FROM ElectionGroup eg" + " WHERE eg.electionEvent.pk = :electionEventPk"
				+ " ORDER BY eg.id"),
		@NamedQuery(
				name = "ElectionEvent.findAllActive",
				query = "SELECT ee FROM ElectionEvent ee WHERE ee.electionEventStatus.id != 9 AND ee.id != :adminEventId") })
public class ElectionEvent extends VersionedEntity implements java.io.Serializable, ContextSecurable {

	private Locale locale;
	private ElectionEventStatus electionEventStatus;
	private String id;
	private String name;
	private Date electoralRollCutOffDate;
	private Integer electoralRollLinesPerPage;
	private Date votingCardElectoralRollDate;
	private Date votingCardDeadline;
	private Date voterNumbersAssignedDate;
	private String voterImportDirName;
	private boolean voterImportMunicipality;
	private String theme;
	private Set<ElectionDay> electionDays = new HashSet<>();

	public ElectionEvent() {
	}

	public ElectionEvent(final String id, final String name, final Locale locale) {
		this.id = id;
		this.name = name;
		this.locale = locale;
	}

	public ElectionEvent(final Long electionEventPk) {
		setPk(electionEventPk);
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "locale_pk", nullable = false)
	@NotNull
	public Locale getLocale() {
		return this.locale;
	}

	public void setLocale(final Locale locale) {
		this.locale = locale;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "election_event_status_pk", nullable = false)
	@NotNull
	public ElectionEventStatus getElectionEventStatus() {
		return this.electionEventStatus;
	}

	public void setElectionEventStatus(final ElectionEventStatus electionEventStatus) {
		this.electionEventStatus = electionEventStatus;
	}

	@Transient
	public ElectionEventStatusEnum getElectionEventStatusEnum() {
		return getElectionEventStatus().toEnumValue();
	}

	@Column(name = "election_event_id", nullable = false, length = 8)
	@ID(size = 6)
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "election_event_name", nullable = false, length = 100)
	@LettersOrDigits
	@StringNotNullEmptyOrBlanks
	@Size(max = 100)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "electoral_roll_cut_off_date", nullable = false, length = 13)
	public Date getElectoralRollCutOffDate() {
		Date returnDate = null;
		if (this.electoralRollCutOffDate != null) {
			returnDate = new Date(this.electoralRollCutOffDate.getTime());
		}
		return returnDate;
	}

	public void setElectoralRollCutOffDate(final Date electoralRollCutOffDate) {
		if (electoralRollCutOffDate != null) {
			this.electoralRollCutOffDate = new Date(electoralRollCutOffDate.getTime());
		} else {
			this.electoralRollCutOffDate = null;
		}
	}

	@Column(name = "electoral_roll_lines_per_page")
	@Min(0)
	@Max(9999)
	public Integer getElectoralRollLinesPerPage() {
		return this.electoralRollLinesPerPage;
	}

	public void setElectoralRollLinesPerPage(final Integer electoralRollLinesPerPage) {
		this.electoralRollLinesPerPage = electoralRollLinesPerPage;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "voting_card_electoral_roll_date", nullable = false, length = 13)
	public Date getVotingCardElectoralRollDate() {
		Date returnDate = null;
		if (this.votingCardElectoralRollDate != null) {
			returnDate = new Date(this.votingCardElectoralRollDate.getTime());
		}
		return returnDate;
	}

	public void setVotingCardElectoralRollDate(final Date votingCardElectoralRollDate) {
		if (votingCardElectoralRollDate != null) {
			this.votingCardElectoralRollDate = new Date(votingCardElectoralRollDate.getTime());
		} else {
			this.votingCardElectoralRollDate = null;
		}
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "voting_card_deadline", nullable = false, length = 13)
	public Date getVotingCardDeadline() {
		Date returnDate = null;
		if (this.votingCardDeadline != null) {
			returnDate = new Date(this.votingCardDeadline.getTime());
		}
		return returnDate;
	}

	public void setVotingCardDeadline(final Date votingCardDeadline) {
		if (votingCardDeadline != null) {
			this.votingCardDeadline = new Date(votingCardDeadline.getTime());
		} else {
			this.votingCardDeadline = null;
		}
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "voter_numbers_assigned_date", length = 13)
	public Date getVoterNumbersAssignedDate() {
		Date returnDate = null;
		if (this.voterNumbersAssignedDate != null) {
			returnDate = new Date(this.voterNumbersAssignedDate.getTime());
		}
		return returnDate;
	}

	public void setVoterNumbersAssignedDate(final Date voterNumbersAssignedDate) {
		if (voterNumbersAssignedDate != null) {
			this.voterNumbersAssignedDate = new Date(voterNumbersAssignedDate.getTime());
		} else {
			this.voterNumbersAssignedDate = null;
		}
	}

	@Column(name = "voter_import_dir_name", length = 226)
	@Size(max = 226)
	@Pattern(regexp = "(^/.+)?", message = "{@validation.absolutePath}")
	public String getVoterImportDirName() {
		return this.voterImportDirName;
	}

	public void setVoterImportDirName(final String voterImportDirName) {
		this.voterImportDirName = voterImportDirName;
	}

	@Column(name = "voter_import_municipality", nullable = false)
	public boolean isVoterImportMunicipality() {
		return this.voterImportMunicipality;
	}

	public void setVoterImportMunicipality(final boolean voterImportMunicipality) {
		this.voterImportMunicipality = voterImportMunicipality;
	}

	@Column(name = "theme", length = 125)
	@Size(max = 125)
	public String getTheme() {
		return theme;
	}

	public void setTheme(final String theme) {
		this.theme = theme;
	}

	@OneToMany(mappedBy = "electionEvent", fetch = FetchType.LAZY)
	public Set<ElectionDay> getElectionDays() {
		return electionDays;
	}

	public void setElectionDays(Set<ElectionDay> electionDays) {
		this.electionDays = electionDays;
	}

	@Override
	public int hashCode() {
		return EqualsHashCodeUtil.genericHashCode(this);
	}

	@Override
	public boolean equals(final Object obj) {
		return EqualsHashCodeUtil.genericEquals(this, obj);
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		if (level.equals(AreaLevelEnum.ROOT)) {
			return getPk();
		}
		return null;
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		if (level.equals(ElectionLevelEnum.ELECTION_EVENT)) {
			return this.getPk();
		}
		return null;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return id;
	}

	public AreaPath areaPath() {
		return AreaPath.from(getId());
	}

	public ElectionPath electionPath() {
		return ElectionPath.from(getId());
	}
}
