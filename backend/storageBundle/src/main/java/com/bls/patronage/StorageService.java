package com.bls.patronage;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.UUID;

public interface StorageService {

    default URI createPublicURI(final Class<?> storageResourceClass, UUID dataId, StorageContexts context, UUID userId) {
        return FilePathsCoder.resolveURIToFile(storageResourceClass, userId, context, dataId);
    }

    UUID create(InputStream stream, StorageContexts contexts, UUID userId) throws StorageException;

    void delete(UUID userId, StorageContexts context, UUID dataId) throws StorageException;

    OutputStream get(UUID userId, StorageContexts context, UUID dataId) throws StorageException;
}
