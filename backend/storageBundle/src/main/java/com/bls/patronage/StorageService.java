package com.bls.patronage;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public interface StorageService {
    UUID create(InputStream stream, UUID userId) throws StorageException;

    void delete(UUID dataId, UUID userId) throws StorageException;

    OutputStream get(UUID dataId, UUID userId) throws StorageException;
}
