package org.eclipse.ppp4j.messages;

public class ProvisionResult {
	public String errorMessage;
	public ErroneousParameter[] erroneousParameters;
	public String[] newFiles;
	public String[] openFiles;

	public ProvisionResult() {
	}

	public ProvisionResult(String errorMessage, ErroneousParameter[] erroneousParameters, String[] newFiles,
			String[] openFiles) {
		this.errorMessage = errorMessage;
		this.erroneousParameters = erroneousParameters;
		this.newFiles = newFiles;
		this.openFiles = openFiles;
	}
}
