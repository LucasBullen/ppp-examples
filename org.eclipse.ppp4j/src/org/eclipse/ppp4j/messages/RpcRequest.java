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

public class RpcRequest {
	final public String jsonrcp = "2.0";
	public String id;
	public String method;
	public Object params;

	public RpcRequest(String id, String method, Object params) {
		this.id = id;
		this.method = method;
		this.params = params;
	}
}
