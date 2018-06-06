package org.eclipse.ppp4j.messages;

public class ComponentVersion {
	public String id;
	public String title;
	public String caption;
	public Version[] versions;

	public ComponentVersion() {
	}

	public ComponentVersion(String id, String title, String caption, Version[] versions) {
		this.id = id;
		this.title = title;
		this.caption = caption;
		this.versions = versions;
	}
}
