package org.eclipse.ppp4j.messages;

public class ErroneousParameter {
	public ParameterType parameterType;
	public String message;
	public String componentVersionId;

	public ErroneousParameter() {
	}

	public ErroneousParameter(ParameterType parameterType, String message, String componentVersionId) {
		this.parameterType = parameterType;
		this.message = message;
		this.componentVersionId = componentVersionId;
	}
}
