package com.bls.patronage.resources;

import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Flashcard;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@Path("/decks/{deckId}/flashcards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FlashcardsResource {
    private final FlashcardDAO flashcardDAO;

    public FlashcardsResource(FlashcardDAO flashcardDAO) {
        this.flashcardDAO = flashcardDAO;
    }

    @POST
    public Flashcard createFlashcard(@Valid FlashcardRepresentation flashcard,
                                     @Valid @PathParam("deckId") UUIDParam id) {
        Flashcard createdFlashcard = new Flashcard(UUID.randomUUID(), flashcard.getQuestion(), flashcard.getAnswer(), id.get());
        flashcardDAO.createFlashcard(createdFlashcard);
        return createdFlashcard;
    }

    @GET
    public List<Flashcard> listDecks(@Valid @PathParam("deckId") UUIDParam id) {
        return flashcardDAO.getAllFlashcards(id.get());
    }
}
