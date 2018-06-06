package org.eclipse.ppp4j.messages;

public class Version {
	public String id;
	public String title;
	public String caption;

	public Version() {
	}

	public Version(String id, String title, String caption) {
		this.id = id;
		this.title = title;
		this.caption = caption;
	}
}
