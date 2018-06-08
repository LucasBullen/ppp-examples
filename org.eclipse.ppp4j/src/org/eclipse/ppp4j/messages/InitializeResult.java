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
