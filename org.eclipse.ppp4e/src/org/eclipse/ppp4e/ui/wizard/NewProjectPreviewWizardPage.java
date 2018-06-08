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
package org.eclipse.ppp4e.ui.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ppp4j.messages.PreviewResult;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class NewProjectPreviewWizardPage extends WizardPage {
	PreviewResult previewResult;

	protected NewProjectPreviewWizardPage() {
		super("Preview");
	}

	public void init(PreviewResult previewResult) {
		this.previewResult = previewResult;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		Label previewLabel = new Label(container, SWT.NONE);
		if (previewResult != null) {
			previewLabel.setText(previewResult.message);
		}
	}
}
