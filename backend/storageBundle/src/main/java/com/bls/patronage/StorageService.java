package com.bls.patronage;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.UUID;

public interface StorageService {

    UUID create(InputStream stream, UUID userId) throws StorageException;

    default URI createPublicURL(final Class<?> storageResourceClass, UUID dataId, UUID userId) {
        return FilePathsCoder.resolveURIToFile(storageResourceClass, dataId, userId);
    }

    ;

    void delete(UUID dataId, UUID userId) throws StorageException;

    OutputStream get(UUID dataId, UUID userId) throws StorageException;
}
