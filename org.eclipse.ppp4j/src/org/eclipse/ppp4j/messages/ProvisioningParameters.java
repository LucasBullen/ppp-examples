package org.eclipse.ppp4j.messages;

public class ProvisioningParameters {
	public String name;
	public String location;
	public String version;
	public TemplateSelection templateSelection;
	public ComponentVersionSelection[] componentVersionSelections;

	public ProvisioningParameters() {
	}

	public ProvisioningParameters(String name, String location, String version, TemplateSelection templateSelection,
			ComponentVersionSelection[] componentVersionSelections) {
		this.name = name;
		this.location = location;
		this.version = version;
		this.templateSelection = templateSelection;
		this.componentVersionSelections = componentVersionSelections;
	}

}
