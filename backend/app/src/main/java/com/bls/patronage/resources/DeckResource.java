package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.exception.ObjectBadRequestException;
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
        final Deck deck = decksDAO.getDeckById(deckId.get());
        if (deck == null) {
            throw new ObjectBadRequestException("There is no deck with specified ID.");
        }
        return deck;
    }

    @DELETE
    public void deleteDeck(
            @Valid
            @PathParam("deckId") UUIDParam deckId) {
        final Deck deck = decksDAO.getDeckById(deckId.get());
        if (deck == null) {
            throw new ObjectBadRequestException("There is no deck with specified ID.");
        }
        decksDAO.deleteDeck(deck.getId());
    }

    @PUT
    public Deck updateDeck(
            @Valid
            @PathParam("deckId") UUIDParam deckId,
            @Valid DeckRepresentation deck) {
        if (deck.getName().isEmpty())
            throw new ObjectBadRequestException("Deck name cannot be empty.");
        Deck updatedDeck = decksDAO.getDeckById(deckId.get());
        if (updatedDeck == null) {
            throw new ObjectBadRequestException("There is no deck with specified ID.");
        }
        updatedDeck.setName(deck.getName());
        decksDAO.updateDeck(updatedDeck);
        return updatedDeck;
    }
}
