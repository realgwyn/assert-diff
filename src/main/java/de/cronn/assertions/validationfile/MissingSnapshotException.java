package de.cronn.assertions.validationfile;

import org.opentest4j.AssertionFailedError;

public class MissingSnapshotException extends RuntimeException {

	public MissingSnapshotException(String message){
		super(message);
	}
}
