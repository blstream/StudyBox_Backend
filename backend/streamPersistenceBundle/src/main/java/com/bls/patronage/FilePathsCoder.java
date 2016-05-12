package com.bls.patronage;

import javax.ws.rs.core.UriBuilder;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.util.UUID;

public class FilePathsCoder {
    public static Path resolvePathToFile(final Path storagePath, final UUID userId, final UUID fileId) {
        return storagePath.resolve(userId.toString()).resolve(fileId.toString());
    }

    public static URI resolveURIToFile(final Class resourceClass, final UUID userId, final UUID fileId) {
        String uri = new StringBuilder(
                UriBuilder
                        .fromResource(resourceClass)
                        .build(userId)
                        .toString())
                .append("/")
                .append(fileId)
                .toString();

        return URI.create(uri);
    }

    public static URI resolveURIToFile(final Class resourceClass, final Path filePath) throws MalformedURLException {
        UUID fileId = UUID.fromString(filePath.getFileName().toString());
        UUID userId = UUID.fromString(filePath.getParent().getFileName().toString());

        String uri = new StringBuilder(
                UriBuilder
                        .fromResource(resourceClass)
                        .build(userId)
                        .toString())
                .append("/")
                .append(fileId)
                .toString();

        return URI.create(uri);
    }
}
