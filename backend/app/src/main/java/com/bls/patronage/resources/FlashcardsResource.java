package com.bls.patronage.resources;

import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Flashcard;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@Path("/flashcards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FlashcardsResource {
    private final FlashcardDAO flashcardDAO;

    public FlashcardsResource(FlashcardDAO flashcardDAO) {
        this.flashcardDAO = flashcardDAO;
    }

    @POST
    public Flashcard createFlashcard(FlashcardRepresentation flashcard) {
        Flashcard createdFlashcard = new Flashcard(UUID.randomUUID(), flashcard.getQuestion(), flashcard.getAnswer());
        flashcardDAO.createFlashcard(createdFlashcard);
        return createdFlashcard;
    }

    @GET
    public List<Flashcard> listDecks() {
        return flashcardDAO.getAllFlashcards();
    }
}
