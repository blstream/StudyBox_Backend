package com.bls.patronage;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public abstract class FilePersistenceBundle<E extends Configuration> implements ConfiguredBundle<E> {
    Listener listener;
    StreamPersistenceService streamService;
    ListenerInformer listenerInformer;

    @Override
    public void run(E configuration, Environment environment) throws Exception {
        listener = getListener(configuration);
        streamService = LocalFileService.getInstance();
        listenerInformer = new RestInformer();
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    abstract public Listener getListener(E configuration);

    public Path persistStreamAsFile(InputStream stream, Path location) throws IOException {
        final Path path = streamService.persistStream(stream, location);

        listenerInformer.inform(listener, path);

        return path;
    }

}