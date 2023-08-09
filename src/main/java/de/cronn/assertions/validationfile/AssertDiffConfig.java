package de.cronn.assertions.validationfile;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;
import de.cronn.assertions.validationfile.serialization.JsonSerializer;
import de.cronn.assertions.validationfile.serialization.ObjectSerializer;

public class AssertDiffConfig {

	private String fileExtension;
	private String suffix = "";
	private ValidationNormalizer validationNormalizer = null;
	private ObjectSerializer objectSerializer = new JsonSerializer();

	public AssertDiffConfig fileExtension(FileExtension fileExtension) {
		this.fileExtension = fileExtension.asString();
		return this;
	}

	public AssertDiffConfig fileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
		return this;
	}

	public AssertDiffConfig suffix(String suffix){
		this.suffix = suffix;
		return this;
	}

	public AssertDiffConfig validationNormalizer(ValidationNormalizer validationNormalizer) {
		this.validationNormalizer = validationNormalizer;
		return this;
	}

	public AssertDiffConfig objectSerializer(ObjectSerializer objectSerializer) {
		this.objectSerializer = objectSerializer;
		return this;
	}

	public void assertWithSnapshot(Object object) {
		AssertDiff.assertWithSnapshot(objectSerializer.serialize(object), suffix, fileExtension != null ? fileExtension : objectSerializer.getFileExtension(), validationNormalizer);
	}

	public void assertWithSnapshot(String actualString) {
		AssertDiff.assertWithSnapshot(actualString, suffix, fileExtension != null ? fileExtension : FileExtensions.TXT.asString(), validationNormalizer);
	}
}
