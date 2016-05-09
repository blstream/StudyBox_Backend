package com.bls.patronage.helpers;

import com.bls.patronage.resources.FilesResource;

import javax.ws.rs.core.UriBuilder;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.util.UUID;

public class FilePathsCoder {
    public static Path decodeFilePath(Path baseLocation, final UUID userId, final UUID fileId) {
        return baseLocation.resolve(userId.toString()).resolve(fileId.toString());
    }

    public static URI encodeFilePath(final Path filePath) throws MalformedURLException {
        UUID fileId = UUID.fromString(filePath.getFileName().toString());
        UUID userId = UUID.fromString(filePath.getParent().getFileName().toString());
        URI uri = UriBuilder
                .fromResource(FilesResource.class).path(fileId.toString())
                .build(userId);
        return uri;
    }
}
