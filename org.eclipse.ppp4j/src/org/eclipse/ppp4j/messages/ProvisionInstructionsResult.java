package org.eclipse.ppp4j.messages;

public class ProvisionInstructionsResult {
	public String errorMessage;
	public ErroneousParameter[] erroneousParameters;
	public String name;
	public Instruction[] newFiles;
	public String[] openFiles;

	public ProvisionInstructionsResult() {
	}

	public ProvisionInstructionsResult(String errorMessage, ErroneousParameter[] erroneousParameters, String name,
			Instruction[] newFiles, String[] openFiles) {
		this.errorMessage = errorMessage;
		this.erroneousParameters = erroneousParameters;
		this.name = name;
		this.newFiles = newFiles;
		this.openFiles = openFiles;
	}
}
