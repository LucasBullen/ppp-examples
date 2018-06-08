package org.eclipse.ppp4j.messages;

public class ProvisionResult {
	public String errorMessage;
	public ErroneousParameter[] erroneousParameters;
	public String location;
	public String[] openFiles;

	public ProvisionResult() {
	}

	public ProvisionResult(String errorMessage, ErroneousParameter[] erroneousParameters, String location,
			String[] openFiles) {
		this.errorMessage = errorMessage;
		this.erroneousParameters = erroneousParameters;
		this.location = location;
		this.openFiles = openFiles;
	}
}
