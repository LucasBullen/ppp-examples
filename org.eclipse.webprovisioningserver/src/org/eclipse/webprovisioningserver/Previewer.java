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

import org.eclipse.osgi.util.NLS;
import org.eclipse.ppp4j.messages.ErroneousParameter;
import org.eclipse.ppp4j.messages.PreviewResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ppp4j.messages.ValidationResult;

public class Previewer {
	private static final String PREIVEW_STRING = "**Resulting structure:**\n\n{0}";
	public PreviewResult preview(ProvisioningParameters parameters, boolean supportMarkdown) {
		ValidationResult validation = new Validator().validation(parameters);
		if (validation.errorMessage != null || validation.erroneousParameters.length > 0) {
			return new PreviewResult(validation.errorMessage, validation.erroneousParameters, null);
		}
		String resultDescription = "index.html";
		switch (parameters.templateSelection.id) {
		case "form":
			resultDescription += "\n\nresult.html";
			break;
		case "advanced_css":
			resultDescription += "\n\nfirst.css\n\nsecond.css";
			break;
		case "advanced_js":
			resultDescription += "\n\nfirst.js\n\nsecond.js";
			break;
		default:
			break;
		}
		String result = NLS.bind(PREIVEW_STRING, new Object[] { resultDescription });
		return new PreviewResult(null, new ErroneousParameter[0], result);
	}
}
