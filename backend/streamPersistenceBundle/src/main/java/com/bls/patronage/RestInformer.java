package com.bls.patronage;

import javax.ws.rs.core.Response;

class RestInformer implements HTTPListenerInformer {
    @Override
    //TODO wait until CV returns collections of flashcards as response
    public Response inform(Listener listener, Object content) {
//        return JerseyClientBuilder
//                .newClient()
//                .target(listener.getUri())
//                .request()
//                .buildPost(Entity.json(content))
//                .invoke();
        return Response.ok().build();
    }
}
