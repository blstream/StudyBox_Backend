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
import java.net.URL;

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

    public URL persistStream(InputStream stream, URL location) throws IOException, URISyntaxException {
        logger.debug("in persistStream() arguments passed to streamService");
        logger.debug("Stream: " + stream + " Location: " + location);
        return streamService.persistStream(stream, location);
    }

    public Response informListener(Message message) {
        logger.debug("in informListener() argument passed to listenerInformer");
        logger.debug("Message: " + message);
        Response inform = listenerInformer.inform(message);
        logger.debug("Listener informed");
        return inform;
    }

    public void deleteStream(URL location) throws IOException {
        logger.debug("in deleteStream()");
        logger.debug("Location: " + location);
        streamService.deleteStream(location);
        logger.debug("Location deleted");
    }
}