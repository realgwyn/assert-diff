package de.cronn.assertions.validationfile.sample;

import de.cronn.assertions.validationfile.AssertDiff;
import de.cronn.assertions.validationfile.FileExtensions;
import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;
import de.cronn.assertions.validationfile.replacements.DateTimeReplacer;
import de.cronn.assertions.validationfile.replacements.Replacer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@ExtendWith(AssertDiff.class)
class ValidationFileSampleTest {

	@Test
	void simpleComparison() {
		String actualString = "this will be content of /data/test/validation/ValidationFileSample_simpleComparison.txt";
		AssertDiff.assertWithSnapshot(actualString);
	}

	@Test
	void differentFileSuffixes() {
		AssertDiff.suffix("example1").assertWithSnapshot("this will be content of ValidationFileSample_differentFileSuffixes_example1.txt");
		AssertDiff.suffix("example2").assertWithSnapshot("this will be content of ValidationFileSample_differentFileSuffixes_example2.txt");
	}

	@Test
	void differentFileFormats() {
		SampleStructure sampleStructure = SampleStructure.filledWithConstantValues();
		AssertDiff.fileExtension(FileExtensions.JSON).assertWithSnapshot(DummySerializer.toJsonString(sampleStructure));
		AssertDiff.fileExtension(FileExtensions.XML).assertWithSnapshot(DummySerializer.toXmlString(sampleStructure));
		AssertDiff.fileExtension(FileExtensions.CSV).assertWithSnapshot(DummySerializer.toCsvString(sampleStructure));
		AssertDiff.fileExtension("str.txt").assertWithSnapshot(sampleStructure.toString());
	}

	@Test
	void normalization_json() {
		SampleStructure sampleStructure = SampleStructure.filledWithChangingValues();

		AssertDiff
			.fileExtension(FileExtensions.JSON)
			.validationNormalizer(ValidationNormalizer.combine(
				Replacer.forJson().withKey("MessageId").build(),
				Replacer.forJson().withKey("TransactionId").withValue("\\d+").withReplacement("[transaction-id]").build(),
				Replacer.forJsonDateTime().withKey("Timestamp").withSourceFormat(ISO_DATE_TIME).withDestinationFormat(normalizedIsoLocalDate()).build()))
			.assertWithSnapshot(DummySerializer.toJsonString(sampleStructure));
	}

	@Test
	void normalization_xml() {
		SampleStructure sampleStructure = SampleStructure.filledWithChangingValues();

		AssertDiff
			.fileExtension(FileExtensions.XML)
			.validationNormalizer(ValidationNormalizer.combine(
				Replacer.forXml().withElementName("MessageId").build(),
				Replacer.forXml().withElementName("TransactionId").withContent("\\d+").withReplacement("[transaction-id]").build(),
				Replacer.forXmlDateTime().withElementName("Timestamp").withSourceFormat(ISO_DATE_TIME).withDestinationFormat(normalizedIsoLocalDate()).build()))
			.assertWithSnapshot(DummySerializer.toXmlString(sampleStructure));
	}

	@Test
	void normalization_custom() {
		SampleStructure sampleStructure = SampleStructure.filledWithChangingValues();

		AssertDiff
			.validationNormalizer(ValidationNormalizer.combine(
				new Replacer("(messageId)='[\\w-]{36}'", "$1=[masked]"),
				new Replacer("(transactionId)=\\d+", "$1=[transaction-id]"),
				new DateTimeReplacer(Pattern.compile("timestamp=(?<DateTime>[\\d-]+T[\\d:]+\\.\\d+)"), ISO_DATE_TIME, normalizedIsoLocalDate())))
			.assertWithSnapshot(sampleStructure.toString());
	}

	private DateTimeFormatter normalizedIsoLocalDate() {
		return new DateTimeFormatterBuilder()
			.append(ISO_LOCAL_DATE).appendLiteral("T").appendLiteral("[masked]").toFormatter();
	}

}
