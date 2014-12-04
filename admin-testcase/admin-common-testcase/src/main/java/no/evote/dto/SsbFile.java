package no.evote.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@edu.umd.cs.findbugs.annotations.SuppressWarnings({"EI_EXPOSE_REP", "EQ_UNUSUAL"})
public class SsbFile implements Serializable, Comparable<SsbFile> {
	private static final long serialVersionUID = 4035313704134105242L;
	private String xml;
	private String html;
	private byte[] file;
	private String fileName;
	private Date date;

	/**
	 * @return SKV01 or SSV01
	 */
	public String type() {
		if (fileName.contains("SKV01")) {
			return "SKV01";
		} else if (fileName.contains("SSV01")) {
			return "SSV01";
		} else if (fileName.contains("SFV01")) {
			return "SFV01";
		}
		return "";
	}

	public boolean disableShowHtml() {
		return type().equals("SFV01");
	}

	public String getXml() {
		return xml;
	}

	public void setXml(final String xml) {
		this.xml = xml;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(final String html) {
		this.html = html;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(final byte[] file) {
		this.file = file.clone();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public Date getDate() {
		return ObjectUtils.clone(date);
	}

	public void setDate(final Date date) {
		this.date = ObjectUtils.clone(date);
	}

	@Override
	public int compareTo(final SsbFile o) {
		return date.compareTo(o.getDate());
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
