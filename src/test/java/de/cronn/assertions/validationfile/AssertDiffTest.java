package de.cronn.assertions.validationfile;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AssertDiff.class)
class AssertDiffTest {

	static List<String> testNamesBefore = new ArrayList<>();
	static List<String> testNamesAfter = new ArrayList<>();

	@BeforeEach
	void setUp() {
		testNamesBefore.add(AssertDiff.getTestName());
	}

	@AfterEach
	void tearDown() {
		testNamesAfter.add(AssertDiff.getTestName());
	}

	@AfterAll
	static void afterAll() {
		assertThat(testNamesBefore).containsExactlyInAnyOrder(
			"AssertDiffTest_otherTest",
			"AssertDiffTest_myTest",
			"AssertDiffTest_dynamicTests"
		);
		assertThat(testNamesAfter).containsExactlyInAnyOrder(
			"AssertDiffTest_otherTest",
			"AssertDiffTest_myTest",
			"AssertDiffTest_dynamicTests"
		);
	}

	@Test
	void myTest() {
		assertThat(AssertDiff.getTestName()).isEqualTo("AssertDiffTest_myTest");
	}

	@Test
	void otherTest() {
		assertThat(AssertDiff.getTestName()).isEqualTo("AssertDiffTest_otherTest");
	}

	@TestFactory
	Collection<DynamicTest> dynamicTests() {
		return Arrays.asList(
			DynamicTest.dynamicTest("dynamic test", () ->
				assertThat(AssertDiff.getTestName()).isEqualTo("AssertDiffTest_dynamicTests")),
			DynamicTest.dynamicTest("other dynamic test", () ->
				assertThat(AssertDiff.getTestName()).isEqualTo("AssertDiffTest_dynamicTests"))
		);
	}

}
