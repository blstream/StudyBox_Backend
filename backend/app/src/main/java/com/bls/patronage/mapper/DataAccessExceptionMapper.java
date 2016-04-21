package com.bls.patronage.mapper;

import com.bls.patronage.db.exception.DataAccessException;

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
        return Response.status(Response.Status.fromStatusCode(dataAccessException.getStatus()))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new HashMap<String, String>() {
                    {
                        put("code", dataAccessException.getStatus().toString());
                        put("message", dataAccessException.getMessage());
                    }
                }).build();

    }
}
