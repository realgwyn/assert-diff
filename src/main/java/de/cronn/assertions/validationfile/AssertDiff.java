package de.cronn.assertions.validationfile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;
import de.cronn.assertions.validationfile.util.FileBasedComparisonUtils;

import de.cronn.assertions.validationfile.util.TestNameUtils;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

public class AssertDiff implements BeforeEachCallback, AfterEachCallback {

	private static ThreadLocal<String> currentTestName = ThreadLocal.withInitial(() -> null);

//	private static ThreadLocal<SimpleTestInfo> testInfo = ThreadLocal.withInitial(() -> null);

	@Override
	public void beforeEach(ExtensionContext context) {
		currentTestName.set(
			context.getTestClass().map(clazz -> clazz.getSimpleName()).orElse(null)
			+ "_" + context.getTestMethod().map(method -> method.getName()).orElse(null));

//		Class<?> testClass = context.getTestClass().orElseThrow(() -> new IllegalStateException("No test class"));
//		Method testMethod = context.getTestMethod().orElseThrow(() -> new IllegalStateException("No test method"));
//		testInfo.set(new SimpleTestInfo(testClass, testMethod.getName()));
	}

	@Override
	public void afterEach(ExtensionContext context) {
		currentTestName.remove();

//		testInfo.remove();
	}

	protected static String getTestName() {
		if(currentTestName.get() == null){
			throw new IllegalStateException("\nAssertDiff:\n\tCould not resolve the name of this test. Please annotate your test class with: @ExtendWith(AssertDiff.class)");
		}else {
			return currentTestName.get();
		}

//		Class<?> testClass = testInfo.get().getClass();
//		String testMethodName = testInfo.get().methodName;
//		return TestNameUtils.getTestName(testClass, testMethodName);
	}

	static final ObjectMapper DEFAULT_OBJECT_MAPPER = JsonMapper.builder()
		.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
		.configure(SerializationFeature.INDENT_OUTPUT, true)
		.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
		.findAndAddModules()
		.build()
		.setSerializationInclusion(JsonInclude.Include.NON_NULL);

	public static String getValidationFileName(String baseName, FileExtension extension) {
		return baseName + "." + extension.asString();
	}

	public static String getValidationFileName(String baseName, String suffix, FileExtension extension) {
		if (suffix == null || suffix.isEmpty()) {
			return getValidationFileName(baseName, extension);
		}
		return getValidationFileName(baseName + "_" + suffix, extension);
	}

	public static void assertWithSnapshot(Object object) {
		try {
			assertWithSnapshotJson(DEFAULT_OBJECT_MAPPER.writeValueAsString(object));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static void assertWithSnapshot(String actualString) {
		assertWithSnapshot(actualString, FileExtensions.TXT);
	}

	public static void assertWithSnapshot(String actualString, FileExtension extension) {
		assertWithSnapshot(actualString, getValidationFileName(getTestName(), extension), null);
	}

	public static void assertWithSnapshot(String actualOutput, String filename, ValidationNormalizer normalizer) {
		FileBasedComparisonUtils.compareActualWithFileHidden(actualOutput, filename, normalizer);
	}

	public static void assertWithSnapshot(String actualOutput, ValidationNormalizer validationNormalizer, FileExtension extension) {
		assertWithSnapshot(actualOutput, getValidationFileName(getTestName(), extension), validationNormalizer);
	}

	public static void assertWithSnapshot(String actualOutput, ValidationNormalizer validationNormalizer) {
		assertWithSnapshot(actualOutput, validationNormalizer, FileExtensions.TXT);
	}

	public static void assertWithSnapshotXml(String actualXmlString) {
		assertWithSnapshot(actualXmlString, FileExtensions.XML);
	}

	public static void assertWithSnapshotXml(String actualXmlString, ValidationNormalizer validationNormalizer) {
		assertWithSnapshot(actualXmlString, validationNormalizer, FileExtensions.XML);
	}

	public static void assertWithSnapshotJson(String actualJsonString) {
		assertWithSnapshot(actualJsonString, FileExtensions.JSON);
	}

	public static void assertWithSnapshotWithSuffix(String actualString, String suffix, FileExtension extension) {
		assertWithSnapshotWithSuffix(actualString, null, suffix, extension);
	}

	public static void assertWithSnapshotWithSuffix(String actualString, ValidationNormalizer validationNormalizer, String suffix, FileExtension extension) {
		assertWithSnapshot(actualString, getValidationFileName(getTestName(), suffix, extension), validationNormalizer);
	}

	public static void assertWithSnapshotWithSuffix(String actualString, String suffix) {
		assertWithSnapshotWithSuffix(actualString, suffix, FileExtensions.TXT);
	}

	public static void assertWithSnapshotWithSuffix(String actualString, ValidationNormalizer validationNormalizer, String suffix) {
		assertWithSnapshotWithSuffix(actualString, validationNormalizer, suffix, FileExtensions.TXT);
	}

	public static void assertWithSnapshotJsonWithSuffix(String actualJsonString, String suffix) {
		assertWithSnapshotWithSuffix(actualJsonString, suffix, FileExtensions.JSON);
	}

	public static void assertWithSnapshotJsonWithSuffix(String actualJsonString, ValidationNormalizer validationNormalizer, String suffix) {
		assertWithSnapshotWithSuffix(actualJsonString, validationNormalizer, suffix, FileExtensions.JSON);
	}

	public static void assertWithSnapshotJson(String actualJsonString, ValidationNormalizer validationNormalizer) {
		assertWithSnapshot(actualJsonString, validationNormalizer, FileExtensions.JSON);
	}

	public static void assertWithSnapshotJson5(String actualJsonString) {
		assertWithSnapshot(actualJsonString, FileExtensions.JSON5);
	}

	public static void assertWithSnapshotJson5WithSuffix(String actualJsonString, String suffix) {
		assertWithSnapshotWithSuffix(actualJsonString, suffix, FileExtensions.JSON5);
	}

	public static void assertWithSnapshotJson5WithSuffix(String actualJsonString, ValidationNormalizer validationNormalizer, String suffix) {
		assertWithSnapshotWithSuffix(actualJsonString, validationNormalizer, suffix, FileExtensions.JSON5);
	}

	public static void assertWithSnapshotJson5(String actualJsonString, ValidationNormalizer validationNormalizer) {
		assertWithSnapshot(actualJsonString, validationNormalizer, FileExtensions.JSON5);
	}


	public static void assertWithSnapshotXmlWithSuffix(String actualXmlString, String suffix) {
		assertWithSnapshotWithSuffix(actualXmlString, suffix, FileExtensions.XML);
	}

	public static void assertWithSnapshotXmlWithSuffix(String actualXmlString, ValidationNormalizer validationNormalizer, String suffix) {
		assertWithSnapshotWithSuffix(actualXmlString, validationNormalizer, suffix, FileExtensions.XML);
	}

}
