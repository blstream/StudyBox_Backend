package com.bls.patronage;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public abstract class StreamPersistenceBundle<E extends Configuration> implements ConfiguredBundle<E> {
    StreamPersistenceService streamService;
    HTTPListenerInformer listenerInformer;
    private Logger logger;

    @Override
    public void run(E configuration, Environment environment) throws Exception {
        streamService = LocalFileService.getInstance();
        listenerInformer = new RestInformer(getListenerURI(configuration));
        logger = LoggerFactory.getLogger(StreamPersistenceBundle.class);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    abstract public URI getListenerURI(E configuration);

    public Path persistStream(InputStream stream, Path location) throws IOException, URISyntaxException {
        return streamService.persistStream(stream, location);
    }

    public Response informListener(Message message) {
        return listenerInformer.inform(message);
    }

    public void deleteStream(Path location) throws IOException {
        streamService.deleteStream(location);
    }
}