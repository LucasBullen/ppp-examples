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
package org.eclipse.ppp4e;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class ProvisioningPlugin extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "org.eclipse.ppp4e"; //$NON-NLS-1$ ;

	public static final boolean DEBUG = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/debug")); //$NON-NLS-1$

	// The shared instance
	private static ProvisioningPlugin plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ProvisioningPlugin getDefault() {
		return plugin;
	}

	/**
	 * Utility method to log errors.
	 *
	 * @param thr The exception through which we noticed the error
	 */
	public static void logError(final Throwable thr) {
		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, 0, thr.getMessage(), thr));
	}

	/**
	 * Utility method to log errors.
	 *
	 * @param message User comprehensible message
	 * @param thr     The exception through which we noticed the error
	 */
	public static void logError(final String message, final Throwable thr) {
		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, 0, message, thr));
	}

	/**
	 * Log an info message for this plug-in
	 *
	 * @param message
	 */
	public static void logInfo(final String message) {
		getDefault().getLog().log(new Status(IStatus.INFO, PLUGIN_ID, 0, message, null));
	}

	/**
	 * Utility method to log warnings for this plug-in.
	 *
	 * @param message User comprehensible message
	 * @param thr     The exception through which we noticed the warning
	 */
	public static void logWarning(final String message, final Throwable thr) {
		getDefault().getLog().log(new Status(IStatus.WARNING, PLUGIN_ID, 0, message, thr));
	}
}
