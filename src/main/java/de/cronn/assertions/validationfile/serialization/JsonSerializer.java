package de.cronn.assertions.validationfile.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import de.cronn.assertions.validationfile.FileExtensions;

public class JsonSerializer implements ObjectSerializer {

	private static final ObjectMapper DEFAULT_OBJECT_MAPPER = JsonMapper.builder()
		.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
		.configure(SerializationFeature.INDENT_OUTPUT, true)
		.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
		.findAndAddModules()
		.build()
		.setSerializationInclusion(JsonInclude.Include.NON_NULL);

	private ObjectMapper objectMapper = DEFAULT_OBJECT_MAPPER;
	private String fileExtension = FileExtensions.JSON.asString();

	public JsonSerializer setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		return this;
	}

	@Override
	public String serialize(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getFileExtension() {
		return fileExtension;
	}
}
