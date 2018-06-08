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

public enum ParameterType {
	Name(1), Location(2), Version(3), ComponentVersion(4), Template(5), TemplateComponentVersion(6);

	private final int value;

	ParameterType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static ParameterType forValue(int value) {
		ParameterType[] allValues = ParameterType.values();
		if (value < 1 || value > allValues.length) {
			throw new IllegalArgumentException("Illegal enum value: " + value);
		}
		return allValues[value - 1];
	}
}
