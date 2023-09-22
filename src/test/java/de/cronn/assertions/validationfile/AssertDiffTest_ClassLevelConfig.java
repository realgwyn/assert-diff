package de.cronn.assertions.validationfile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.cronn.assertions.validationfile.sample.SampleStructure;

@ExtendWith(AssertDiff.class)
public class AssertDiffTest_ClassLevelConfig {

	AssertDiffConfig assertDiff = new AssertDiffConfig().suffix("class_level");

	@Test
	void testCaseA() {
		assertDiff.assertWithSnapshot(SampleStructure.filledWithConstantValues());
	}

	@Test
	void testCaseB() {
		assertDiff.assertWithSnapshot(SampleStructure.filledWithConstantValues());
	}

}
