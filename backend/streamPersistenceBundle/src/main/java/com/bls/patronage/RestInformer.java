package com.bls.patronage;

import org.glassfish.jersey.client.JerseyClientBuilder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

class RestInformer implements HTTPListenerInformer {
    @Override
    public Response inform(Listener listener, Object content) {
        return JerseyClientBuilder
                .newClient()
                .target(listener.getUri())
                .request()
                .buildPost(Entity.json(content))
                .invoke();
    }
}
