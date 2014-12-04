package no.evote.dto;

import java.io.Serializable;

/**
 * Used by local/central configuration overview
 */
public class ConfigurationDto implements Serializable {
	private final String id;
	private final String name;

	public ConfigurationDto(final String id, final String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
