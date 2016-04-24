package com.bls.patronage.resources;

import com.bls.patronage.Message;
import com.bls.patronage.StreamPersistenceBundle;
import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.Auth;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Path("/users/{userId}/files")
@Produces(MediaType.APPLICATION_JSON)
public class FilesResource {

    private final StreamPersistenceBundle bundle;
    private final java.nio.file.Path baseLocation;
    private final FlashcardDAO flashcardDAO;
    private final DeckDAO deckDAO;

    public FilesResource(StreamPersistenceBundle bundle, java.nio.file.Path baseLocation, DeckDAO deckDAO, FlashcardDAO flashcardDAO) {
        this.bundle = bundle;
        this.deckDAO = deckDAO;
        this.baseLocation = baseLocation;
        this.flashcardDAO = flashcardDAO;
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
            final Response response = bundle.persistStreamAsFile(
                    uploadedInputStream,
                    location,
                    new CVMessage(location.toUri().toURL(), "ImageToFlashcard") //TODO ask if CV can resolve type
            );

            saveFlashcardsFromResponse(response, deck);

            return Response.ok().status(Response.Status.CREATED).build();

        } catch (IOException e) {
            return Response.serverError().entity(e.getCause()).build();
        }
    }

    private void saveFlashcardsFromResponse(Response response, Deck deck) {
        response.readEntity(new GenericType<List<FlashcardRepresentation>>() {
        })
                .stream()
                .forEach(flashcardRepresentation -> flashcardDAO.createFlashcard(
                        flashcardRepresentation.setDeckId(deck.getId()).map()
                        )
                );
    }

    private class CVMessage implements Message {

        private final URL location;
        private final String action;

        private CVMessage(URL location, String action) {
            this.location = location;
            this.action = action;
        }
    }
}
