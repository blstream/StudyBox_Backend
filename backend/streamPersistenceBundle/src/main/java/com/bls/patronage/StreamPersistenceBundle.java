package com.bls.patronage;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;

public abstract class StreamPersistenceBundle<E extends Configuration> implements ConfiguredBundle<E> {
    StorageService streamService;
    HTTPInformer listenerInformer;

    @Override
    public void run(E configuration, Environment environment) throws Exception {
        streamService = LocalFileService.getInstance();
        listenerInformer = new RestInformer(getServiceURI(configuration));
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    abstract public URI getServiceURI(E configuration);

    public Path persistStream(InputStream stream, Path location) throws StorageException {
        return streamService.persistStream(stream, location);
    }

    public Response informService(Message message) {
        return listenerInformer.inform(message);
    }

    public void deleteFile(Path location) throws StorageException {
        streamService.deleteFile(location);
    }

    public File getFile(Path filePath) {
        return streamService.getFile(filePath);
    }
}