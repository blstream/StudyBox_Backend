package com.bls.patronage;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public interface StorageService {
    default URL createPublicURL(final HttpServletRequest request, final Class<?> storageResourceClass, final UUID userId, final StorageContexts context, final UUID dataId) throws MalformedURLException {
        final URL baseURL = new URL("http://" + request.getServerName() + ":" + request.getServerPort());
        return this.createPublicURL(baseURL, storageResourceClass, userId, context, dataId);
    }

    default URL createPublicURL(final URL baseURL, final Class<?> storageResourceClass, final UUID userId, final StorageContexts context, final UUID dataId) throws MalformedURLException {
        return FilePathsCoder.resolveURIToFile(baseURL, storageResourceClass, userId, context, dataId);
    }

    UUID create(final InputStream stream, final StorageContexts contexts, final UUID userId) throws StorageException;

    void delete(final UUID userId, final StorageContexts context, final UUID dataId) throws StorageException;

    InputStream get(final UUID userId, final StorageContexts context, final UUID dataId) throws StorageException;
}
