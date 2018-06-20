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
package io.openshift.wizard.client;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class OpenShiftProvisioningPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private Text serverText;

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));

		Label serverLabel = new Label(container, SWT.NONE);
		serverLabel.setText("Provisioning server command:");
		serverLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		serverText = new Text(container, SWT.BORDER);
		serverText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		serverText.addModifyListener(e -> {
			setValid(true);
		});
		serverText.setText(
				OpenShiftProvisioningPlugin.getDefault().getPreferenceStore()
						.getString(OpenShiftPreferenceInitializer.osppsPathPreference));
		return container;
	}

	@Override
	public boolean performOk() {
		OpenShiftProvisioningPlugin.getDefault().getPreferenceStore().setValue(OpenShiftPreferenceInitializer.osppsPathPreference,
				serverText.getText());
		return super.performOk();
	}

	@Override
	protected void performDefaults() {
		serverText.setText(OpenShiftPreferenceInitializer.getOSPPSPathBestGuess());
		super.performDefaults();
	}

	@Override
	public void init(IWorkbench workbench) {
		// Nothing to init
	}
}
