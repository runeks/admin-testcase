package no.valg.eva.admin.common;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;

public class Area implements Serializable, Comparable<Area> {
	private AreaPath areaPath;
	private String name;

	public Area(AreaPath areaPath, String name) {
		requireNonNull(areaPath, "Area has no area path");
		requireNonNull(name, "Area has no name");

		this.areaPath = areaPath;
		this.name = name;
	}

	public AreaPath getAreaPath() {
		return areaPath;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Area area = (Area) o;

		if (!areaPath.equals(area.areaPath)) {
			return false;
		}
		if (!name.equals(area.name)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = areaPath.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Area{"
				+ "areaPath=" + areaPath
				+ ", name='" + name + '\''
				+ '}';
	}

	@Override
	public int compareTo(Area o) {
		return o.areaPath.toString().compareTo(this.areaPath.toString());
	}
}
