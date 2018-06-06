package org.eclipse.ppp4j.messages;

public class InitializeResult {
	public boolean versionRequired;
	public boolean validationSupported;
	public boolean previewSupported;
	public Template[] templates;
	public ComponentVersion[] componentVersions;
	public ProvisioningParameters defaultProvisioningParameters;

	public InitializeResult() {
	}

	public InitializeResult(boolean versionRequired, boolean validationSupported, boolean previewSupported,
			Template[] templates, ComponentVersion[] componentVersions,
			ProvisioningParameters defaultProvisioningParameters) {
		this.versionRequired = versionRequired;
		this.validationSupported = validationSupported;
		this.previewSupported = previewSupported;
		this.templates = templates;
		this.componentVersions = componentVersions;
		this.defaultProvisioningParameters = defaultProvisioningParameters;
	}
}
