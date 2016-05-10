package com.bls.patronage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

class LocalFileService implements StorageService {

    private static final LocalFileService LOCAL_FILE_SERVICE = new LocalFileService();
    private final Logger logger;


    private LocalFileService() {
        logger = LoggerFactory.getLogger(LocalFileService.class);
    }

    public static LocalFileService getInstance() {
        return LOCAL_FILE_SERVICE;
    }

    @Override
    public Path persistStream(InputStream stream, Path location) throws StorageException {
        Path path = null;
        try {
            path = createPathToFile(location);

            Files.copy(stream, path);
        } catch (IOException e) {
            throw (StorageException) new StorageException().initCause(e);
        }

        return path;
    }

    public Path createPathToFile(Path location) throws StorageException {
        if (!Files.exists(location)) {
            try {
                Files.createDirectory(location);
            } catch (IOException e) {
                throw (StorageException) new StorageException().initCause(e);
            }
        }

        return location.resolve(UUID.randomUUID().toString());
    }

    @Override
    public void deleteFile(Path location) throws StorageException {
        try {
            Files.delete(location);
        } catch (IOException e) {
            throw (StorageException) new StorageException().initCause(e);
        }
    }

    @Override
    public File getFile(Path filePath) {
        return filePath.toFile();
    }
}
