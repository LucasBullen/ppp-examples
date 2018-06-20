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

import java.io.File;
import java.util.ArrayList;

import org.eclipse.ppp4j.messages.ErroneousParameter;
import org.eclipse.ppp4j.messages.ParameterType;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ppp4j.messages.ValidationResult;

public class Validator {
	public ValidationResult validation(ProvisioningParameters parameters) {
		java.util.List<ErroneousParameter> erroneousParameter = new ArrayList<>();
		String errorMessage = null;

		if (parameters.name == null || parameters.name.isEmpty()) {
			errorMessage = "Name field cannot be empty";
			erroneousParameter.add(new ErroneousParameter(ParameterType.Name, "Name field cannot be empty", null));
		}

		String locationError = null;
		if (parameters.location == null || parameters.location.isEmpty()) {
			locationError = "Location field cannot be empty";
		} else {
			File directory = new File(parameters.location);
			if (directory.isFile()) {
				locationError = "Given location is an existing file";
			} else if (directory.getParentFile() == null
					|| (!directory.exists() && !directory.getParentFile().canWrite())) {
				locationError = "Unable to create project in given location";
			} else if (directory.exists() && !directory.canWrite()) {
				locationError = "Cannot write in given location";
			}
		}
		if (locationError != null) {
			if (errorMessage == null) {
				errorMessage = locationError;
			} else {
				errorMessage = "Multiple errors";
			}
			erroneousParameter
			.add(new ErroneousParameter(ParameterType.Location, "Location field cannot be empty", null));
		}

		if (parameters.templateSelection == null || parameters.templateSelection.id == null) {
			if (errorMessage == null) {
				errorMessage = "Select a template";
			} else {
				errorMessage = "Multiple errors";
			}
			erroneousParameter.add(new ErroneousParameter(ParameterType.Template, "Select a template", null));
		}

		return new ValidationResult(errorMessage, erroneousParameter.toArray(new ErroneousParameter[0]));
	}
}
