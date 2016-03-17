package com.bls.patronage.db.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;

@Provider
public class DataAccessExceptionMapper
        implements ExceptionMapper<DataAccessException> {

    public DataAccessExceptionMapper() {
    }

    @Override
    public Response toResponse(DataAccessException dataAccessException) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new HashMap<String, String>() {
                    {
                        put("code", "400");
                        put("message", dataAccessException.getMessage());
                    }
                }).build();

    }
}
