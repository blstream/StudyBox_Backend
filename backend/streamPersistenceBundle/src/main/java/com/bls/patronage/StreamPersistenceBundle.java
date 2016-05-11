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

public abstract class StreamPersistenceBundle<E extends Configuration> implements ConfiguredBundle<E> {
    private StorageService streamService;
    private URI serviceURI;

    @Override
    public void run(E configuration, Environment environment) throws Exception {
        streamService = LocalFileService.getInstance();
        serviceURI = getServiceURI(configuration);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    abstract public URI getServiceURI(E configuration);

    public Path persistStream(InputStream stream, Path location) throws StorageException {
        return streamService.persistStream(stream, location);
    }

    public Response informService(Object message) {
        return JerseyClientBuilder
                .createClient()
                .target(serviceURI)
                .request()
                .buildPost(Entity.json(message))
                .invoke();
    }

    public void deleteFile(Path location) throws StorageException {
        streamService.deleteFile(location);
    }

    public OutputStream getFile(Path filePath) throws StorageException {
        return streamService.getFile(filePath);
    }
}
