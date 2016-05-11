package com.bls.patronage.mapper;

import com.bls.patronage.StorageException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;

@Provider
public class StorageExceptionMapper
        implements ExceptionMapper<StorageException> {

    public StorageExceptionMapper() {
    }

    @Override
    public Response toResponse(StorageException storageException) {
        return Response.status(Response.Status.fromStatusCode(502))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new HashMap<String, String>() {
                    {
                        put("code", "502");
                        put("message", "Storage exception: " + storageException.getCause().getMessage());
                    }
                }).build();

    }
}
