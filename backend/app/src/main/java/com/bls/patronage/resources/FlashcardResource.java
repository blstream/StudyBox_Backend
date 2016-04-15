package com.bls.patronage.resources;

import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.db.dao.FlashcardDAO;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/decks/{deckId}/flashcards/{flashcardId}")
@Produces(MediaType.APPLICATION_JSON)
public class FlashcardResource {
    private final FlashcardDAO flashcardDAO;

    public FlashcardResource(FlashcardDAO flashcardDAO) {
        this.flashcardDAO = flashcardDAO;
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
    public FlashcardRepresentation updateFlashcard(
            @Valid @PathParam("flashcardId") UUIDParam flashcardId,
            @Valid FlashcardRepresentation flashcard,
            @Valid @PathParam("deckId") UUIDParam deckId) {


        flashcardDAO.getFlashcardById(flashcardId.get());
        flashcardDAO.updateFlashcard(flashcard.setId(flashcardId.get()).setDeckId(deckId.get()).buildDbModel());


        return flashcard;
    }
}
