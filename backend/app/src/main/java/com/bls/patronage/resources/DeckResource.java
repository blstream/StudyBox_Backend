package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.Deck;
import io.dropwizard.jersey.params.BooleanParam;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.UUIDParam;

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
    public Deck getDeck(
            @Auth
            @Valid
            @PathParam("deckId") UUIDParam deckId) {
        return decksDAO.getDeckById(deckId.get());
    }

    @DELETE
    public void deleteDeck(
            @Auth
            @Valid
            @PathParam("deckId") UUIDParam deckId) {
        final Deck deck = decksDAO.getDeckById(deckId.get());
        decksDAO.deleteDeck(deck.getId());
    }

    @PUT
    public Deck updateDeck(
            @Auth
            @Valid
            @PathParam("deckId") UUIDParam deckId,
            @Valid DeckRepresentation deck) {

        Deck deckToUpdate = decksDAO.getDeckById(deckId.get());
        deckToUpdate.setName(deck.getName());
        deckToUpdate.setIsPublic(deck.getIsPublic());
        decksDAO.update(deckToUpdate);
        return deckToUpdate;
    }

    @Path("/public/{access}")
    @POST
    public void changeStatus(@Valid @PathParam("deckId") UUIDParam deckId,
                             @Valid @PathParam("access") BooleanParam access) {
        Deck deck = decksDAO.getDeckById(deckId.get());
        deck.setIsPublic(access.get());
        decksDAO.update(deck);
    }
}
