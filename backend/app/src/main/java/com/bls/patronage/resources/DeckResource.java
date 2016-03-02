package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.core.Deck;
import com.bls.patronage.db.DeckDAO;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/decks/{deckId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeckResource {
    private final DeckDAO decksDAO;

    public DeckResource(DeckDAO decksDAO) {
        this.decksDAO = decksDAO;
    }

    @GET
    public Deck getDeck(@PathParam("deckId") UUIDParam deckId) {
        return findSafely(deckId.get());
    }

    @DELETE
    public void deleteDeck(@PathParam("deckId") UUIDParam deckId) {
        decksDAO.deleteDeck(findSafely(deckId.get()).getId());
    }

    @PUT
    public Deck updateDeck(@PathParam("deckId") UUID deckId,
                           @Valid DeckRepresentation deck) {
        Deck updatedDeck = findSafely(deckId);
        updatedDeck.setName(deck.getName());
        decksDAO.updateDeck(updatedDeck);
        return updatedDeck;
    }

    private Deck findSafely(UUID deckId) {
        final Deck deck = decksDAO.getDeckById(deckId);
        if (deck == null) {
            throw new BadRequestException("There is no deck with specified ID.");
        }
        return deck;
    }

}
