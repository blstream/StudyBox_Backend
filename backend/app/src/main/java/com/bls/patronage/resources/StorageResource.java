package com.bls.patronage.resources;

import com.bls.patronage.StorageException;
import com.bls.patronage.StorageService;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.UUID;

@Path("/storage/{storageId}")
@Produces(MediaType.APPLICATION_JSON)
public class StorageResource {

    private final StorageService storageService;

    public StorageResource(StorageService storageService) {
        this.storageService = storageService;
    }

    @GET
    public StreamingOutput getFile(@Auth User user,
                                   @PathParam("userId") UUID userId,
                                   @PathParam("storageId") UUID storageId) throws StorageException {

        assert user.getId().equals(userId);

        return os -> {
            final Writer writer = new BufferedWriter(
                    new OutputStreamWriter(
                            storageService.get(storageId, userId)));
            writer.flush();
        };
    }
}
