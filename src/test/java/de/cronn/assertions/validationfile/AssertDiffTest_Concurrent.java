package de.cronn.assertions.validationfile;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(AssertDiff.class)
class AssertDiffTest_Concurrent {

	@Test
	void testOne() {
		assertThat(AssertDiff.getTestName()).isEqualTo("AssertDiffTest_Concurrent_testOne");
	}

	@Test
	void testTwo() {
		assertThat(AssertDiff.getTestName()).isEqualTo("AssertDiffTest_Concurrent_testTwo");
	}

	@Test
	void testThree() {
		assertThat(AssertDiff.getTestName()).isEqualTo("AssertDiffTest_Concurrent_testThree");
	}
}
