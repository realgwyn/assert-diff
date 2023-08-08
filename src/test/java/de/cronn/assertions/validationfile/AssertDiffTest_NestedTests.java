package de.cronn.assertions.validationfile;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(AssertDiff.class)
public class AssertDiffTest_NestedTests {

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
			"AssertDiffTest_NestedTests_NestedTest_nestedTest",
			"AssertDiffTest_NestedTests_NestedTest_NestedNestedTest_nestedNestedTest"
		);
		assertThat(testNamesAfter).containsExactlyInAnyOrder(
			"AssertDiffTest_NestedTests_NestedTest_nestedTest",
			"AssertDiffTest_NestedTests_NestedTest_NestedNestedTest_nestedNestedTest"
		);
	}

	@Nested
	class NestedTest {

		@Test
		void nestedTest() {
			assertThat(AssertDiff.getTestName()).isEqualTo("AssertDiffTest_NestedTests_NestedTest_nestedTest");
		}

		@Nested
		class NestedNestedTest {

			@Test
			void nestedNestedTest() {
				assertThat(AssertDiff.getTestName()).isEqualTo("AssertDiffTest_NestedTests_NestedTest_NestedNestedTest_nestedNestedTest");
			}
		}

	}

}
