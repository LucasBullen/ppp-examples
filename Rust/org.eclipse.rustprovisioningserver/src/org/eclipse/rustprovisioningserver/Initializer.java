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
package org.eclipse.rustprovisioningserver;

import org.eclipse.ppp4j.messages.ComponentVersion;
import org.eclipse.ppp4j.messages.ComponentVersionSelection;
import org.eclipse.ppp4j.messages.InitializeResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ppp4j.messages.Template;
import org.eclipse.ppp4j.messages.TemplateSelection;
import org.eclipse.ppp4j.messages.Version;

public class Initializer {
	public InitializeResult initialize() {
		ComponentVersion[] cargoTemplateComponentVersions = new ComponentVersion[] {
				new ComponentVersion("crate_version", "Time Crate Version", null,
						new Version[] { new Version("0.1.40", "0.1.40", null), new Version("0.1.35", "0.1.35", null),
								new Version("0.1.30", "0.1.30", null), }) };
		Template[] templates = new Template[] {
				new Template("hello_world", "Hello World", "basic project outputting 'hello world' to the console",
						new ComponentVersion[0]),
				new Template("crate_example", "Cargo Crate Example",
						"Basic cargo based Rust project that imports an external crate",
						cargoTemplateComponentVersions) };

		TemplateSelection selection = new TemplateSelection("hello_world", new ComponentVersionSelection[0]);

		ComponentVersion[] componentVersions = new ComponentVersion[] { new ComponentVersion("toolchain",
				"Rustup Toolchain", "The version of Rustup to set as the default for the system",
				new Version[] {
						new Version("stable", "Stable", "Latest version to be fully tested and verified as stable"),
						new Version("beta", "Beta", null),
						new Version("nightly", "Nightly", "The newest release, may have bugs") }) };
		ComponentVersionSelection[] cargoComponentVersionSelection = new ComponentVersionSelection[] {
				new ComponentVersionSelection("toolchain", "stable") };

		InitializeResult result = new InitializeResult(true, true, true, templates, componentVersions,
				new ProvisioningParameters("new_rust_project", "/tmp/new_rust_project", "0.0.1-beta", selection,
						cargoComponentVersionSelection));
		return result;
	}
}