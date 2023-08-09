package de.cronn.assertions.validationfile;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

import static de.cronn.assertions.validationfile.AssertDiff.*;

public class AssertDiffConfiguration {

	private String fileExtension = FileExtensions.TXT.asString();
	private String fileNameSuffix = null;
	private ValidationNormalizer normalizer = null;
	private ObjectMapper objectMapper = AssertDiff.DEFAULT_OBJECT_MAPPER;

	public String getFileExtension() {
		return fileExtension;
	}

	public AssertDiffConfiguration fileExtension(FileExtension fileExtension) {
		this.fileExtension = fileExtension.asString();
		return this;
	}

	public AssertDiffConfiguration fileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
		return this;
	}

	public String getFileNameSuffix() {
		return fileNameSuffix;
	}

	public AssertDiffConfiguration fileNameSuffix(String suffix) {
		this.fileNameSuffix = suffix;
		return this;
	}

	public ValidationNormalizer getNormalizer() {
		return normalizer;
	}

	public AssertDiffConfiguration normalizer(ValidationNormalizer normalizer) {
		this.normalizer = normalizer;
		return this;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public AssertDiffConfiguration objectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		return this;
	}

}
