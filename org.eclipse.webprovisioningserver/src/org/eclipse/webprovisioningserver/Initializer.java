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
package org.eclipse.webprovisioningserver;

import org.eclipse.ppp4j.messages.ComponentVersion;
import org.eclipse.ppp4j.messages.ComponentVersionSelection;
import org.eclipse.ppp4j.messages.InitializeResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ppp4j.messages.Template;
import org.eclipse.ppp4j.messages.TemplateSelection;

public class Initializer {
	public InitializeResult initialize() {
		Template[] templates = new Template[] {
				new Template("hello_world", "Hello World", "Basic project showing html basics",
						new ComponentVersion[0]),
				new Template("form", "Form", "An HTML form which sends results to another page",
						new ComponentVersion[0]),
				new Template("advanced_js", "Advanced JavaScript", "Multiple examples of advanced JS techniques",
						new ComponentVersion[0]),
				new Template("advanced_css", "Advanced CSS", "Multiple examples of advanced CSS techniques",
						new ComponentVersion[0]) };

		TemplateSelection selection = new TemplateSelection("hello_world", new ComponentVersionSelection[0]);

		InitializeResult result = new InitializeResult(true, true, true, templates, new ComponentVersion[0],
				new ProvisioningParameters("New_Website", "/tmp/New_Website", "0.0.1-beta", selection,
						new ComponentVersionSelection[0]));
		return result;
	}
}