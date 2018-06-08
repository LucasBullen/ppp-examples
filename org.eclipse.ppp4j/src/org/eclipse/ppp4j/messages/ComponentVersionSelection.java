package org.eclipse.ppp4j.messages;

public class ComponentVersionSelection {
	public String id;
	public String versionId;

	public ComponentVersionSelection() {
	}

	public ComponentVersionSelection(String id, String versionId) {
		this.id = id;
		this.versionId = versionId;
	}

}
