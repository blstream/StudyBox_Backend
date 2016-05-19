package com.bls.patronage.resources;

import com.bls.patronage.StorageContexts;
import com.bls.patronage.StorageException;
import com.bls.patronage.StorageService;
import com.bls.patronage.api.AcceptableFileTypes;
import com.bls.patronage.cv.CVRequest;
import com.bls.patronage.cv.CVResponse;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.Auth;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;

@Path("/decks/cv")
@Produces(MediaType.APPLICATION_JSON)
public class DecksCvMagicResource {

    private static final Logger LOG = LoggerFactory.getLogger(DecksCvMagicResource.class);

    private final StorageService storageService;
    private final FlashcardDAO flashcardDAO;
    private final DeckDAO deckDAO;
    private final JerseyWebTarget cvServer;

    public DecksCvMagicResource(StorageService storageService, DeckDAO deckDAO, FlashcardDAO flashcardDAO, final JerseyWebTarget cvServer) {
        this.storageService = storageService;
        this.deckDAO = deckDAO;
        this.flashcardDAO = flashcardDAO;
        this.cvServer = cvServer;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@Auth User user,
                               @FormDataParam("file") InputStream inputStream,
                               @QueryParam("fileType") AcceptableFileTypes type) throws StorageException {
        LOG.debug("Data recieved: Stream - " + inputStream + " | Type - " + type);
        final UUID dataId = storageService.create(inputStream, StorageContexts.CV, user.getId());
        LOG.debug("Successfully created file with id " + dataId + " for user " + user.getId());
        final URI publicURLToUploadedFile = storageService.createPublicURI(StorageResource.class, user.getId(), StorageContexts.CV, dataId);
        LOG.debug("URI to file created - " + publicURLToUploadedFile);

        LOG.debug("Sending CVRequest containing URI");
        final Response rawCVResponse = recoginzeFlashcards(publicURLToUploadedFile, type.getFileType());
        LOG.debug("Response recieved - " + rawCVResponse);
        LOG.debug("Saving flashcards from response to database");
        saveFlashcardsFromResponse(rawCVResponse, user.getId());

        LOG.debug("Save complete, deleting file");
        storageService.delete(user.getId(), StorageContexts.CV, dataId);
        LOG.debug("Returning 201 response");
        return Response.ok()
                .status(Response.Status.CREATED).build();
    }

    private Response recoginzeFlashcards(final URI publicURLToUploadedFile, String fileType) {
        return cvServer
                .request()
                .buildPost(Entity.json(CVRequest.createRecognizeRequest(publicURLToUploadedFile, fileType)))
                .invoke();
    }

    private void saveFlashcardsFromResponse(Response response, UUID userId) {
        final Deck deck = createNewDeck(userId);

        if (response.getEntity() != null) {
            response
                    .readEntity(CVResponse.class)
                    .getFlashcards()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(flashcardRepresentation -> flashcardRepresentation.setDeckId(deck.getId()).map())
                    .forEach(flashcard -> flashcardDAO.createFlashcard(flashcard));
        }
    }

    private Deck createNewDeck(final UUID userId) {
        final Deck deck = new Deck(UUID.randomUUID());
        deckDAO.createDeck(deck, userId);
        return deck;
    }
}
