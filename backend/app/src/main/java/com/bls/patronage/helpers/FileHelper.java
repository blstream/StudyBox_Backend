package com.bls.patronage.helpers;

import com.bls.patronage.StreamPersistenceBundle;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

public class FileHelper {
    private final StreamPersistenceBundle bundle;

    public FileHelper(StreamPersistenceBundle bundle) {

        this.bundle = bundle;
    }

    public Response handleInputStream(InputStream stream, Path location) throws Exception {
        final URL streamedFileLocation = bundle.persistStream(stream, location.toUri().toURL());
        return bundle.informListener(new CVMessage(streamedFileLocation, "ImageToFlashcard"));
    }

    public void cleanUp(Path location) throws Exception {
        bundle.deleteStream(location.toUri().toURL());
    }
}
