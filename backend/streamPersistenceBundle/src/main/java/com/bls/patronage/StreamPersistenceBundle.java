package com.bls.patronage;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public abstract class StreamPersistenceBundle<E extends Configuration> implements ConfiguredBundle<E> {
    Listener listener;
    StreamPersistenceService streamService;
    HTTPListenerInformer listenerInformer;

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

    public URL persistStream(InputStream stream, URL location) throws IOException, URISyntaxException {

        return streamService.persistStream(stream, location);
    }

    public Response informListener(Message message) {
        return listenerInformer.inform(listener, message);
    }

    public void deleteStream(URL location) throws IOException {
        streamService.deleteStream(location);
    }
}