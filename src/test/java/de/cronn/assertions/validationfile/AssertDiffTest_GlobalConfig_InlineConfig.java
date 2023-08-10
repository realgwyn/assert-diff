package de.cronn.assertions.validationfile;

import de.cronn.assertions.validationfile.sample.SampleStructure;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AssertDiff.class)
public class AssertDiffTest_GlobalConfig_InlineConfig {

	@BeforeAll
	public static void setupGlobalConfig() {
		AssertDiff.configure(new AssertDiffConfig().suffix("global_config"));
	}

	@AfterAll
	public static void restoreDefaults() {
		AssertDiff.configure(AssertDiff.DEFAULT_CONFIG);
	}

	@Test
	public void globalConfigShouldBeAppliedToThisTest() {
		AssertDiff.assertWithSnapshot(SampleStructure.filledWithConstantValues());
	}

	@Test
	void globalConfigShouldAlsoBeAppliedToThisTest() {
		AssertDiff.assertWithSnapshot(SampleStructure.filledWithConstantValues());
	}

	@Test
	void inlineConfigShouldOverrideGlobalConfig(){
		AssertDiff.suffix("inline_config").assertWithSnapshot(SampleStructure.filledWithConstantValues());
	}

}
