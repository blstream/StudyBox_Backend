package com.bls.patronage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

class LocalFileService implements StorageService {

    private final Path STORAGE_PATH;

    public LocalFileService(Path storagePath) {
        STORAGE_PATH = storagePath;
    }

    @Override
    public Path persistStream(InputStream stream, UUID userId) throws StorageException {
        Path path = Paths.get(STORAGE_PATH.toString(), userId.toString());
        try {
            path = createPathToFile(path);
            Files.copy(stream, path);
        } catch (IOException e) {
            throw new StorageException(e);
        }

        return path;
    }

    public Path createPathToFile(Path location) throws StorageException {
        if (!Files.exists(location)) {
            try {
                Files.createDirectory(location);
            } catch (IOException e) {
                throw new StorageException(e);
            }
        }

        return location.resolve(UUID.randomUUID().toString());
    }

    @Override
    public void deleteFile(Path location) throws StorageException {
        try {
            Files.delete(location);
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public OutputStream getFile(Path filePath) throws StorageException {
        try {
            return new FileOutputStream(filePath.toFile());
        } catch (FileNotFoundException e) {
            throw new StorageException(e);
        }
    }
}
