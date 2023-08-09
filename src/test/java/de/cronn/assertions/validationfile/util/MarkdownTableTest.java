package de.cronn.assertions.validationfile.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import de.cronn.assertions.validationfile.AssertDiff;

import org.junit.jupiter.api.Test;

import de.cronn.assertions.validationfile.FileExtensions;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AssertDiff.class)
class MarkdownTableTest {

	@Test
	void shouldBuildMarkdownInManyWays() {
		List<String> header = Arrays.asList("Title", "First Name", "Surname", "Company");
		MarkdownTable markdownTable = new MarkdownTable(header);

		markdownTable.addRow("Mr", "George", "Dawson", "Countrywide Powdered Coatings Ltd");
		markdownTable.addRow(Arrays.asList("Mrs", "Stacy", "Felderman", "On Time Workwear Ltd"));

		markdownTable.addCells("Mr", "Scott", "Zuckerman");
		markdownTable.addCell("Zuckerman Security Ltd");
		markdownTable.nextRow();

		markdownTable.addCells(Arrays.asList("Mrs", "Isabelle", "Hewitt", "Craft Textiles Ltd"));
		markdownTable.nextRow();

		markdownTable.addRow("Mr", "Albert", "Barker", "Barker Insulation Services Ltd");

		AssertDiff.fileExtension(FileExtensions.MD).assertWithSnapshot(markdownTable.toString());
	}

	@Test
	void shouldPadCells() {
		MarkdownTable markdownTable = new MarkdownTable();
		markdownTable.addRow("Company");
		markdownTable.addRow("On Time Workwear Ltd");
		markdownTable.addRow("Countrywide Powdered Coatings Ltd");
		markdownTable.addRow("Craft Textiles Ltd");

		assertThat(markdownTable.toString())
			.isEqualTo("| Company                           |\n" +
				"|-----------------------------------|\n" +
				"| On Time Workwear Ltd              |\n" +
				"| Countrywide Powdered Coatings Ltd |\n" +
				"| Craft Textiles Ltd                |");
	}

}
