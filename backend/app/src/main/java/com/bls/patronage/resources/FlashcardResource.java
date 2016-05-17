package com.bls.patronage.resources;

import com.bls.patronage.StorageContexts;
import com.bls.patronage.StorageException;
import com.bls.patronage.StorageService;
import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Flashcard;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.UUIDParam;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

@Path("/decks/{deckId}/flashcards/{flashcardId}")
@Produces(MediaType.APPLICATION_JSON)
public class FlashcardResource {
    private final FlashcardDAO flashcardDAO;
    private final StorageService storageService;

    public FlashcardResource(FlashcardDAO flashcardDAO, StorageService storageService) {
        this.flashcardDAO = flashcardDAO;
        this.storageService = storageService;
    }

    @GET
    public FlashcardRepresentation getFlashcard(
            @Valid @PathParam("flashcardId") UUIDParam flashcardId) {

        return new FlashcardRepresentation(
                flashcardDAO.getFlashcardById(flashcardId.get())
        );
    }

    @DELETE
    public void deleteFlashcard(
            @Valid @PathParam("flashcardId") UUIDParam flashcardId) {

        flashcardDAO.getFlashcardById(flashcardId.get());
        flashcardDAO.deleteFlashcard(flashcardId.get());
    }

    @PUT
    public FlashcardRepresentation updateFlashcard(@Auth
            @Valid @PathParam("flashcardId") UUIDParam flashcardId,
            @Valid FlashcardRepresentation flashcard,
            @Valid @PathParam("deckId") UUIDParam deckId,
            @Context SecurityContext context) {

        final User user = (User) context.getUserPrincipal();

        flashcardDAO.getFlashcardById(flashcardId.get());
        flashcardDAO.updateFlashcard(flashcard.setId(flashcardId.get()).setDeckId(deckId.get()).map(), user.getId());


        return flashcard;
    }

    @Path("/questionImage")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public FlashcardRepresentation postQuestionImage(
            @Auth User user,
            @Valid @PathParam("flashcardId") UUIDParam flashcardId,
            @Valid @PathParam("deckId") UUIDParam deckId,
            @FormDataParam("file") InputStream inputStream) throws StorageException {

        UUID questionImageId = storageService.create(inputStream, StorageContexts.FLASHCARDS, user.getId());
        URI questionImageURI = storageService.createPublicURI(StorageResource.class, questionImageId, StorageContexts.FLASHCARDS, user.getId());

        Flashcard result = flashcardDAO.getFlashcardById(flashcardId.get()).setQuestionImageURL(questionImageURI.toString());
        flashcardDAO.updateFlashcard(result, user.getId());

        return new FlashcardRepresentation(result);
    }

    @Path("/answerImage")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public FlashcardRepresentation postAnswerImage(
            @Auth User user,
            @Valid @PathParam("flashcardId") UUIDParam flashcardId,
            @Valid @PathParam("deckId") UUIDParam deckId,
            @FormDataParam("file") InputStream inputStream) throws StorageException {

        UUID answerImageId = storageService.create(inputStream, StorageContexts.FLASHCARDS, user.getId());
        URI answerImageURI = storageService.createPublicURI(StorageResource.class, answerImageId, StorageContexts.FLASHCARDS, user.getId());

        Flashcard result = flashcardDAO.getFlashcardById(flashcardId.get()).setAnswerImageURL(answerImageURI.toString());
        flashcardDAO.updateFlashcard(result, user.getId());

        return new FlashcardRepresentation(result);
    }
}
