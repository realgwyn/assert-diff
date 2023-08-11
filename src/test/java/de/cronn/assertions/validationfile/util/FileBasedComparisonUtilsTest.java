package de.cronn.assertions.validationfile.util;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import de.cronn.assertions.validationfile.FileBasedComparisonFailure;
import de.cronn.assertions.validationfile.extension.ValidationFilesTestHelper;

class FileBasedComparisonUtilsTest {

	@Test
	void testReplaceCharactersThatAreForbiddenInWindowsFileNames() throws Exception {
		final String someAllowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabzdefghijklmnopqrstuvwxyz0123456789.-_/";
		assertThat(FileBasedComparisonUtils.validateAndFixFilename(someAllowedCharacters)).isEqualTo(someAllowedCharacters);

		assertThat(FileBasedComparisonUtils.validateAndFixFilename("some/path_with_back\\slash.ext"))
			.isEqualTo("some/path_with_back_slash.ext");

		assertThat(FileBasedComparisonUtils.validateAndFixFilename("<>:\"|?*")).isEqualTo("_______");
		assertThat(FileBasedComparisonUtils.validateAndFixFilename("\u0000\u0001\u0002\u0003\u0004\u0005" +
			"\u0006\u0007\u0008\u0009\u000b\u000c\n\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017" +
			"\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f")).isEqualTo("_______________________________");
	}

}
