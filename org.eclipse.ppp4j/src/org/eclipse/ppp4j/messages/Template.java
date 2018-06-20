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

public class Template {
	public String id;
	public String title;
	public String caption;
	public ComponentVersion[] componentVersions;

	public Template() {
	}

	public Template(String id, String title, String caption, ComponentVersion[] componentVersions) {
		this.id = id;
		this.title = title;
		this.caption = caption;
		this.componentVersions = componentVersions;
	}

	public ComponentVersion getComponentVersionById(String id) {
		for (ComponentVersion componentVersion : componentVersions) {
			if (componentVersion.id.equals(id)) {
				return componentVersion;
			}
		}
		return null;
	}
}
