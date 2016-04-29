package com.bls.patronage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

class LocalFileService implements StreamPersistenceService {

    private static final LocalFileService LOCAL_FILE_SERVICE = new LocalFileService();
    private final Logger logger;


    private LocalFileService() {
        logger = LoggerFactory.getLogger(LocalFileService.class);
    }

    public static StreamPersistenceService getInstance() {
        return LOCAL_FILE_SERVICE;
    }

    @Override
    public URL persistStream(InputStream stream, URL location) throws IOException, URISyntaxException {
        logger.debug("Mapping location: " + location + " to directory");
        Path directory = Paths.get(location.getPath());
        logger.debug("Checking if the directory is present in file system, and if not, creating it");
        if (!Files.exists(directory)) Files.createDirectory(directory);
        logger.debug("In persistStream(), using Direcory: " + directory + " to create path to file");
        final Path path = directory.resolve(UUID.randomUUID().toString());
        logger.debug("Created path: " + path);
        logger.debug("Files.copy with path and stream: " + stream);
        Files.copy(stream, path);
        logger.debug("Returning path as a url");
        return path.toUri().toURL();
    }

    @Override
    public void deleteStream(URL location) throws IOException {
        logger.debug("In deleteStream(), using Location: " + location + " to delete file from location");
        Files.delete(Paths.get(location.getPath()));
        logger.debug("Delete completed");
    }
}
