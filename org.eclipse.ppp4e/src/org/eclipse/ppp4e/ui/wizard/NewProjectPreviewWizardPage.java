package org.eclipse.ppp4e.ui.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class NewProjectPreviewWizardPage extends WizardPage {
	static String pageName = "name";

	protected NewProjectPreviewWizardPage() {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		Label previewLabel = new Label(container, SWT.NONE);
		previewLabel.setText("Preview");
	}
}
