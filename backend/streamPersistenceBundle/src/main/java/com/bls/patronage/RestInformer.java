package com.bls.patronage;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Informs an external REST service with specified message and returns it's response
 */

class RestInformer implements HTTPInformer {
    private final JerseyWebTarget client;

    public RestInformer(URI uri) {
        client = JerseyClientBuilder
                .createClient()
                .target(uri);
    }

    @Override
    public Response inform(final Object message) {
        return client
                .request()
                .buildPost(Entity.json(message))
                .invoke();
    }
}
