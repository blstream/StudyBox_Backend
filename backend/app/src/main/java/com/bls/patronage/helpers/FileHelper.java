package com.bls.patronage.helpers;

import com.bls.patronage.StreamPersistenceBundle;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;

public class FileHelper {
    private final StreamPersistenceBundle bundle;

    public FileHelper(StreamPersistenceBundle bundle) {

        this.bundle = bundle;
    }

    public Path handleInputStream(InputStream stream, Path location) throws Exception {
        return bundle.persistStream(stream, location);
    }

    public Response informService(URI location) {
        return bundle.informService(new CVRequest(location, "ImageToFlashcard"));
    }

    public void cleanUp(Path location) throws Exception {
        bundle.deleteStream(location);
    }

    public File getFile(Path filePath) {
        return bundle.getFile(filePath);
    }
}
