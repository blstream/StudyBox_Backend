package com.bls.patronage;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public interface StorageService {
    default URL createPublicURL(HttpServletRequest request, final Class<?> storageResourceClass, UUID userId, StorageContexts context, UUID dataId) throws MalformedURLException {
        URL baseURL = new URL("http://" + request.getServerName() + ":" + request.getServerPort());
        return this.createPublicURL(baseURL, storageResourceClass, userId, context, dataId);
    }

    default URL createPublicURL(URL baseURL, final Class<?> storageResourceClass, UUID userId, StorageContexts context, UUID dataId) throws MalformedURLException {
        return FilePathsCoder.resolveURIToFile(baseURL, storageResourceClass, userId, context, dataId);
    }

    UUID create(InputStream stream, StorageContexts contexts, UUID userId) throws StorageException;

    void delete(UUID userId, StorageContexts context, UUID dataId) throws StorageException;

    byte[] get(UUID userId, StorageContexts context, UUID dataId) throws StorageException;
}
