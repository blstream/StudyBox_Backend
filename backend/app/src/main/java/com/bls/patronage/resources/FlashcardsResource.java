package com.bls.patronage.resources;

import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Amount;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/decks/{deckId}/flashcards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FlashcardsResource {
    private final FlashcardDAO flashcardDAO;

    public FlashcardsResource(FlashcardDAO flashcardDAO) {
        this.flashcardDAO = flashcardDAO;
    }


    @POST
    public Response createFlashcard(@Valid FlashcardRepresentation flashcard,
                                    @Valid @PathParam("deckId") UUIDParam id) {
        flashcardDAO.createFlashcard(flashcard.setId(UUID.randomUUID()).setDeckId(id.get()).map());

        return Response.ok(flashcard).status(Response.Status.CREATED).build();
    }

    @GET
    public List<FlashcardRepresentation> listFlashcard(@Valid
                                                       @PathParam("deckId") UUIDParam id,
                                                       @QueryParam("random") Amount amount,
                                                       @QueryParam("countTips") Boolean countTips) {
        if (amount == null) {
            return flashcardDAO.getAllFlashcards(id.get())
                    .stream()
                    .map(flashcard -> ((countTips == null || !countTips ) ?
                            new FlashcardRepresentation(flashcard) :
                            new FlashcardRepresentation(flashcard).setTipsNumber(
                                    flashcardDAO.getTipsNumber(flashcard.getId()))))
                    .collect(Collectors.toList());
        } else {
            return flashcardDAO.getRandomFlashcards(amount.getValue(), id.get())
                    .stream()
                    .map(flashcard -> ((countTips == null || !countTips ) ?
                            new FlashcardRepresentation(flashcard) :
                            new FlashcardRepresentation(flashcard).setTipsNumber(
                                    flashcardDAO.getTipsNumber(flashcard.getId()))))
                    .collect(Collectors.toList());
        }
    }
}
