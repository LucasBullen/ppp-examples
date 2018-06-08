package org.eclipse.ppp4j.messages;

public class ValidationResult {
	public String errorMessage;
	public ErroneousParameter[] erroneousParameters;

	public ValidationResult() {
	}

	public ValidationResult(String errorMessage, ErroneousParameter[] erroneousParameters) {
		this.errorMessage = errorMessage;
		this.erroneousParameters = erroneousParameters;
	}
}
