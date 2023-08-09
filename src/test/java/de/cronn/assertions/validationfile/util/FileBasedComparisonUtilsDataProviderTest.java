package de.cronn.assertions.validationfile.util;


import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import de.cronn.assertions.validationfile.AssertDiff;

@ExtendWith(AssertDiff.class)
class FileBasedComparisonUtilsDataProviderTest {

	private static Stream<Arguments> objectDataProvider() {
		return Stream.of(
			arguments("one!", "2016-01-01T00:00:00.123456Z"),
			arguments("two+", "2016-01-01")
		);
	}

	@ParameterizedTest
	@ValueSource(strings = { "one!", "t \nw*o+" })
	void shouldCheckAgainstValidationFile(String input) {
		AssertDiff.suffix(input)
			.assertWithSnapshot(input);
	}

	@ParameterizedTest
	@MethodSource("objectDataProvider")
	void shouldCheckAgainstValidationFile(String input, Object secondValue) {
		AssertDiff.suffix(input + "_" + secondValue)
			.assertWithSnapshot(
			String.format("%sX%s", input, secondValue)
		);
	}

}
