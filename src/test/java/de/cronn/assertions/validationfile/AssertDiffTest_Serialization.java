package de.cronn.assertions.validationfile;

import de.cronn.assertions.validationfile.sample.DummySerializer;
import de.cronn.assertions.validationfile.sample.SampleStructure;

import de.cronn.assertions.validationfile.serialization.ObjectSerializer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AssertDiff.class)
public class AssertDiffTest_Serialization {

	@Test
	void byDefaultSerializeToJsonString() {
		AssertDiff.assertWithSnapshot(SampleStructure.filledWithConstantValues());
	}

	@Test
	void customXmlSerializer() {
		AssertDiff.objectSerializer(new XmlSerializer()).assertWithSnapshot(SampleStructure.filledWithConstantValues());
	}

	private class XmlSerializer implements ObjectSerializer {

		@Override
		public String serialize(Object object) {
			return DummySerializer.toXmlString(object);
		}

		@Override
		public String getFileExtension() {
			return FileExtensions.XML.toString();
		}
	}

}
