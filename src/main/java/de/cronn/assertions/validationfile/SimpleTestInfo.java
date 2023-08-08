package de.cronn.assertions.validationfile;

public class SimpleTestInfo {

	final Class<?> testClass;
	final String methodName;

	public SimpleTestInfo(Class<?> testClass, String methodName) {
		this.testClass = testClass;
		this.methodName = methodName;
	}

	public Class<?> getTestClass() {
		return testClass;
	}

	public String getMethodName() {
		return methodName;
	}
}
