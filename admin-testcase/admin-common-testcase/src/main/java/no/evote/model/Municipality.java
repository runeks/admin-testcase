package no.evote.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import no.evote.constants.AreaLevelEnum;
import no.evote.constants.ElectionLevelEnum;
import no.evote.logging.AuditLogUtil;
import no.evote.persistence.AntiSamyEntityListener;
import no.evote.security.ContextSecurable;
import no.evote.validation.AntiSamy;
import no.evote.validation.ID;
import no.evote.validation.LettersOrDigits;
import no.evote.validation.StringNotNullEmptyOrBlanks;
import no.valg.eva.admin.common.AreaPath;
import no.valg.eva.admin.common.configuration.constants.MunicipalityStatusEnum;

/**
 * Municipalities
 */
@Entity
@Table(name = "municipality", uniqueConstraints = @UniqueConstraint(columnNames = { "county_pk", "municipality_id" }))
@AttributeOverride(name = "pk", column = @Column(name = "municipality_pk"))
@EntityListeners({ AntiSamyEntityListener.class })
@NamedQueries({
		@NamedQuery(name = "Municipality.findById", query = "select m from Municipality m WHERE m.county.pk = :countyPk AND m.id = :id"),
		@NamedQuery(name = "Municipality.findByElectionEventAndId",
                query = "select m from Municipality m WHERE m.county.country.electionEvent.pk = :electionEventPk AND m.id = :id"),
		@NamedQuery(name = "Municipality.findCountByCounty", query = "select COUNT(m) from Municipality m WHERE m.county.pk = :countyPk"),
		@NamedQuery(name = "Municipality.findLocale", query = "select m.locale from Municipality m WHERE m.pk = :municipalityPk"),
		@NamedQuery(name = "Municipality.findByCountry", query = "select m from Municipality m WHERE m.county.country.pk = :countryPk"),
		@NamedQuery(name = "Municipality.findByCounty", query = "select m from Municipality m WHERE m.county.pk = :countyPk"),
		@NamedQuery(name = "Municipality.findByElectionEventAndStatus", query = "select m from Municipality m, County county, Country country "
				+ "where m.county.pk = county.pk and county.country.pk = country.pk and m.municipalityStatus.id = :municipalityStatusId and "
				+ "country.electionEvent.pk = :electionEventPk"),
		@NamedQuery(name = "Municipality.findByPollingDistrict", query = "select pd.borough.municipality from PollingDistrict pd where "
				+ "pd.pk = :pollingDistrictPk"),

		@NamedQuery(name = "Municipality.findWithoutEncompassingBoroughs", query = "select m from Municipality m, County county, Country country "
				+ "where m.county.pk = county.pk and county.country.pk = country.pk and country.electionEvent.pk = :electionEventPk and "
				+ "not exists(select 1 from Borough b where b.municipality = m and b.municipality1 = true)"),

		@NamedQuery(name = "Municipality.findWithoutEncompassingPollingDistricts", query = "select m from Municipality m, County county, Country country "
				+ "where m.county.pk = county.pk and county.country.pk = country.pk and country.electionEvent.pk = :electionEventPk and "
				+ "not exists(select 1 from PollingDistrict pd, Borough b where b = pd.borough and b.municipality = m and pd.municipality = true)"),
		@NamedQuery(name = "Municipality.withoutBoroughsByCountry", query = "SELECT m from Municipality m, County c WHERE m.county.pk = c.pk "
				+ "AND c.country.pk = :countryPk AND NOT EXISTS (SELECT b FROM Borough b WHERE b.municipality.pk = m.pk)") })
