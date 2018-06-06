package org.eclipse.ppp4j.messages;

public class PreviewResult {

	public String errorMessage;
	public ErroneousParameter[] erroneousParameters;
	public String message;

	public PreviewResult() {
	}

	public PreviewResult(String errorMessage, ErroneousParameter[] erroneousParameters, String message) {
		this.errorMessage = errorMessage;
		this.erroneousParameters = erroneousParameters;
		this.message = message;
	}
}
