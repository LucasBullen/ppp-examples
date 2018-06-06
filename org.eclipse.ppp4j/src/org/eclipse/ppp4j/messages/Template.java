package org.eclipse.ppp4j.messages;

public class Template {
	String id;
	String title;
	String caption;
	ComponentVersion[] componentVersions;

	public Template() {
	}

	public Template(String id, String title, String caption, ComponentVersion[] componentVersions) {
		this.id = id;
		this.title = title;
		this.caption = caption;
		this.componentVersions = componentVersions;
	}
}
