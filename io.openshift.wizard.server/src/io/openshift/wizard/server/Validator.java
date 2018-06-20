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
package io.openshift.wizard.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.ppp4j.messages.ComponentVersionSelection;
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
		} else if (parameters.name.length() > 65 || parameters.name.length() < 4) {
			errorMessage = "Name must be between 4 and 65 characters long";
			erroneousParameter.add(
					new ErroneousParameter(ParameterType.Name, "Name must be between 4 and 65 characters long", null));
		} else if (!parameters.name.matches("[a-zA-Z0-9 -]*")) {
			errorMessage = "Name can only contain letters, numbers, spaces or hyphens";
			erroneousParameter.add(new ErroneousParameter(ParameterType.Name,
					"Name must only contain letters, numbers, spaces or hyphens", null));
		} else if (parameters.name.matches("^[ -].*|.*[ -]$")) {
			errorMessage = "Name cannot start or end with spaces or hyphens";
			erroneousParameter.add(new ErroneousParameter(ParameterType.Name,
					"Name cannot start or end with spaces or hyphens", null));
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

		if (parameters.version == null || parameters.version.isEmpty()) {
			if (errorMessage == null) {
				errorMessage = "Version field cannot be empty";
			} else {
				errorMessage = "Multiple errors";
			}
			erroneousParameter
			.add(new ErroneousParameter(ParameterType.Version, "Version field cannot be empty", null));
		}

		String templateId = parameters.templateSelection.id;
		String missionId = null;
		for (ComponentVersionSelection selection : parameters.componentVersionSelections) {
			if (selection.id.equals("mission")) {
				missionId = selection.versionId;
				break;
			}
		}
		if (missionId == null) {
			if (errorMessage == null) {
				errorMessage = "Mission must be selected";
			} else {
				errorMessage = "Multiple errors";
			}
			erroneousParameter
			.add(new ErroneousParameter(ParameterType.ComponentVersion, "Mission must be selected", "mission"));
		} else if (!validTemplateAndMission(templateId, missionId)) {
			if (errorMessage == null) {
				errorMessage = "This mission and runtime combination is not currently available, but you can contribute to our library and help us expand these offerings.";
			} else {
				errorMessage = "Multiple errors";
			}
			erroneousParameter.add(new ErroneousParameter(ParameterType.ComponentVersion,
					"This mission and runtime combination is not currently available, but you can contribute to our library and help us expand these offerings.",
					"mission"));
		}

		return new ValidationResult(errorMessage, erroneousParameter.toArray(new ErroneousParameter[0]));
	}

	private boolean validTemplateAndMission(String templateId, String missionId) {
		switch (missionId) {
		case "cache":
			return (Arrays.asList(new String[] { "nodejs", "vertex", "spring", "wildfly" }).contains(templateId));
		case "externalized_configuration":
			return (Arrays.asList(new String[] { "nodejs", "fuse", "vertex", "spring", "wildfly" })
					.contains(templateId));
		case "circuit_breaker":
			return (Arrays.asList(new String[] { "nodejs", "fuse", "vertex", "spring", "wildfly" })
					.contains(templateId));
		case "crud":
			return (Arrays.asList(new String[] { "nodejs", "vertex", "spring", "wildfly" }).contains(templateId));
		case "health_check":
			return (Arrays.asList(new String[] { "nodejs", "fuse", "vertex", "spring", "wildfly" })
					.contains(templateId));
		case "istio_distributed_tracing":
			return (Arrays.asList(new String[] { "spring", "wildfly" }).contains(templateId));
		case "istio_circuit_breaker":
			return (Arrays.asList(new String[] { "nodejs", "spring", "wildfly" }).contains(templateId));
		case "rest_api":
			return (Arrays.asList(new String[] { "nodejs", "fuse", "vertex", "spring", "wildfly" })
					.contains(templateId));
		case "secured":
			return (Arrays.asList(new String[] { "nodejs", "vertex", "spring", "wildfly" }).contains(templateId));
		case "istio_security":
			return (Arrays.asList(new String[] { "spring", "wildfly" }).contains(templateId));
		case "istio_routing":
			return (Arrays.asList(new String[] { "spring", "wildfly" }).contains(templateId));
		default:
			break;
		}
		return false;
	}
}
