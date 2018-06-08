package org.eclipse.ppp4j.messages;

public class Instruction {
	public String path;
	public String content;

	public Instruction() {
	}

	public Instruction(String path, String content) {
		this.path = path;
		this.content = content;
	}

}
