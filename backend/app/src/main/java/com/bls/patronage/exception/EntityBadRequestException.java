package com.bls.patronage.exception;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

public class EntityBadRequestException extends WebApplicationException {
    private static final long serialVersionUID = 1L;

    public EntityBadRequestException() {
        super(Response.status(Response.Status.BAD_REQUEST).build());
    }

    public EntityBadRequestException(String message) {

        super(Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new HashMap<String, String>() {
                    {
                        put("code", "400");
                        put("message", message);
                    }
                }).build());

    }
}