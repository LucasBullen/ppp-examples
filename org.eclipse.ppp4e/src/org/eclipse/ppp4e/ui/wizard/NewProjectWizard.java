package org.eclipse.ppp4e.ui.wizard;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Adapters;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ppp4e.ProvisioningPlugin;
import org.eclipse.ppp4e.core.Server;
import org.eclipse.ppp4e.core.StreamConnectionProvider;
import org.eclipse.ppp4j.messages.ComponentVersionSelection;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;

public abstract class NewProjectWizard extends Wizard implements INewWizard {
	private NewProjectWizardPage inputPage;
	private NewProjectPreviewWizardPage previewPage;
	Semaphore initSemaphore = new Semaphore(0);
	private Server server;

	public NewProjectWizard() {
		server = new Server(getStreamConnectionProvider());
	}

	protected abstract StreamConnectionProvider getStreamConnectionProvider();

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		inputPage = new NewProjectWizardPage("name");
		CompletableFuture.runAsync(() -> {
			server.Initalize()
			.thenAccept(initializeResult -> {
				inputPage.init(initializeResult);
				try {
					initSemaphore.acquire();
					if (initializeResult.validationSupported) {
						previewPage = new NewProjectPreviewWizardPage();
						addPage(previewPage);
					}
				} catch (InterruptedException e) {
					ProvisioningPlugin.logError(e);
				}
			});
		});
		Iterator<?> selectionIterator = selection.iterator();
		Set<IWorkingSet> workingSets = new HashSet<>();
		IResource selectedResource = null;

		while (selectionIterator.hasNext()) {
			Object element = selectionIterator.next();
			IResource asResource = toResource(element);

			if (asResource != null && selectedResource == null) {
				selectedResource = asResource;
			} else {
				IWorkingSet asWorkingSet = Adapters.adapt(element, IWorkingSet.class);
				if (asWorkingSet != null) {
					workingSets.add(asWorkingSet);
				}
			}
		}

		if (workingSets.isEmpty() && selectedResource != null) {
			workingSets.addAll(getWorkingSets(selectedResource));
		}
		inputPage.setWorkingSets(workingSets);

		if (selectedResource != null) {
			inputPage.setDirectory(toFile(selectedResource));
		} else {
			inputPage.setDirectory(newFolderLocation());
		}
	}

	@Override
	public void addPages() {
		addPage(inputPage);
		initSemaphore.release();
	}

	@Override
	public boolean performFinish() {
		String name = "name";
		String location = "location";
		String version = "version";
		CompletableFuture.runAsync(() -> {
			ProvisioningParameters parameters = new ProvisioningParameters(name, location, version, null,
					new ComponentVersionSelection[0]);
			server.Provision(parameters)
			.thenAccept(provisionResult -> {
				System.out.println("First New File:" + provisionResult.newFiles[0]);
			});
		});
		return false;
	}

	protected File newFolderLocation() {
		return null;
	}

	private Set<IWorkingSet> getWorkingSets(IResource resource) {
		IWorkingSet[] allWorkingSets = PlatformUI.getWorkbench().getWorkingSetManager().getAllWorkingSets();
		Set<IWorkingSet> fileWorkingSets = new HashSet<>();

		for (IWorkingSet iWorkingSet : allWorkingSets) {
			IAdaptable[] elements = iWorkingSet.getElements();
			if (Arrays.asList(elements).contains(resource.getProject())) {
				fileWorkingSets.add(iWorkingSet);
			}
		}

		return fileWorkingSets;
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
