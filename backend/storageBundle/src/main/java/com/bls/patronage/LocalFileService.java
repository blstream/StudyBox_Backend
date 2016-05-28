package com.bls.patronage;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public UUID create(final InputStream stream, final StorageContexts contexts, final UUID userId) throws StorageException {
        final Path location = Paths.get(STORAGE_PATH.toString(), userId.toString(), contexts.toString());
        final UUID dataId = UUID.randomUUID();

        try {
            createPath(location);
            final Path filePath = location.resolve(dataId.toString());
            Files.copy(stream, filePath);
        } catch (IOException e) {
            throw new StorageException(e);
        }

        return dataId;
    }

    public void createPath(final Path location) throws StorageException {
        if (!Files.exists(location)) {
            try {
                Files.createDirectories(location);
            } catch (IOException e) {
                throw new StorageException(e);
            }
        }
    }

    @Override
    public void delete(final UUID userId, final StorageContexts context, final UUID dataId) throws StorageException {
        try {
            Files.delete(FilePathsCoder.resolvePathToFile(STORAGE_PATH, userId, context, dataId));
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public InputStream get(final UUID userId, final StorageContexts context, final UUID dataId) throws StorageException {
        try {
            final Path path = FilePathsCoder.resolvePathToFile(STORAGE_PATH, userId, context, dataId);
            return new BufferedInputStream(new FileInputStream(path.toFile()));
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }
}
