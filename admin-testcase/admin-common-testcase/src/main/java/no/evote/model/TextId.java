package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import no.evote.constants.SQLConstants;
import no.evote.logging.AuditLogUtil;
import no.evote.persistence.AntiSamyEntityListener;
import no.evote.validation.AntiSamy;
import no.evote.validation.LettersOrDigits;
import no.evote.validation.StringNotNullEmptyOrBlanks;

/**
 * Text id for translation
 */
@Entity
@Table(name = "text_id", uniqueConstraints = { @UniqueConstraint(columnNames = { SQLConstants.ELECTION_EVENT_PK, "text_id" }) })
@AttributeOverride(name = "pk", column = @Column(name = "text_id_pk"))
@EntityListeners({ AntiSamyEntityListener.class })
@NamedQueries({
		@NamedQuery(name = "TextId.findByElectionEvent", query = "SELECT  ti from  TextId ti where election_event_pk = :electionEventPk order by textId"),
		@NamedQuery(name = "TextId.findGlobal", query = "SELECT  ti from  TextId ti where election_event_pk is null order by textId"),
		@NamedQuery(name = "TextId.findByElectionEventAndId", query = "SELECT  ti from  TextId ti where election_event_pk = :electionEventPk"
				+ " and text_id = :textId"),
		@NamedQuery(name = "TextId.findGlobalById", query = "SELECT  ti from  TextId ti where election_event_pk is null and text_id = :textId") })
public class TextId extends VersionedEntity implements java.io.Serializable {

	private ElectionEvent electionEvent;
	private String textId;
	@AntiSamy
	private String infoText;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = SQLConstants.ELECTION_EVENT_PK)
	public ElectionEvent getElectionEvent() {
		return this.electionEvent;
	}

	public void setElectionEvent(final ElectionEvent electionEvent) {
		this.electionEvent = electionEvent;
	}

	@Column(name = "text_id", nullable = false, length = 100)
	@StringNotNullEmptyOrBlanks
	@LettersOrDigits(extraChars = "@[]._-")
	@Size(max = 100)
	public String getTextId() {
		return this.textId;
	}

	public void setTextId(final String textId) {
		this.textId = textId;
	}

	@Column(name = "info_text", length = 150)
	@LettersOrDigits
	@Size(max = 150)
	public String getInfoText() {
		return this.infoText;
	}

	public void setInfoText(final String infoText) {
		this.infoText = infoText;
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return AuditLogUtil.generateId(electionEvent, textId);
	}
}
