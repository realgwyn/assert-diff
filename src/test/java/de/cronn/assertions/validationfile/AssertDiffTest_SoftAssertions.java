package de.cronn.assertions.validationfile;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(AssertDiff.class)
class AssertDiffTest_SoftAssertions {

	SoftAssertions softly = new SoftAssertions();

	@Test
	void correctUsageOfSoftAssertions() {
		assertThatThrownBy(() -> {
			softly.check(() -> AssertDiff.assertWithSnapshot("this is first failing test"));
			softly.fail("and one normal failure after");
			softly.check(() -> AssertDiff.assertWithSnapshot("this failing test could not be reached without soft assertions"));
			softly.assertAll();
		}).hasMessageContainingAll(
			"this is first failing test",
			"and one normal failure after",
			"this failing test could not be reached without soft assertions"
		);
	}

	@Test
	void incorrectUsageOfSoftAssertions() {
		assertThatThrownBy(() -> {
			AssertDiff.assertWithSnapshot("this is first failing test");
			softly.fail("and one normal failure after");
			AssertDiff.assertWithSnapshot("this failing test could not be reached without soft assertions");
			softly.assertAll();
		}).hasMessageContainingAll(
			"this is first failing test"
		);
	}

}
