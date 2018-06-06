package org.eclipse.ppp4j.messages;

public class ComponentVersionSelection {
	String id;
	String versionId;

	public ComponentVersionSelection() {
	}

	public ComponentVersionSelection(String id, String versionId) {
		this.id = id;
		this.versionId = versionId;
	}

}
