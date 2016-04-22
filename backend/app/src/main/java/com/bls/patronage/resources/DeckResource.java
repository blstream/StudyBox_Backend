package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.BooleanParam;
import io.dropwizard.jersey.params.UUIDParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/decks/{deckId: [0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}}")
@Produces(MediaType.APPLICATION_JSON)
public class DeckResource {
    private final DeckDAO decksDAO;

    public DeckResource(DeckDAO decksDAO) {
        this.decksDAO = decksDAO;
    }

    @GET
    public DeckRepresentation getDeck(
            @Auth User user,
            @Valid
            @PathParam("deckId") UUIDParam deckId) {

        return new DeckRepresentation(
                decksDAO.getDeckById(deckId.get(), user.getId())
        );
    }

    @DELETE
    public void deleteDeck(
            @Auth User user,
            @Valid
            @PathParam("deckId") UUIDParam deckId) {

        decksDAO.getDeckById(deckId.get(), user.getId());
        decksDAO.deleteDeck(deckId.get());
    }

    @PUT
    public DeckRepresentation updateDeck(
            @Auth User user,
            @Valid
            @PathParam("deckId") UUIDParam deckId,
            @Valid DeckRepresentation deck) {

        decksDAO.getDeckById(deckId.get(), user.getId());
        decksDAO.update(deck.setId(deckId.get()).map());
        return deck;
    }

    @Path("/public/{access}")
    @POST
    public void changeStatus(@Auth User user,
                             @Valid @PathParam("deckId") UUIDParam deckId,
                             @Valid @PathParam("access") BooleanParam access) {

        decksDAO.update(
                decksDAO.getDeckById(deckId.get(), user.getId())
                        .setIsPublic(access.get())
        );
    }
}
