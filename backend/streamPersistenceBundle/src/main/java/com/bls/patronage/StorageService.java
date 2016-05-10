package com.bls.patronage;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

public interface StorageService {
    Path persistStream(InputStream stream, Path location) throws StorageException;

    void deleteFile(Path location) throws StorageException;

    File getFile(Path filePath);
}
