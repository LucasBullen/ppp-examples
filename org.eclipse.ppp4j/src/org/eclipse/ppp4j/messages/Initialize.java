package org.eclipse.ppp4j.messages;

public class Initialize {
	boolean supportMarkdown;
	boolean allowFileCreation;

	public Initialize() {
	}

	public Initialize(boolean supportMarkdown, boolean allowFileCreation) {
		this.supportMarkdown = supportMarkdown;
		this.allowFileCreation = allowFileCreation;
	}
}
