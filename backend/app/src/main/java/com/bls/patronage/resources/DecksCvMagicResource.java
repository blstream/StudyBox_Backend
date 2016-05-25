package com.bls.patronage.resources;

import com.bls.patronage.StorageContexts;
import com.bls.patronage.StorageException;
import com.bls.patronage.StorageService;
import com.bls.patronage.api.AcceptableFileType;
import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.cv.CVRequest;
import com.bls.patronage.cv.CVResponse;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.Flashcard;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.Auth;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/decks/cv")
@Produces(MediaType.APPLICATION_JSON)
public class DecksCvMagicResource {

    private static final Logger LOG = LoggerFactory.getLogger(DecksCvMagicResource.class);

    private final StorageService storageService;
    private final FlashcardDAO flashcardDAO;
    private final DeckDAO deckDAO;
    private final JerseyWebTarget cvServer;

    public DecksCvMagicResource(final StorageService storageService,
                                final DeckDAO deckDAO,
                                final FlashcardDAO flashcardDAO,
                                final JerseyWebTarget cvServer) {
        this.storageService = storageService;
        this.deckDAO = deckDAO;
        this.flashcardDAO = flashcardDAO;
        this.cvServer = cvServer;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@Auth User user,
                               @FormDataParam("file") InputStream inputStream,
                               @QueryParam("fileType") @NotNull AcceptableFileType type,
                               @Context HttpServletRequest request) throws StorageException, MalformedURLException {

        LOG.debug("Data recieved: Stream - " + inputStream + " | Type - " + type);
        final UUID dataId = storageService.create(inputStream, StorageContexts.CV, user.getId());

        LOG.debug("Successfully created file with id " + dataId + " for user " + user.getId());
        final URL publicURLToUploadedFile = storageService.createPublicURL(request, StorageResource.class, user.getId(), StorageContexts.CV, dataId);

        LOG.debug("URI to file created - " + publicURLToUploadedFile + "; Sending CVRequest containing URI");
        final Response rawCVResponse = recoginzeFlashcards(
                publicURLToUploadedFile,
                type.toString());

        LOG.debug("Response recieved - " + rawCVResponse + "; Reading response and saving data");
        saveFlashcardsFromResponse(rawCVResponse, user.getId());

        LOG.debug("Save complete, deleting file");
        storageService.delete(user.getId(), StorageContexts.CV, dataId);

        LOG.debug("Returning response");
        return Response.ok().status(Response.Status.CREATED).build();
    }

    private Response recoginzeFlashcards(final URL publicURLToUploadedFile, final String fileType) {
        final Entity<CVRequest> json = Entity.json(CVRequest.createRecognizeRequest(publicURLToUploadedFile, fileType));
        return cvServer
                .request()
                .buildPost(json)
                .invoke();
    }

    private void saveFlashcardsFromResponse(final Response response, final UUID userId) {
        final Deck deck = createNewDeck(userId);

        if (response.getEntity() == null) {
            LOG.debug("No entity included in response, throwing exception");
            throw new WebApplicationException("No response recieved from CV server", 502);
        }
        final CVResponse cvResponse = mapResponseToCvResponse(response);
        switch (cvResponse.getStatus()) {
            case 0: {
                LOG.debug("CV server reported an error: " + cvResponse.getErrorDescription());
                throw new WebApplicationException("CV server error: " + cvResponse.getErrorDescription(), 502);
            }
            case 1: {
                LOG.debug("Reading flashcards from CV response");
                final List<Flashcard> flashcards = mapFlashcardsToDbModels(cvResponse.getFlashcards(), deck.getId());
                LOG.debug("Saving flashcards to database");
                saveFlashcardsToDatabase(flashcards);
                break;
            }

            case 2: {
                LOG.debug("Ok, but there was no flashcards recognized");
                throw new WebApplicationException("CV server could not recognize any flashcards from file", 400);
            }
            default: {
                throw new WebApplicationException("CV communication problem: Unrecognized status field value", 502);
            }
        }
    }

    private void saveFlashcardsToDatabase(final List<Flashcard> flashcards) {
        flashcards.forEach(flashcard -> flashcardDAO.createFlashcard(flashcard));
    }

    private List<Flashcard> mapFlashcardsToDbModels(final List<FlashcardRepresentation> flashcards, final UUID deckId) {
        return flashcards
                .stream()
                .map(flashcardRepresentation -> flashcardRepresentation.setId(UUID.randomUUID()).setHidden(false).setDeckId(deckId).map())
                .collect(Collectors.toList());
    }

    private CVResponse mapResponseToCvResponse(final Response response) {
        return response
                .readEntity(CVResponse.class);
    }

    private Deck createNewDeck(final UUID userId) {
        final Deck deck = new Deck(UUID.randomUUID(), "File Import", false);
        deckDAO.createDeck(deck, userId);
        return deck;
    }
}
