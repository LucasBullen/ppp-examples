package org.eclipse.ppp4j.messages;

public class TemplateSelection {
	String id;
	ComponentVersion[] componentVersions;

	public TemplateSelection() {
	}

	public TemplateSelection(String id, ComponentVersion[] componentVersions) {
		this.id = id;
		this.componentVersions = componentVersions;
	}
}
