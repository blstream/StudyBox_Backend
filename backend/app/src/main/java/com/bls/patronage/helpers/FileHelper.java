package com.bls.patronage.helpers;

import com.bls.patronage.StorageException;
import com.bls.patronage.StreamPersistenceBundle;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Path;

public class FileHelper {
    private final StreamPersistenceBundle bundle;

    public FileHelper(StreamPersistenceBundle bundle) {

        this.bundle = bundle;
    }

    public Path handleInputStream(InputStream stream, Path location) throws StorageException {
        return bundle.persistStream(stream, location);
    }

    public Response informService(URI location) {
        return bundle.informService(new CVRequest(location, "ImageToFlashcard"));
    }

    public void cleanUp(Path location) throws StorageException {
        bundle.deleteFile(location);
    }

    public OutputStream getFile(Path filePath) throws StorageException {
        return bundle.getFile(filePath);
    }
}
