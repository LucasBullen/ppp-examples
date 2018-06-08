/*********************************************************************
 * Copyright (c) 2018 Red Hat Inc. and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Lucas Bullen (Red Hat Inc.) - Initial implementation
 *******************************************************************************/
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
