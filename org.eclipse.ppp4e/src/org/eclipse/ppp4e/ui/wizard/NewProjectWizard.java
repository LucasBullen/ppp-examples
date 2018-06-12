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

import java.io.File;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ppp4e.ProvisioningPlugin;
import org.eclipse.ppp4e.core.Server;
import org.eclipse.ppp4e.core.StreamConnectionProvider;
import org.eclipse.ppp4j.messages.ProvisionResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public abstract class NewProjectWizard extends Wizard implements INewWizard {
	private NewProjectWizardPage inputPage;
	private NewProjectPreviewWizardPage previewPage;
	private Server server;

	protected abstract StreamConnectionProvider getStreamConnectionProvider();

	protected abstract String getWizardName();

	protected abstract File newFolderLocation();

	public NewProjectWizard() {
		inputPage = new NewProjectWizardPage(getWizardName());
		addPage(inputPage);
		previewPage = null;
		server = new Server(getStreamConnectionProvider(), getWizardName());
		if (server.openConnection()) {
			server.Initalize().thenAccept(initializeResult -> {
				inputPage.init(initializeResult, server);
				if (initializeResult.previewSupported) {
					previewPage = new NewProjectPreviewWizardPage();
					previewPage.setServer(server);
					Display.getDefault().asyncExec(() -> {
						addPage(previewPage);
						inputPage.updatedButtons();
					});
				}
			});
		} else {
			server = null;
			inputPage.init(null, null);
		}
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		Iterator<?> selectionIterator = selection.iterator();
		IResource asResource = null;
		while (selectionIterator.hasNext()) {
			Object element = selectionIterator.next();
			asResource = toResource(element);
			if (asResource != null) {
				inputPage.setDirectory(toFile(asResource));
			}
			break;
		}
		if (asResource == null) {
			inputPage.setDirectory(newFolderLocation());
		}
	}

	@Override
	public boolean performFinish() {
		try {
			ProvisioningParameters parameters = inputPage.getParameters();
			ProvisionResult result = server.Provision(parameters).get();// TODO: set up as a job to work in background
			if (result.erroneousParameters.length > 0
					|| (result.errorMessage != null && !result.errorMessage.isEmpty())) {
				if (getContainer().getCurrentPage() != inputPage) {
					getContainer().showPage(inputPage);
				}
				inputPage.showError(result.errorMessage, result.erroneousParameters);
				return false;
			}
			createProject(parameters.name, result.location, result.openFiles);
			return true;
		} catch (InterruptedException | ExecutionException e) {
			ProvisioningPlugin.logError(e);
			return false;
		}
	}

	private void createProject(String name, String directory, String[] openFiles) { // TODO: link into a job/monitor
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(name);
		IProjectDescription description = root.getWorkspace().newProjectDescription(project.getName());
		description.setLocation(Path.fromOSString(directory));
		try {
			project.create(description, new NullProgressMonitor());
			project.open(new NullProgressMonitor());
			project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			MessageDialog.openError(getShell(), "Unable to create project", e.toString());
		}

		Display.getDefault().asyncExec(() -> {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if (page != null) {
				try {
					for (String filePath : openFiles) {
						IFile rsPrgramFile = project.getFile(filePath);
						if (rsPrgramFile.exists()) {
							IDE.openEditor(page, rsPrgramFile);
						}
					}
				} catch (CoreException e) {
					MessageDialog.openError(getShell(), "Unable to open project", e.toString());
				}
			}
		});
	}

	@Override
	public void dispose() {
		if (server != null) {
			server.closeConnection();
		}
		super.dispose();
	}

	private IResource toResource(Object o) {
		if (o instanceof IResource) {
			return (IResource) o;
		} else if (o instanceof IAdaptable) {
			return ((IAdaptable) o).getAdapter(IResource.class);
		} else {
			return null;
		}
	}

	private File toFile(IResource r) {
		IPath location = r.getLocation();
		if (location.toFile().isFile()) {
			return location.toFile().getParentFile().getAbsoluteFile();
		}
		return location == null ? null : location.toFile();
	}

}
