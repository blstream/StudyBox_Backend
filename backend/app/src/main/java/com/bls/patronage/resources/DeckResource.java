package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.Deck;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/decks/{deckId}")
@Produces(MediaType.APPLICATION_JSON)
public class DeckResource {
    private final DeckDAO decksDAO;

    public DeckResource(DeckDAO decksDAO) {
        this.decksDAO = decksDAO;
    }

    @GET
    public Deck getDeck(
            @Valid
            @PathParam("deckId") UUIDParam deckId) {
        return decksDAO.getDeckById(deckId.get());
    }

    @DELETE
    public void deleteDeck(
            @Valid
            @PathParam("deckId") UUIDParam deckId) {
        final Deck deck = decksDAO.getDeckById(deckId.get());
        decksDAO.deleteDeck(deck.getId());
    }

    @PUT
    public Deck updateDeck(
            @Valid
            @PathParam("deckId") UUIDParam deckId,
            @Valid DeckRepresentation deck) {

        Deck deckToUpdate = decksDAO.getDeckById(deckId.get());
        deckToUpdate.setName(deck.getName());
        deckToUpdate.setIsPublic(deck.getIsPublic());
        decksDAO.update(deckToUpdate);
        return deckToUpdate;
    }
}
