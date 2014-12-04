package no.evote.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * Standard report templates
 */
@Entity
@Table(name = "standard_report", uniqueConstraints = @UniqueConstraint(columnNames = "standard_report_id"))
@AttributeOverride(name = "pk", column = @Column(name = "standard_report_pk"))
@NamedQuery(name = "StandardReport.findById", query = "SELECT sr FROM StandardReport sr WHERE sr.id = :id")
public class StandardReport extends VersionedEntity implements java.io.Serializable {

	private String id;
	private String name;
	private String template;
	private byte[] binary;

	@Column(name = "standard_report_id", unique = true, nullable = false, length = 25)
	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Column(name = "standard_report_name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "standard_report_template", nullable = false, length = 32768)
	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(final String template) {
		this.template = template;
	}

	@Column(name = "standard_report_binary")
	public byte[] getBinary() {
		return this.binary;
	}

	public void setBinary(final byte[] binary) {
		if (binary != null) {
			this.binary = new byte[binary.length];
			System.arraycopy(binary, 0, this.binary, 0, binary.length);
		}
	}

	@Override
	@Transient
	public String getAuditLogId() {
		return id;
	}
}
