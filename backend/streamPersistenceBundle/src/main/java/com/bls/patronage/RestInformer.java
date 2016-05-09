package com.bls.patronage;

import org.glassfish.jersey.client.JerseyClientBuilder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.net.URI;

class RestInformer implements HTTPInformer {
    private final URI uri;

    public RestInformer(URI uri) {
        this.uri = uri;
    }

    @Override
    public Response inform(Message message) {
        return JerseyClientBuilder
                .newClient()
                .target(uri)
                .request()
                .buildPost(Entity.json(message))
                .invoke();
    }
}
