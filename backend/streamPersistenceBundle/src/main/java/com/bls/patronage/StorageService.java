package com.bls.patronage;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.UUID;

public interface StorageService {
    Path persistStream(InputStream stream, UUID userId) throws StorageException;

    void deleteFile(Path location) throws StorageException;

    OutputStream getFile(Path filePath) throws StorageException;
}
