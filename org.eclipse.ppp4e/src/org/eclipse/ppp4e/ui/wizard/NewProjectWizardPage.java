package org.eclipse.ppp4e.ui.wizard;

import java.io.File;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ppp4e.ProvisioningPlugin;
import org.eclipse.ppp4j.messages.InitializeResult;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkingSet;

public class NewProjectWizardPage extends WizardPage {
	private Set<IWorkingSet> workingSets;
	private File directory;
	private InitializeResult initializeResult;
	Semaphore initSemaphore = new Semaphore(0);
	boolean initialized = false;

	Label loadingLabel;

	Text nameInput;
	Text locationInput;
	Text versionInput;

	protected NewProjectWizardPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		CompletableFuture.runAsync(() -> {
			try {
				initSemaphore.acquire();
				Display.getDefault().asyncExec(() -> {
					removeLoadingControl();
					createParameterControl(parent);
				});
			} catch (InterruptedException e) {
				ProvisioningPlugin.logError(e);
				getWizard().performCancel();
			}
		});
		addLoadingControl(parent);
	}

	private void addLoadingControl(Composite parent) {
		Composite container = (Composite) getControl();
		if (container == null) {
			container = new Composite(parent, SWT.NULL);
			setControl(container);
		}
		if (initialized) {
			return;
		}
		container.setLayout(new GridLayout(1, false));
		loadingLabel = new Label(container, SWT.NONE);
		loadingLabel.setText("Loading wizard from Project Provisioning Server...");
		if (initialized) {
			removeLoadingControl();
		}
	}

	private void removeLoadingControl() {
		if (!loadingLabel.isDisposed()) {
			loadingLabel.dispose();
		}
	}

	private void createParameterControl(Composite parent) {
		Composite container = (Composite) getControl();
		if (container == null) {
			container = new Composite(parent, SWT.NULL);
			setControl(container);
		}
		container.setLayout(new GridLayout(2, false));

		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText("Name:");
		nameInput = new Text(container, SWT.BORDER);
		nameInput.setText(initializeResult.defaultProvisioningParameters.name);

		Label lcoationLabel = new Label(container, SWT.NONE);
		lcoationLabel.setText("Location:");
		locationInput = new Text(container, SWT.BORDER);
		locationInput.setText(initializeResult.defaultProvisioningParameters.location);

		if (initializeResult.versionRequired) {
			Label versionLabel = new Label(container, SWT.NONE);
			versionLabel.setText("Version:");
			versionInput = new Text(container, SWT.NONE);
			versionInput.setText(initializeResult.defaultProvisioningParameters.version);
		}
		container.getParent().layout(true, true);
		container.redraw();
		container.update();
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public void setWorkingSets(Set<IWorkingSet> workingSets) {
		this.workingSets = workingSets;
	}

	public void init(InitializeResult initializeResult) {
		this.initializeResult = initializeResult;
		initialized = true;
		initSemaphore.release();
	}

}
