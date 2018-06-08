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

public class ProvisionInstructionsResult {
	public String errorMessage;
	public ErroneousParameter[] erroneousParameters;
	public String name;
	public Instruction[] newFiles;
	public String[] openFiles;

	public ProvisionInstructionsResult() {
	}

	public ProvisionInstructionsResult(String errorMessage, ErroneousParameter[] erroneousParameters, String name,
			Instruction[] newFiles, String[] openFiles) {
		this.errorMessage = errorMessage;
		this.erroneousParameters = erroneousParameters;
		this.name = name;
		this.newFiles = newFiles;
		this.openFiles = openFiles;
	}
}
