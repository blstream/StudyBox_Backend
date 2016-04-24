package com.bls.patronage;

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

    private LocalFileService() {
    }

    public static StreamPersistenceService getInstance() {
        return LOCAL_FILE_SERVICE;
    }

    @Override
    public URL persistStream(InputStream stream, URL location) throws IOException, URISyntaxException {
        final Path path = Paths.get(location.getPath()).resolve(UUID.randomUUID().toString());

        Files.copy(stream, path);

        return path.toUri().toURL();
    }

    @Override
    public void deleteStream(URL location) throws IOException {
        Files.delete(Paths.get(location.toString()));
    }
}
