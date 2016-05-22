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
                               @QueryParam("fileType") @NotNull AcceptableFileType type,
                               @Context HttpServletRequest request) throws StorageException, MalformedURLException {
        LOG.debug("Data recieved: Stream - " + inputStream + " | Type - " + type);
        final UUID dataId = storageService.create(inputStream, StorageContexts.CV, user.getId());
        LOG.debug("Successfully created file with id " + dataId + " for user " + user.getId());
        final URL publicURLToUploadedFile = storageService.createPublicURL(request, StorageResource.class, user.getId(), StorageContexts.CV, dataId);
        LOG.debug("URI to file created - " + publicURLToUploadedFile);
        LOG.debug("Sending CVRequest containing URI");
        final Response rawCVResponse = recoginzeFlashcards(
                publicURLToUploadedFile,
                type.toString());
        LOG.debug("Response recieved - " + rawCVResponse);
        LOG.debug("Saving flashcards from response to database");
        saveFlashcardsFromResponse(rawCVResponse, user.getId());

        LOG.debug("Save complete, deleting file");
        storageService.delete(user.getId(), StorageContexts.CV, dataId);
        LOG.debug("Returning response");
        return Response.ok().status(Response.Status.CREATED).build();
    }

    private Response recoginzeFlashcards(final URL publicURLToUploadedFile, String fileType) {
        Entity<CVRequest> json = Entity.json(CVRequest.createRecognizeRequest(publicURLToUploadedFile, fileType));
        System.out.println(json);
        return cvServer
                .request()
                .buildPost(json)
                .invoke();
    }

    private void saveFlashcardsFromResponse(Response response, UUID userId) {
        final Deck deck = createNewDeck(userId);

        if (response.getEntity() == null) {
            throw new WebApplicationException("No response recieved from CV server", 502);
        }
        CVResponse cvResponse = mapResponseToCvResponse(response);
        System.out.println(cvResponse);
        switch (cvResponse.getStatus()) {
            case 0: {
                throw new WebApplicationException("CV server error: " + cvResponse.getErrorDescription(), 502);
            }
            case 1: {
                List<Flashcard> flashcards = mapFlashcardsToDbModels(cvResponse.getFlashcards(), deck.getId());
                saveFlashcardsToDatabase(flashcards);
            }

            case 2: {
                throw new WebApplicationException("CV server response: " + cvResponse.getErrorDescription(), 400);
            }
            default: {
                throw new WebApplicationException("CV server error: Unrecognized status field value", 502);
            }
        }
    }

    private void saveFlashcardsToDatabase(List<Flashcard> flashcards) {
        flashcards.forEach(flashcard -> flashcardDAO.createFlashcard(flashcard));
    }

    private List<Flashcard> mapFlashcardsToDbModels(List<FlashcardRepresentation> flashcards, UUID deckId) {
        return flashcards
                .stream()
                .map(flashcardRepresentation -> flashcardRepresentation.setDeckId(deckId).map())
                .collect(Collectors.toList());
    }

    private CVResponse mapResponseToCvResponse(Response response) {
        return response
                .readEntity(CVResponse.class);
    }

    private Deck createNewDeck(final UUID userId) {
        final Deck deck = new Deck(UUID.randomUUID(), "File Import", false);
        deckDAO.createDeck(deck, userId);
        return deck;
    }
}
