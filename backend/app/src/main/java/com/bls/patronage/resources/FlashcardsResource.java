package com.bls.patronage.resources;

import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Amount;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
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
    public Response createFlashcard(@Auth @Valid FlashcardRepresentation flashcard,
                                    @Valid @PathParam("deckId") UUIDParam id,
                                    @Context SecurityContext context) {

        final User user = (User) context.getUserPrincipal();

        flashcardDAO.createFlashcard(flashcard.setId(UUID.randomUUID()).setDeckId(id.get()).map(), user.getId());

        return Response.ok(flashcard).status(Response.Status.CREATED).build();
    }

    @GET
    public List<FlashcardRepresentation> listFlashcard(@Valid
                                                       @PathParam("deckId") UUIDParam id,
                                                       @QueryParam("random") Amount amount,
                                                       @QueryParam("tipsCount") Boolean tipsCount) {
        if (amount == null) {
            return flashcardDAO.getAllFlashcards(id.get())
                    .stream()
                    .map(flashcard -> ((tipsCount == null || !tipsCount ) ?
                            new FlashcardRepresentation(flashcard) :
                            new FlashcardRepresentation(flashcard)
                                    .setTipsCount(flashcardDAO.getTipsCount(flashcard.getId()))
                                    .setAuditFields(flashcardDAO.getFlashcardAuditFields(flashcard.getId()))))
                    .collect(Collectors.toList());
        } else {
            return flashcardDAO.getRandomFlashcards(amount.getValue(), id.get())
                    .stream()
                    .map(flashcard -> ((tipsCount == null || !tipsCount ) ?
                            new FlashcardRepresentation(flashcard) :
                            new FlashcardRepresentation(flashcard).setTipsCount(
                                    flashcardDAO.getTipsCount(flashcard.getId()))
                                    .setAuditFields(flashcardDAO.getFlashcardAuditFields(flashcard.getId()))))
                    .collect(Collectors.toList());
        }
    }
}
