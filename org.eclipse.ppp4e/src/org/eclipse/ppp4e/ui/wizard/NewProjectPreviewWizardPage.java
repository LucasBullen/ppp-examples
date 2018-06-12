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
import org.eclipse.mylyn.wikitext.markdown.MarkdownLanguage;
import org.eclipse.mylyn.wikitext.parser.MarkupParser;
import org.eclipse.ppp4e.core.Server;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class NewProjectPreviewWizardPage extends WizardPage {
	private static final MarkupParser MARKDOWN_PARSER = new MarkupParser(new MarkdownLanguage());
	private Server server;
	private Browser browser;


	protected NewProjectPreviewWizardPage() {
		super("Preview");
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible && server != null && browser != null) {
			resetPreview();
		}
		super.setVisible(visible);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		browser = new Browser(container, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		browser.setText("Loading Preview");
	}

	private void resetPreview() {
		NewProjectWizardPage inputPage = ((NewProjectWizardPage) getPreviousPage());
		ProvisioningParameters parameters = inputPage.getParameters();
		server.Preview(parameters).thenAccept(previewResult -> {
			if (previewResult.erroneousParameters.length > 0
					|| (previewResult.errorMessage != null && !previewResult.errorMessage.isEmpty())) {
				getContainer().showPage(getPreviousPage());
				inputPage.showError(previewResult.errorMessage, previewResult.erroneousParameters);
			} else {
				String result = MARKDOWN_PARSER.parseToHtml(previewResult.message);
				Display.getDefault().asyncExec(() -> {
					browser.setText(result);
					browser.getParent().getParent().layout(true, true);
					browser.getParent().redraw();
					browser.getParent().update();
				});
			}
		});
	}

	public void setServer(Server server) {
		this.server = server;
	}
}
