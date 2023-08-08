package de.cronn.assertions.validationfile;

import de.cronn.assertions.validationfile.extension.CleanupValidationFilesAfterAllTests;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(CleanupValidationFilesAfterAllTests.class)
@ExtendWith(AssertDiff.class)
class AssertDiffTest_SoftAssertions {

	SoftAssertions softly = new SoftAssertions();

	@Test
	void correctUsageOfSoftAssertions() {
		assertThatThrownBy(() -> {
			softly.check(() -> AssertDiff.assertWithSnapshot("lorem ipsum"));
			softly.check(() -> AssertDiff.assertWithSnapshot("another"));
			softly.fail("and one normal failure after");
			softly.check(() -> AssertDiff.assertWithSnapshot("not reached without soft assertions"));
			softly.assertAll();
		}).hasMessageContainingAll(
			"lorem ipsum",
			"another",
			"and one normal failure after",
			"not reached without soft assertions"
		);
	}

	@Test
	void incorrectUsageOfSoftAssertions() {
		assertThatThrownBy(() -> {
			AssertDiff.assertWithSnapshot("lorem ipsum");
			AssertDiff.assertWithSnapshot("another");
			softly.fail("and one normal failure after");
			AssertDiff.assertWithSnapshot("not reached without soft assertions");
			softly.assertAll();
		}).hasMessageContainingAll(
			"lorem ipsum"
		);
	}

}
