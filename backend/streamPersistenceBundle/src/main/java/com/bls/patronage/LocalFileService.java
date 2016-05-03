package com.bls.patronage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

class LocalFileService implements StreamPersistenceService {

    private static final LocalFileService LOCAL_FILE_SERVICE = new LocalFileService();
    private final Logger logger;


    private LocalFileService() {
        logger = LoggerFactory.getLogger(LocalFileService.class);
    }

    public static LocalFileService getInstance() {
        return LOCAL_FILE_SERVICE;
    }

    @Override
    public Path persistStream(InputStream stream, Path location) throws IOException, URISyntaxException {
        Path path = createPathToFile(location);

        Files.copy(stream, path);

        return path;
    }

    public Path createPathToFile(Path location) throws IOException {
        if (!Files.exists(location)) {
            Files.createDirectory(location);
        }

        return location.resolve(UUID.randomUUID().toString());
    }

    @Override
    public void deleteStream(Path location) throws IOException {
        Files.delete(location);
    }
}
