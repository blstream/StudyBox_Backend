package com.bls.patronage;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.nio.file.Path;
import java.util.UUID;

class FilePathsCoder {

    public static Path resolvePathToFile(final Path storagePath, final UUID userId, final StorageContexts context, final UUID fileId) {
        return storagePath.resolve(userId.toString()).resolve(context.getContext()).resolve(fileId.toString());
    }

    public static URI resolveURIToFile(final Class resourceClass, final UUID userId, final StorageContexts context, final UUID fileId) {
        String uri = UriBuilder
                        .fromResource(resourceClass)
                .build(userId, context, fileId)
                .toString();

        return URI.create(uri);
    }
}
