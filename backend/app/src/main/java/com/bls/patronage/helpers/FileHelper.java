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

    public Path handleInputStream(InputStream stream, Path location) throws Exception {
        return bundle.persistStream(stream, location);
    }

    public Response informListener(URL location) {
        return bundle.informListener(new CVMessage(location, "ImageToFlashcard"));
    }

    public void cleanUp(Path location) throws Exception {
        bundle.deleteStream(location);
    }
}
