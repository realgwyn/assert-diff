package de.cronn.assertions.validationfile.serialization;

import de.cronn.assertions.validationfile.FileExtensions;

public class CsvObjectMapper implements ObjectSerializer {
	@Override
	public String serialize(Object object) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public String getFileExtension() {
		return FileExtensions.CSV.asString();
	}
}
