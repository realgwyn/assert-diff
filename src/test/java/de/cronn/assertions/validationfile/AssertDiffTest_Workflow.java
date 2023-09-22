package de.cronn.assertions.validationfile;

import de.cronn.assertions.validationfile.extension.ValidationFilesTestHelper;
import de.cronn.assertions.validationfile.util.FileBasedComparisonUtils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssertDiffTest_Workflow {

	@RegisterExtension
	static ValidationFilesTestHelper helper = new ValidationFilesTestHelper();

	@AfterAll
	static void tearDown() throws IOException {
		helper.cleanupFiles();
	}

	@Order(1)
	@Test
	void firstTestRun_throwsError_savesSnapshot() throws Exception {
		// Given
		assertThat(helper.collectAddedFiles()).isEmpty();

		// When
		Throwable error = catchThrowable(
			() -> AssertDiff.fileName("AssertDiff_workflow.txt").assertWithSnapshot("Actual content."));

		// Then
		assertThat(error).hasMessage("Snapshot doesn't exist at data/test/validation/AssertDiff_workflow.txt, saving actual snapshot as expected snapshot.");

		assertThat(helper.collectAddedFiles()).containsExactlyInAnyOrder(
			Paths.get("data/test/validation/AssertDiff_workflow.txt"),
			Paths.get("data/test/output/AssertDiff_workflow.txt"),
			Paths.get("data/test/tmp/AssertDiff_workflow.txt.raw"));

		assertThat(helper.linesDiffOutputValidation("AssertDiff_workflow.txt")).isEmpty();
	}

	@Order(2)
	@Test
	void secondTestRun_is_successful() throws Throwable {
		AssertDiff.fileName("AssertDiff_workflow.txt").assertWithSnapshot("Actual content.");
	}

	@Order(3)
	@Test
	void changedActualFile_throwsError() throws Throwable {
		// When
		Throwable error = catchThrowable(
			() -> AssertDiff.fileName("AssertDiff_workflow.txt").assertWithSnapshot("Actual content has changed."));

		// Then
		assertThat(error).hasMessage("\n" +
			"--- expected/AssertDiff_workflow.txt\n" +
			"+++ actual/AssertDiff_workflow.txt\n" +
			"@@ -1,1 +1,1 @@\n" +
			"-Actual content.\n" +
			"+Actual content has changed.");

		assertThat(helper.linesDiffOutputValidation("AssertDiff_workflow.txt"))
			.containsExactlyInAnyOrder("-Actual content.", "+Actual content has changed.");
	}

	@Order(4)
	@Test
	void copyingContentsOfActualToExpectedFile_fixesTest() throws Throwable {
		// Given
		assertThatThrownBy(
			() -> AssertDiff.fileName("AssertDiff_workflow.txt").assertWithSnapshot("Actual content has changed."))
			.isInstanceOf(FileBasedComparisonFailure.class);

		// When
		helper.copyOutputToValidation("AssertDiff_workflow.txt");

		// Then
		assertThatCode(
			() -> AssertDiff.fileName("AssertDiff_workflow.txt").assertWithSnapshot("Actual content has changed."))
			.doesNotThrowAnyException();
		assertThat(helper.linesDiffOutputValidation("AssertDiff_workflow.txt")).isEmpty();
	}

}
