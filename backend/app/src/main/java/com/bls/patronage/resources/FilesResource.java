package com.bls.patronage.resources;

import com.bls.patronage.Message;
import com.bls.patronage.StreamPersistenceBundle;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.Auth;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Path("/user/{userId}/files")
@Produces(MediaType.APPLICATION_JSON)
public class FilesResource {

    private StreamPersistenceBundle bundle;
    private DeckDAO deckDAO;
    private java.nio.file.Path baseLocation;

    public FilesResource(StreamPersistenceBundle bundle, DeckDAO deckDAO, java.nio.file.Path baseLocation) {

        this.bundle = bundle;
        this.deckDAO = deckDAO;
        this.baseLocation = baseLocation;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @Auth User user,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition details) {

        try {
            final Deck deck = new Deck(UUID.randomUUID(), user.getName());
            final java.nio.file.Path location = baseLocation.resolve(user.getId().toString());

            deckDAO.createDeck(deck, user.getId());


            bundle.persistStreamAsFile(uploadedInputStream, location, new CVMessage(deck.getId(), location));


            return Response.ok().status(Response.Status.CREATED).build();

        } catch (IOException e) {
            return Response.serverError().entity(e.getCause()).build();
        }
    }

    private class CVMessage implements Message {

        private final UUID id;
        private final java.nio.file.Path location;

        public CVMessage(UUID id, java.nio.file.Path location) {

            this.id = id;
            this.location = location;
        }
    }
}
