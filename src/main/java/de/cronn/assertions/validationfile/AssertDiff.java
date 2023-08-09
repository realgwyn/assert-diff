package de.cronn.assertions.validationfile;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;
import de.cronn.assertions.validationfile.util.FileBasedComparisonUtils;
import de.cronn.assertions.validationfile.util.TestNameUtils;

public class AssertDiff implements BeforeEachCallback, AfterEachCallback {

	private static ThreadLocal<AssertDiffConfiguration> methodLevelConfig = ThreadLocal.withInitial(() -> null);
	private static ThreadLocal<SimpleTestInfo> testInfo = ThreadLocal.withInitial(() -> null);

	public static AssertDiff configure(AssertDiffConfiguration configuration){
		methodLevelConfig.set(configuration);
		return null;
	}

	@Override
	public void beforeEach(ExtensionContext context) {
		Class<?> testClass = context.getTestClass().orElseThrow(() -> new IllegalStateException("No test class"));
		Method testMethod = context.getTestMethod().orElseThrow(() -> new IllegalStateException("No test method"));
		testInfo.set(new SimpleTestInfo(testClass, testMethod.getName()));
		methodLevelConfig.set(new AssertDiffConfiguration());
	}

	@Override
	public void afterEach(ExtensionContext context) {
		testInfo.remove();
		methodLevelConfig.remove();
	}

	protected static String getTestName() {
		if (testInfo.get() == null) {
			throw new IllegalStateException("\nAssertDiff:\n\tCould not resolve the name of this test. Please annotate your test class with: @ExtendWith(AssertDiff.class)");
		}

		Class<?> testClass = testInfo.get().getTestClass();
		String testMethodName = testInfo.get().getMethodName();
		return TestNameUtils.getTestName(testClass, testMethodName);
	}

	public static final ObjectMapper DEFAULT_OBJECT_MAPPER = JsonMapper.builder()
		.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
		.configure(SerializationFeature.INDENT_OUTPUT, true)
		.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
		.findAndAddModules()
		.build()
		.setSerializationInclusion(JsonInclude.Include.NON_NULL);

	public static final FileExtension DEFAULT_OBJECT_MAPPER_EXTENSION = FileExtensions.JSON;

	public static String getValidationFileName(String baseName, String suffix, FileExtension extension) {
		return getValidationFileName(baseName, suffix, extension.asString());
	}

	public static String getValidationFileName(String baseName, String extension) {
		return baseName + "." + extension;
	}

	public static String getValidationFileName(String baseName, String suffix, String extension) {
		if (suffix == null || suffix.isEmpty()) {
			return getValidationFileName(baseName, extension);
		}
		return getValidationFileName(baseName + "_" + suffix, extension);
	}



	public static void assertWithSnapshot(Object object) {
		try {
			AssertDiffConfiguration config = methodLevelConfig.get();
			assertWithSnapshot(
				config.getObjectMapper().writeValueAsString(object),
				getValidationFileName(getTestName(), config.getFileNameSuffix(), config.getFileExtension()),
				config.getNormalizer());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static void assertWithSnapshot(String actualString) {
		AssertDiffConfiguration config = methodLevelConfig.get();
		assertWithSnapshot(actualString,
			getValidationFileName(getTestName(), config.getFileNameSuffix(), config.getFileExtension()),
			config.getNormalizer());
	}

	public static void assertWithSnapshot(String actualString, FileExtension extension) {
		assertWithSnapshot(actualString, getValidationFileName(getTestName(), extension.asString()), null);
	}

	public static void assertWithSnapshot(String actualOutput, String filename, ValidationNormalizer normalizer) {
		FileBasedComparisonUtils.compareActualWithFileHidden(actualOutput, filename, normalizer);
	}

	public static void assertWithSnapshot(String actualOutput, ValidationNormalizer validationNormalizer, FileExtension extension) {
		assertWithSnapshot(actualOutput, getValidationFileName(getTestName(), extension.asString()), validationNormalizer);
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
