package com.bls.patronage.resources;

import java.util.UUID;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.Deck;

import io.dropwizard.jersey.params.UUIDParam;

@Path("/decks/{deckId}")
@Produces(MediaType.APPLICATION_JSON)
public class DeckResource {
    private final DeckDAO decksDAO;

    public DeckResource(DeckDAO decksDAO) {
        this.decksDAO = decksDAO;
    }

    @GET
    public Deck getDeck(@PathParam("deckId") UUIDParam deckId) {
        return decksDAO.getDeckById(deckId.get());
    }

    @DELETE
    public void deleteDeck(@PathParam("deckId") UUIDParam deckId) {
        decksDAO.deleteDeck(decksDAO.getDeckById(deckId.get()).getId());
    }

    @PUT
    public void updateDeck(@PathParam("deckId") UUID deckId,
                           @Valid DeckRepresentation deck) {
        Deck updatedDeck = decksDAO.getDeckById(deckId);
        updatedDeck.setName(deck.getName());
        decksDAO.updateDeck(updatedDeck);
    }
}
