package org.eclipse.ppp4j.messages;

public class TemplateSelection {
	public String id;
	public ComponentVersionSelection[] componentVersions;

	public TemplateSelection() {
	}

	public TemplateSelection(String id, ComponentVersionSelection[] componentVersions) {
		this.id = id;
		this.componentVersions = componentVersions;
	}
}
