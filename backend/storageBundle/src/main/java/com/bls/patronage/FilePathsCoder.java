package com.bls.patronage;

import javax.ws.rs.core.UriBuilder;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.UUID;

class FilePathsCoder {

    public static Path resolvePathToFile(final Path storagePath, final UUID userId, final StorageContexts context, final UUID fileId) {
        return storagePath.resolve(userId.toString()).resolve(context.toString()).resolve(fileId.toString());
    }

    public static URL resolveURIToFile(final URL baseURL, final Class resourceClass, final UUID userId, final StorageContexts context, final UUID fileId) throws MalformedURLException {
        final String url = baseURL +
                UriBuilder
                        .fromResource(resourceClass)
                .build(userId, context, fileId)
                .toString();

        return new URL(url);
    }
}
