package com.bls.patronage;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.UUID;

public interface StorageService {

    UUID create(InputStream stream, UUID userId) throws StorageException;

    default URI createPublicURL(final Class<?> storageResourceClass, UUID dataId, StorageContexts context, UUID userId) {
        return FilePathsCoder.resolveURIToFile(storageResourceClass, userId, context, dataId);
    }

    void delete(UUID userId, StorageContexts context, UUID dataId) throws StorageException;

    OutputStream get(UUID userId, StorageContexts context, UUID dataId) throws StorageException;
}
