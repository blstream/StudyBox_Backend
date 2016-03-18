package com.bls.patronage.resources;

import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Flashcard;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/decks/{deckId}/flashcards/{flashcardId}")
@Produces(MediaType.APPLICATION_JSON)
public class FlashcardResource {
    private final FlashcardDAO flashcardDAO;

    public FlashcardResource(FlashcardDAO flashcardDAO) {
        this.flashcardDAO = flashcardDAO;
    }

    @GET
    public Flashcard getFlashcard(
            @Valid @PathParam("flashcardId") UUIDParam flashcardId) {
        return flashcardDAO.getFlashcardById(flashcardId.get());
    }

    @DELETE
    public void deleteFlashcard(
            @Valid @PathParam("flashcardId") UUIDParam flashcardId) {
        flashcardDAO.deleteFlashcard(flashcardDAO.getFlashcardById(flashcardId.get()).getId());
    }

    @PUT
    public void updateFlashcard(
            @Valid @PathParam("flashcardId") UUID flashcardId,
            @Valid FlashcardRepresentation flashcard,
            @Valid @PathParam("deckId") UUIDParam deckId) {
        Flashcard updatedFlashcard = new Flashcard(flashcardId, flashcard.getQuestion(), flashcard.getAnswer(), deckId.get());
        flashcardDAO.updateFlashcard(updatedFlashcard);
    }
}
