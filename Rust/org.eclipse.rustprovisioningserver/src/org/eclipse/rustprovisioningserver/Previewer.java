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

import org.eclipse.osgi.util.NLS;
import org.eclipse.ppp4j.messages.ErroneousParameter;
import org.eclipse.ppp4j.messages.PreviewResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ppp4j.messages.ValidationResult;

public class Previewer {
	private static final String MARKDOWN_PREIVEW_STRING = "**To create the project `cargo` is used:**\n\n"
			+ "`{0}`\n\n is run in the `{1}` directory.\n\n" + "**Based on the** ` {2} ` **template selected:**\n\n"
			+ "{3}\n\n" + "**Resulting project structure:**\n" + "\n{4}";
	private static final String PREIVEW_STRING = "To create the project `cargo` is used:\n"
			+ "`{0}` is run in the `{1}` directory.\n\n" + "Based on the `{2}` template selected:\n" + "{3}\n\n"
			+ "Resulting project structure:\n{4}";
	public PreviewResult preview(ProvisioningParameters parameters, boolean supportMarkdown) {
		ValidationResult validation = new Validator().validation(parameters);
		if (validation.errorMessage != null || validation.erroneousParameters.length > 0) {
			return new PreviewResult(validation.errorMessage, validation.erroneousParameters, null);
		}
		String result;
		if (supportMarkdown) {
			String actionDescription = "No further action is required.";
			String resultDescription = "Cargo.toml\n\nCargo.lock\n\nsrc\n\n \\-- main.rs";
			if (parameters.templateSelection.id.equals("crate_example")) {
				actionDescription = "The `time` crate is added to the dependencies section of the `Cargo.toml` file with the specified "
						+ parameters.templateSelection.getComponentVersion("crate_version") + " version.\n\n"
						+ "`main.rs` is overwritten with an example program using the imported `time` crate";
			}
			result = NLS.bind(MARKDOWN_PREIVEW_STRING,
					// TODO: replace template id with name when better template structure is made
					new Object[] { String.join(" ", Provisionner.commandListFromParameters(parameters)),
							parameters.location, parameters.templateSelection.id, actionDescription,
							resultDescription });
		} else {
			String actionDescription = "No further action is required.";
			String resultDescription = "  Cargo.toml\n  Cargo.lock\n  src\n   \\--main.rs";
			if (parameters.templateSelection.id.equals("crate_example")) {
				actionDescription = "The `time` crate is added to the dependencies section of the `Cargo.toml` file with the specified "
						+ parameters.templateSelection.getComponentVersion("crate_version") + " version.\n"
						+ "`main.rs` is overwritten with an example program using the imported `time` crate";
			}
			result = NLS.bind(PREIVEW_STRING,
					// TODO: replace template id with name when better template structure is made
					new Object[] { String.join(" ", Provisionner.commandListFromParameters(parameters)),
							parameters.location, parameters.templateSelection.id, actionDescription,
							resultDescription });
		}
		return new PreviewResult(null, new ErroneousParameter[0], result);
	}
}
