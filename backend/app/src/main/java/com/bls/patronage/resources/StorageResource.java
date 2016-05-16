package com.bls.patronage.resources;

import com.bls.patronage.StorageContexts;
import com.bls.patronage.StorageException;
import com.bls.patronage.StorageService;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.UUID;

@Path("/storage/{userId}/{context}/{storageId}")
@Produces(MediaType.APPLICATION_JSON)
public class StorageResource {

    private final StorageService storageService;

    public StorageResource(StorageService storageService) {
        this.storageService = storageService;
    }

    @GET
    public StreamingOutput getFile(@Auth User user,
                                   @PathParam("userId") UUID userId,
                                   @PathParam("context") StorageContexts context,
                                   @PathParam("storageId") UUID storageId) throws StorageException {

        assert user.getId().equals(userId);

        return os -> {
            final Writer writer = new BufferedWriter(
                    new OutputStreamWriter(
                            storageService.get(userId, context, storageId)));
            writer.flush();
        };
    }

    @DELETE
    public Response deleteFile(@Auth User user,
                               @PathParam("userId") UUID userId,
                               @PathParam("context") StorageContexts context,
                               @PathParam("storageId") UUID storageId) throws StorageException {

        assert user.getId().equals(userId);

        storageService.delete(user.getId(), context, storageId);

        return Response.ok().build();
    }
}
