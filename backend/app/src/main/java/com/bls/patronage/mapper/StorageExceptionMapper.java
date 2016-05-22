package com.bls.patronage.mapper;

import com.bls.patronage.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StorageExceptionMapper implements ExceptionMapper<StorageException> {

    public StorageExceptionMapper() {
    }

    @Override
    public Response toResponse(StorageException storageException) {
        final Logger LOG = LoggerFactory.getLogger(StorageExceptionMapper.class);
        LOG.debug("Storage exception: ", storageException);
        return Response.status(Response.Status.fromStatusCode(502))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(storageException.getJSONMessage()).build();
    }
}
