package com.bls.patronage;

import org.glassfish.jersey.client.JerseyClientBuilder;

import javax.ws.rs.client.Entity;

class RestInformer implements ListenerInformer {
    @Override
    public void inform(Listener listener, Object content) {
        JerseyClientBuilder
                .newClient()
                .target(listener.getUri())
                .request()
                .buildPost(Entity.json(content))
                .invoke();
    }
}
