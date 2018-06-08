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
package org.eclipse.ppp4j.messages;

public class ComponentVersion {
	public String id;
	public String title;
	public String caption;
	public Version[] versions;

	public ComponentVersion() {
	}

	public ComponentVersion(String id, String title, String caption, Version[] versions) {
		this.id = id;
		this.title = title;
		this.caption = caption;
		this.versions = versions;
	}
}
