package com.bls.patronage;


import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.util.Map;

/**
 * This exception is thrown within streamPersistenceBundle.
 * It's used to separate errors thrown in this bundle from other exceptions
 */
public class StorageException extends IOException {
    public StorageException(Throwable cause) {
        super(cause);
    }

    public Map<String, String> getJSONMessage() {
        return ImmutableMap.<String, String>builder()
                .put("code", "502")
                .put("message", "Storage exception: " + getCause() != null ? getCause().getMessage() : " No cause provided")
                .build();
    }
}
