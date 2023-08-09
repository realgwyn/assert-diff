package de.cronn.assertions.validationfile.serialization;

public interface ObjectSerializer {

	 String serialize(Object object);

	 String getFileExtension();

}