@NamedNativeQueries({ @NamedNativeQuery(
		name = "Municipality.findWihoutConfiguredPollingStations",
		query = "SELECT DISTINCT m.* FROM "
				+ "(SELECT * FROM mv_area mva WHERE text2ltree(mva.area_path) <@ text2ltree(?1) AND mva.area_level = 6 ORDER BY mva.area_path) mva " + "JOIN "
				+ "(SELECT * from  polling_place pp where pp.using_polling_stations = true) pp " + "ON pp.polling_place_pk = mva.polling_place_pk "
				+ "JOIN municipality m " + "ON m.municipality_pk = mva.municipality_pk " + "WHERE pp.election_day_voting = true "
				+ "AND m.electronic_markoffs = false "
				+ "AND NOT EXISTS (SELECT * FROM polling_station where polling_station.polling_place_pk = pp.polling_place_pk)",
		resultClass = Municipality.class) })
public class Municipality extends VersionedEntity implements java.io.Serializable, ContextSecurable {
	private Locale locale;
	private County county;
	private MunicipalityStatus municipalityStatus;
	private String id;
	private String name;
	private boolean paperVoting;
	private boolean electronicVoting;
	private boolean electronicMarkoffs;
	private boolean advanceVoteInBallotBox;
	private boolean requiredProtocolCount;
	private boolean technicalPollingDistrictsAllowed;
	private Boolean handleElectionCardCentrally;
	private Set<Borough> boroughs = new HashSet<>();
	@AntiSamy
	private String electionCardText;

	public Municipality() {
	}

	public Municipality(final String id, final String name, final County county) {
		this.id = id;
		this.name = name;
		this.county = county;
	}

	@Column(name = "electronic_markoffs", nullable = false)
	public boolean isElectronicMarkoffs() {
		return electronicMarkoffs;
	}

	public void setElectronicMarkoffs(final boolean electronicMarkoffs) {
		this.electronicMarkoffs = electronicMarkoffs;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "locale_pk", nullable = false)
	@NotNull
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(final Locale locale) {
		this.locale = locale;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "county_pk", nullable = false)
	@NotNull
	public County getCounty() {
		return county;
	}

	public void setCounty(final County county) {
		this.county = county;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "municipality_status_pk", nullable = false)
	public MunicipalityStatus getMunicipalityStatus() {
		return municipalityStatus;
	}

	public void setMunicipalityStatus(final MunicipalityStatus municipalityStatus) {
		this.municipalityStatus = municipalityStatus;
	}

	@Transient
	public MunicipalityStatusEnum getMunicipalityStatusEnum() {
		return getMunicipalityStatus().toEnumValue();
	}

