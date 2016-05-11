package com.bls.patronage;

import java.io.IOException;

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
}
