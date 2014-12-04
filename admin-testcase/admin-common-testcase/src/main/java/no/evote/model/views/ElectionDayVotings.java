package no.evote.model.views;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.model.Contest;
import no.evote.model.ElectionDay;
import no.evote.model.MvArea;
import no.evote.model.VotingCategory;
import no.evote.security.ContextSecurable;

@Entity
@Table(name = "election_day_votings")
@NamedQueries({ @NamedQuery(name = "ElectionDayVotings.get", query = "SELECT edv FROM ElectionDayVotings edv WHERE edv.contest.pk = :contestPk AND "
		+ "edv.mvArea.pollingDistrict.pk = :pollingDistrictPk AND edv.votingCategory.id = :votingCategoryId ORDER BY edv.electionDay.date ") })
public class ElectionDayVotings implements java.io.Serializable, ContextSecurable {

	private ElectionDayVotingsId id;
	private Contest contest;
	private MvArea mvArea;
	private ElectionDay electionDay;
	private VotingCategory votingCategory;
	private Long votings;

	public ElectionDayVotings() {
	}

	public ElectionDayVotings(final Contest contest, final MvArea mvArea, final VotingCategory votingCategory, final ElectionDay electionDay) {
		super();
		this.contest = contest;
		this.mvArea = mvArea;
		this.votingCategory = votingCategory;
		this.electionDay = electionDay;
		this.id = new ElectionDayVotingsId(contest.getPk(), electionDay.getPk(), mvArea.getPk(), votingCategory.getPk());
	}

	@EmbeddedId
	@AttributeOverrides({ @AttributeOverride(name = "contestPk", column = @Column(name = "contest_pk", nullable = false)),
			@AttributeOverride(name = "mvAreaPk", column = @Column(name = "mv_area_pk", nullable = false)),
			@AttributeOverride(name = "electionDayPk", column = @Column(name = "election_day_pk", nullable = false)),
			@AttributeOverride(name = "votingCategoryPk", column = @Column(name = "voting_category_pk", nullable = false)) })
	public ElectionDayVotingsId getId() {
		return id;
	}

	public void setId(final ElectionDayVotingsId id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "contest_pk", insertable = false, updatable = false)
	public Contest getContest() {
		return contest;
	}

	public void setContest(final Contest contest) {
		this.contest = contest;
	}

	@ManyToOne
	@JoinColumn(name = "mv_area_pk", insertable = false, updatable = false)
	public MvArea getMvArea() {
		return mvArea;
	}

	public void setMvArea(final MvArea mvArea) {
		this.mvArea = mvArea;
	}

	@ManyToOne
	@JoinColumn(name = "election_day_pk", insertable = false, updatable = false)
	public ElectionDay getElectionDay() {
		return electionDay;
	}

	public void setElectionDay(final ElectionDay electionDay) {
		this.electionDay = electionDay;
	}

	@ManyToOne
	@JoinColumn(name = "voting_category_pk", insertable = false, updatable = false)
	public VotingCategory getVotingCategory() {
		return votingCategory;
	}

	public void setVotingCategory(final VotingCategory votingCategory) {
		this.votingCategory = votingCategory;
	}

	@Min(0)
	@Column(name = "votings", insertable = false, updatable = false)
	public Long getVotings() {
		return votings;
	}

	public void setVotings(final Long votings) {
		this.votings = votings;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		return mvArea.getAreaPk(level);
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		if (level.equals(ElectionLevelEnum.CONTEST)) {
			return contest.getPk();
		} else {
			return null;
		}
	}
}
