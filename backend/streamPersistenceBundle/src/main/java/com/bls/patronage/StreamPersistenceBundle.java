package com.bls.patronage;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public abstract class StreamPersistenceBundle<E extends Configuration> implements ConfiguredBundle<E> {
    Listener listener;
    StreamPersistenceService streamService;
    HTTPListenerInformer listenerInformer;
    String input;
    Path location;

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

    public Response persistStreamAsFile(InputStream stream, Path location, Message message) throws IOException {

        streamService.persistStream(stream, location);

        return listenerInformer.inform(listener, message);
    }

}