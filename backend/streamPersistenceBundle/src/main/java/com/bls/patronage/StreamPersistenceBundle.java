package com.bls.patronage;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.client.JerseyClientBuilder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.UUID;

public abstract class StreamPersistenceBundle<E extends Configuration> implements ConfiguredBundle<E> {
    private StorageService streamService;
    private URI serviceURI;

    @Override
    public void run(E configuration, Environment environment) throws Exception {
        streamService = new LocalFileService(getStoragePath(configuration));
        serviceURI = getServiceURI(configuration);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    abstract public URI getServiceURI(E configuration);

    abstract public Path getStoragePath(E configuration);

    public UUID persistStream(InputStream stream, UUID userId) throws StorageException {
        return streamService.create(stream, userId);
    }

    public Response informService(Object message) {
        return JerseyClientBuilder
                .createClient()
                .target(serviceURI)
                .request()
                .buildPost(Entity.json(message))
                .invoke();
    }

    public void deleteFile(UUID dataId, UUID userId) throws StorageException {
        streamService.delete(dataId, userId);
    }

    public OutputStream getFile(UUID dataId, UUID userId) throws StorageException {
        return streamService.get(dataId, userId);
    }
}
