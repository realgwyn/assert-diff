package de.cronn.assertions.validationfile;

import de.cronn.assertions.validationfile.sample.SampleStructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AssertDiff.class)
public class AssertDiffTest_Serialization {

	@Test
	void serializeObjectToJsonString() {
		AssertDiff.assertWithSnapshot(SampleStructure.filledWithConstantValues());
	}

}
