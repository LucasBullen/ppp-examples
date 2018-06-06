package org.eclipse.ppp4j.messages;

public enum ParameterType {
	Name(1), Location(2), Version(3), Template(4), TemplateComponentVersion(5), ComponentVersion(6);

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
