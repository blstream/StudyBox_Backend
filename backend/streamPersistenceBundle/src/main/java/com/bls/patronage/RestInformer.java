package com.bls.patronage;

import javax.ws.rs.core.Response;
import java.net.URI;

class RestInformer implements HTTPListenerInformer {
    private final URI uri;

    public RestInformer(URI uri) {
        this.uri = uri;
    }

    @Override
    //TODO wait until CV returns collections of flashcards as response
    public Response inform(Message message) {
//        return JerseyClientBuilder
//                .newClient()
//                .target(uri)
//                .request()
//                .buildPost(Entity.json(message))
//                .invoke();
        return Response.ok().build();
    }
}
