package de.cronn.assertions.validationfile;

import de.cronn.assertions.validationfile.sample.SampleStructure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AssertDiff.class)
public class AssertDiffTest_GlobalConfig {

	@Test
	void serializeObjectToJsonString() {
		AssertDiff.configure(new AssertDiffConfiguration()
				.fileNameSuffix("custom_suffix")
				.fileExtension(FileExtensions.CSV))
			.assertWithSnapshot(SampleStructure.filledWithConstantValues());
	}

}
