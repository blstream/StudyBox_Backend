package com.bls.patronage;

import java.io.IOException;
import java.util.HashMap;

/**
 * This exception is thrown within streamPersistenceBundle.
 * It's used to separate errors thrown in this bundle from other exceptions
 */
public class StorageException extends IOException {
    public StorageException() {
    }

    public StorageException(Throwable cause) {
        super(cause);
    }

    public HashMap<String, String> getJSONMessage() {
        return new HashMap<String, String>() {
            {
                put("code", "502");
                put("message", "Storage exception: " + getCause() != null ? getCause().getMessage() : " No cause provided");
            }
        };
    }
}