	@Column(name = "municipality_id", nullable = false, length = 4)
	@ID(size = 4)
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "municipality_name", nullable = false, length = 50)
	@LettersOrDigits
	@StringNotNullEmptyOrBlanks
	@Size(max = 50)
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "paper_voting", nullable = false)
	public boolean isPaperVoting() {
		return paperVoting;
	}

	public void setPaperVoting(final boolean paperVoting) {
		this.paperVoting = paperVoting;
	}

	@Column(name = "electronic_voting", nullable = false)
	public boolean isElectronicVoting() {
		return electronicVoting;
	}

	public void setElectronicVoting(final boolean electronicVoting) {
		this.electronicVoting = electronicVoting;
	}

	@Column(name = "advance_vote_in_ballot_box", nullable = false)
	public boolean isAdvanceVoteInBallotBox() {
		return advanceVoteInBallotBox;
	}

	public void setAdvanceVoteInBallotBox(final boolean advanceVoteInBallotBox) {
		this.advanceVoteInBallotBox = advanceVoteInBallotBox;
	}

	@Column(name = "election_card_text", length = 200)
	public String getElectionCardText() {
		return electionCardText;
	}

	public void setElectionCardText(final String electionCardText) {
		this.electionCardText = electionCardText;
	}

	@Column(name = "required_protocol_count", nullable = false)
	public boolean isRequiredProtocolCount() {
		return requiredProtocolCount;
	}

	public void setRequiredProtocolCount(final boolean requiredProtocolCount) {
		this.requiredProtocolCount = requiredProtocolCount;
	}

	@Column(name = "technical_polling_districts_allowed", nullable = false)
	@NotNull
	public boolean isTechnicalPollingDistrictsAllowed() {
		return technicalPollingDistrictsAllowed;
	}

	public void setTechnicalPollingDistrictsAllowed(final boolean technicalPollingDistrictsAllowed) {
		this.technicalPollingDistrictsAllowed = technicalPollingDistrictsAllowed;
	}

	@Column(name = "handle_election_card_centrally")
	public Boolean getHandleElectionCardCentrally() {
		return handleElectionCardCentrally;
	}

	public void setHandleElectionCardCentrally(final Boolean handleElectionCardCentrally) {
		this.handleElectionCardCentrally = handleElectionCardCentrally;
	}

	@OneToMany(mappedBy = "municipality", fetch = FetchType.LAZY)
	public Set<Borough> getBoroughs() {
		return boroughs;
	}

	void setBoroughs(Set<Borough> boroughs) {
		this.boroughs = boroughs;
	}

	public void add(Borough borough) {
		getBoroughs().add(borough);
	}
	
	public void enableElectronicVoting() {
		electronicVoting = true;
		electronicMarkoffs = true;
	}

	public void disableElectronicMarkoffs() {
		electronicMarkoffs = false;
		electronicVoting = false;
		paperVoting = true;
	}

	@Override
	public String toString() {
		return id + " - " + name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getPk() == null) ? 0 : getPk().hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Municipality other = (Municipality) obj;
		if (getPk() == null) {
			if (other.getPk() != null) {
				return false;
			}
		} else if (!getPk().equals(other.getPk())) {
			return false;
		}
		return true;
	}

	@Override
	public Long getAreaPk(final AreaLevelEnum level) {
		switch (level) {
		case MUNICIPALITY:
			return this.getPk();
		case COUNTY:
			return county.getPk();
		default:
			return null;
		}
	}

	@Override
	public Long getElectionPk(final ElectionLevelEnum level) {
		return null;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(county, id);
	}

	/**
	 * @return pollingDistricts in this municipality
	 */
	public Collection<PollingDistrict> pollingDistricts() {
		Collection<PollingDistrict> pollingDistricts = new HashSet<>();
		for (Borough borough : getBoroughs()) {
			pollingDistricts.addAll(borough.getPollingDistricts());
		}
		return pollingDistricts;
	}

	/**
	 * @return pollingDistricts in this municipality, but not krets 0000
	 */
	public Collection<PollingDistrict> pollingDistrictsWithOutMunicipalityDistrictAndWithOutTechnicalDistricts() {
		Collection<PollingDistrict> pollingDistricts = new HashSet<>();
		for (Borough borough : getBoroughs()) {
			for (PollingDistrict pollingDistrict : borough.getPollingDistricts()) {
				if (!pollingDistrict.isMunicipality() && !pollingDistrict.isTechnicalPollingDistrict()) {
					pollingDistricts.add(pollingDistrict);
				}
			}
		}
		return pollingDistricts;
	}

	/**
	 * @return pollingPlaces in this municipality
	 */
	public Collection<PollingPlace> pollingPlaces() {
		Collection<PollingPlace> pollingPlaces = new HashSet<>();
		for (PollingDistrict pollingDistrict : pollingDistricts()) {
			pollingPlaces.addAll(pollingDistrict.getPollingPlaces());
		}
		return pollingPlaces;
	}

	public AreaPath areaPath() {
		return getCounty().areaPath().add(getId());
	}

	/**
	 * @return true if any of the polling districts for this municipality is a parent polling district (tellekrets), else false
	 */
	@Transient
	public boolean hasParentPollingDistricts() {
		for (PollingDistrict pollingDistrict : pollingDistricts()) {
			if (pollingDistrict.isParentPollingDistrict()) {
				return true;
			}
		}
		return false;
	}

    public PollingDistrict pollingDistrictOfId(String id) {
        for (PollingDistrict pollingDistrict : pollingDistricts()) {
            if (id.equals(pollingDistrict.getId())) {
                return pollingDistrict;
            }
        }
        return null;
    }
}
