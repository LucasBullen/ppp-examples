package org.eclipse.ppp4j.messages;

public class Version {
	String id;
	String title;
	String caption;

	public Version() {
	}

	public Version(String id, String title, String caption) {
		this.id = id;
		this.title = title;
		this.caption = caption;
	}
}
