package de.cronn.assertions.validationfile.sample;

import static java.time.format.DateTimeFormatter.*;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.regex.Pattern;

import de.cronn.assertions.validationfile.AssertDiff;

import org.junit.jupiter.api.Test;

import de.cronn.assertions.validationfile.FileExtensions;
import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;
import de.cronn.assertions.validationfile.replacements.DateTimeReplacer;
import de.cronn.assertions.validationfile.replacements.Replacer;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AssertDiff.class)
class ValidationFileSampleTest {

	@Test
	void simpleComparison() {
		String actualString = "this will be content of /data/test/validation/ValidationFileSample_simpleComparison.txt";
		AssertDiff.assertWithSnapshot(actualString);
	}

	@Test
	void differentFileSuffixes() {
		AssertDiff.assertWithSnapshotWithSuffix("this will be content of ValidationFileSample_differentFileSuffixes_example1.txt", "example1");
		AssertDiff.assertWithSnapshotWithSuffix("this will be content of ValidationFileSample_differentFileSuffixes_example2.txt", "example2");
	}

	@Test
	void differentFileFormats() {
		SampleStructure sampleStructure = SampleStructure.filledWithConstantValues();
		AssertDiff.assertWithSnapshotJson(DummySerializer.toJsonString(sampleStructure));
		AssertDiff.assertWithSnapshotXml(DummySerializer.toXmlString(sampleStructure));
		AssertDiff.assertWithSnapshot(DummySerializer.toCsvString(sampleStructure), FileExtensions.CSV);
		AssertDiff.assertWithSnapshot(sampleStructure.toString(), () -> "str.txt");
	}

	@Test
	void normalization_json() {
		SampleStructure sampleStructure = SampleStructure.filledWithRandomValues();

		AssertDiff.assertWithSnapshotJson(DummySerializer.toJsonString(sampleStructure), ValidationNormalizer.combine(
			Replacer.forJson().withKey("MessageId").build(),
			Replacer.forJson().withKey("TransactionId").withValue("\\d+").withReplacement("[transaction-id]").build(),
			Replacer.forJsonDateTime().withKey("Timestamp").withSourceFormat(ISO_DATE_TIME).withDestinationFormat(normalizedIsoLocalDate()).build()
		));
	}

	@Test
	void normalization_xml()  {
		SampleStructure sampleStructure = SampleStructure.filledWithRandomValues();

		AssertDiff.assertWithSnapshotXml(DummySerializer.toXmlString(sampleStructure), ValidationNormalizer.combine(
			Replacer.forXml().withElementName("MessageId").build(),
			Replacer.forXml().withElementName("TransactionId").withContent("\\d+").withReplacement("[transaction-id]").build(),
			Replacer.forXmlDateTime().withElementName("Timestamp").withSourceFormat(ISO_DATE_TIME).withDestinationFormat(normalizedIsoLocalDate()).build()
		));
	}

	@Test
	void normalization_custom() {
		SampleStructure sampleStructure = SampleStructure.filledWithRandomValues();

		AssertDiff.assertWithSnapshot(sampleStructure.toString(), ValidationNormalizer.combine(
			new Replacer("(messageId)='[\\w-]{36}'", "$1=[masked]"),
			new Replacer("(transactionId)=\\d+", "$1=[transaction-id]"),
			new DateTimeReplacer(Pattern.compile("timestamp=(?<DateTime>[\\d-]+T[\\d:]+\\.\\d+)"), ISO_DATE_TIME, normalizedIsoLocalDate())
		));
	}

	private DateTimeFormatter normalizedIsoLocalDate() {
		return new DateTimeFormatterBuilder()
			.append(ISO_LOCAL_DATE).appendLiteral("T").appendLiteral("[masked]").toFormatter();
	}

}
