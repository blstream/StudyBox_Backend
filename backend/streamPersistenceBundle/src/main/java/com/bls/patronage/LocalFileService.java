package com.bls.patronage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

class LocalFileService implements StreamPersistenceService {

    private static final LocalFileService LOCAL_FILE_SERVICE = new LocalFileService();

    private LocalFileService() {
    }

    public static StreamPersistenceService getInstance() {
        return LOCAL_FILE_SERVICE;
    }

    @Override
    public Path persistStream(InputStream stream, Path location) throws IOException {
        final Path path = location.resolve(UUID.randomUUID().toString());

        Files.copy(stream, path);

        return path;
    }
}
