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

import org.eclipse.osgi.util.NLS;
import org.eclipse.ppp4j.messages.ErroneousParameter;
import org.eclipse.ppp4j.messages.PreviewResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ppp4j.messages.Template;
import org.eclipse.ppp4j.messages.ValidationResult;
import org.eclipse.ppp4j.messages.Version;

public class Previewer {
	private static final String PREIVEW_STRING = "A {0} ({1}) project will be created using the {2} base project.\n{3}\nThis project will {4}\nApplication Name: {5}\nMaven Artifact: booster\nVersion:{6}\nGroup ID:io.openshift.booster";
	public PreviewResult preview(ProvisioningParameters parameters, boolean supportMarkdown) {
		String preview = PREIVEW_STRING;
		if (supportMarkdown) {
			preview = preview.replace("\n", "\n\n");
		}
		ValidationResult validation = new Validator().validation(parameters);
		if (validation.errorMessage != null || validation.erroneousParameters.length > 0) {
			return new PreviewResult(validation.errorMessage, validation.erroneousParameters, null);
		}

		String runtime_title;
		String runtime_version;
		String mission_title;
		String mission_caption;
		String cluster_preview;

		Template selectedTemplate = null;

		for (Template template : Initializer.templates) {
			if (template.id.equals(parameters.templateSelection.id)) {
				selectedTemplate = template;
				break;
			}
		}
		runtime_title = selectedTemplate.title;
		runtime_version = selectedTemplate.getComponentVersionById("version")
				.getVersionById(parameters.templateSelection.getComponentVersion("version")).title;

		Version selectedMission = Initializer.mission
				.getVersionById(parameters.getComponentVersionSelectionById("mission").versionId);
		mission_title = selectedMission.title;
		mission_caption = selectedMission.caption;

		Version selectedCluster = Initializer.cluster
				.getVersionById(parameters.getComponentVersionSelectionById("cluster").versionId);
		if (selectedCluster.id.equals("local")) {
			cluster_preview = "be created in the " + parameters.location + "directory.";
		} else {
			cluster_preview = "be launched on the " + selectedCluster.title + "(" + selectedCluster.caption
					+ ") OpenShift cluster";
		}

		String result = NLS.bind(preview, new Object[] { runtime_title, runtime_version, mission_title, mission_caption,
				cluster_preview, parameters.name, parameters.version });
		return new PreviewResult(null, new ErroneousParameter[0], result);
	}
}
