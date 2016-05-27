package com.bls.patronage.resources;

import com.bls.patronage.StorageContexts;
import com.bls.patronage.StorageException;
import com.bls.patronage.StorageService;
import com.bls.patronage.db.model.User;
import com.google.common.io.ByteStreams;
import io.dropwizard.auth.Auth;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.InputStream;
import java.util.UUID;

@Path("/storage/{userId: [0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}}/{context}/{storageId: [0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}}")
@Produces("application/octet-stream")
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
            final InputStream fileStream = storageService.get(userId, StorageContexts.CV, storageId);
            ByteStreams.copy(fileStream, os);
            os.flush();
            os.close();
            fileStream.close();
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
