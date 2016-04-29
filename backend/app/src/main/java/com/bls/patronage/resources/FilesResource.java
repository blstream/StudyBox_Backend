package com.bls.patronage.resources;

import com.bls.patronage.StreamPersistenceBundle;
import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.User;
import com.bls.patronage.helpers.FileHelper;
import io.dropwizard.auth.Auth;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Path("/users/{userId}/files")
@Produces(MediaType.APPLICATION_JSON)
public class FilesResource {

    private final java.nio.file.Path baseLocation;
    private final FlashcardDAO flashcardDAO;
    private final DeckDAO deckDAO;
    private final FileHelper helper;

    public FilesResource(StreamPersistenceBundle bundle, java.nio.file.Path baseLocation, DeckDAO deckDAO, FlashcardDAO flashcardDAO) {
        this.helper = new FileHelper(bundle);
        this.deckDAO = deckDAO;
        this.baseLocation = baseLocation;
        this.flashcardDAO = flashcardDAO;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @Auth User user,
            @FormDataParam("file") InputStream uploadedInputStream) throws Exception {

        final Deck deck = new Deck(UUID.randomUUID(), "foo");
        final java.nio.file.Path location = baseLocation.resolve(user.getId().toString());


        Response response = helper.handleInputStream(uploadedInputStream, location);

        saveFlashcardsFromResponse(response, deck, user.getId());

        helper.cleanUp(location);

        return Response.ok().status(Response.Status.CREATED).build();
    }

    private void saveFlashcardsFromResponse(Response response, Deck deck, UUID userId) {
        deckDAO.createDeck(deck, userId);
        response
                .readEntity(new GenericType<List<FlashcardRepresentation>>() {
                })
                .stream()
                .filter(Objects::nonNull)
                .forEach(flashcardRepresentation -> flashcardDAO.createFlashcard(
                        flashcardRepresentation.setDeckId(deck.getId()).map()
                        )
                );
    }
}
