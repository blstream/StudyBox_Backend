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
    public UUID create(InputStream stream, UUID userId) throws StorageException {
        Path path = Paths.get(STORAGE_PATH.toString(), userId.toString());
        UUID dataId = UUID.randomUUID();
        try {
            createPathToFile(path);
            path.resolve(dataId.toString());
            Files.copy(stream, path);
        } catch (IOException e) {
            throw new StorageException(e);
        }

        return dataId;
    }

    public void createPathToFile(Path location) throws StorageException {
        if (!Files.exists(location)) {
            try {
                Files.createDirectory(location);
            } catch (IOException e) {
                throw new StorageException(e);
            }
        }
    }

    @Override
    public void delete(UUID dataId, UUID userId) throws StorageException {
        try {
            Files.delete(FilePathsCoder.resolvePathToFile(STORAGE_PATH, dataId, userId));
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public OutputStream get(UUID dataId, UUID userId) throws StorageException {
        try {
            return new FileOutputStream(FilePathsCoder.resolvePathToFile(STORAGE_PATH, dataId, userId).toFile());
        } catch (FileNotFoundException e) {
            throw new StorageException(e);
        }
    }
}
