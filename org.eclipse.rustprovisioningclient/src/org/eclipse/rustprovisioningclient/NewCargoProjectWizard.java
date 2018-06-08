package org.eclipse.rustprovisioningclient;

import org.eclipse.ppp4e.core.StreamConnectionProvider;
import org.eclipse.ppp4e.ui.wizard.NewProjectWizard;

public class NewCargoProjectWizard extends NewProjectWizard {
	private RustStreamConnectionProvider connectionProvider;

	@Override
	protected StreamConnectionProvider getStreamConnectionProvider() {
		if (connectionProvider == null) {
			connectionProvider = new RustStreamConnectionProvider();
		}
		return connectionProvider;
	}

	@Override
	protected String getWizardName() {
		return "New Cargo Project";
	}

}
