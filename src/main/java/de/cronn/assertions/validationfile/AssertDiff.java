package de.cronn.assertions.validationfile;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;
import de.cronn.assertions.validationfile.serialization.JsonSerializer;
import de.cronn.assertions.validationfile.serialization.ObjectSerializer;
import de.cronn.assertions.validationfile.util.FileBasedComparisonUtils;

import de.cronn.assertions.validationfile.util.TestNameUtils;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

import static de.cronn.assertions.validationfile.AssertDiffConfig.*;

public class AssertDiff implements BeforeEachCallback, AfterEachCallback {

	public static final AssertDiffConfig DEFAULT_CONFIG = new AssertDiffConfig();

	private static AssertDiffConfig globalConfig = DEFAULT_CONFIG;

	private static ThreadLocal<SimpleTestInfo> testInfo = ThreadLocal.withInitial(() -> null);

	public static void assertWithSnapshot(Object object) {
		AssertDiff.assertWithSnapshot(globalConfig.getObjectSerializer().serialize(object),
			getValidationFileName(getTestName(), globalConfig.getSuffix(),
				globalConfig.getFileExtension() != null ? globalConfig.getFileExtension() : globalConfig.getObjectSerializer().getFileExtension()),
			globalConfig.getValidationNormalizer());
	}

	public static void assertWithSnapshot(String actualString) {
		AssertDiff.assertWithSnapshot(actualString,
			getValidationFileName(getTestName(), globalConfig.getSuffix(),
				globalConfig.getFileExtension() != null ? globalConfig.getFileExtension() : DEFAULT_FILE_EXTENSION),
			globalConfig.getValidationNormalizer());
	}

	public static void assertWithSnapshot(String actualString, String suffix, String fileExtension, ValidationNormalizer normalizer) {
		AssertDiff.assertWithSnapshot(actualString, getValidationFileName(getTestName(), suffix, fileExtension), normalizer);
	}

	public static void assertWithSnapshot(String actualOutput, String filename, ValidationNormalizer normalizer) {
		FileBasedComparisonUtils.compareActualWithFileHidden(actualOutput, filename, normalizer);
	}

	public static AssertDiffConfig fileExtension(FileExtension fileExtension) {
		return new AssertDiffConfig().fileExtension(fileExtension);
	}

	public static AssertDiffConfig fileExtension(String fileExtension) {
		return new AssertDiffConfig().fileExtension(fileExtension);
	}

	public static AssertDiffConfig suffix(String suffix){
		return new AssertDiffConfig().suffix(suffix);
	}

	public static AssertDiffConfig validationNormalizer(ValidationNormalizer validationNormalizer) {
		return new AssertDiffConfig().validationNormalizer(validationNormalizer);
	}

	public static AssertDiffConfig objectSerializer(ObjectSerializer objectSerializer) {
		return new AssertDiffConfig().objectSerializer(objectSerializer);
	}

	@Override
	public void beforeEach(ExtensionContext context) {
		Class<?> testClass = context.getTestClass().orElseThrow(() -> new IllegalStateException("Missing test class"));
		Method testMethod = context.getTestMethod().orElseThrow(() -> new IllegalStateException("Missing test method"));
		testInfo.set(new SimpleTestInfo(testClass, testMethod.getName()));
	}

	@Override
	public void afterEach(ExtensionContext context) {
		testInfo.remove();
	}

	protected static String getTestName() {
		if(testInfo.get() == null){
			throw new IllegalStateException("\nAssertDiff:\n\tCould not resolve the name of this test. Please annotate your test class with: @ExtendWith(AssertDiff.class)");
		}

		Class<?> testClass = testInfo.get().getTestClass();
		String testMethodName = testInfo.get().getMethodName();
		return TestNameUtils.getTestName(testClass, testMethodName);
	}

	public static void configure(AssertDiffConfig globalConfig) {
		AssertDiff.globalConfig = globalConfig;
	}

	public static String getValidationFileName(String baseName, String suffix, FileExtension extension) {
		return getValidationFileName(baseName, suffix, extension.asString());
	}

	public static String getValidationFileName(String baseName, String suffix, String extension) {
		if (suffix == null || suffix.isEmpty()) {
			return getValidationFileName(baseName, extension);
		}
		return getValidationFileName(baseName + "_" + suffix, extension);
	}

	public static String getValidationFileName(String baseName, String extension) {
		return baseName + "." + extension;
	}

}
