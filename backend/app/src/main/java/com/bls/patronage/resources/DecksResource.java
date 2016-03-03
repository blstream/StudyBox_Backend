package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.core.Deck;
import com.bls.patronage.db.DeckDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@Path("/decks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DecksResource {
    private final DeckDAO decksDAO;

    public DecksResource(DeckDAO decksDAO) {
        this.decksDAO = decksDAO;
    }

    @POST
    public Deck createDeck(DeckRepresentation deck) {
        Deck createdDeck = new Deck(UUID.randomUUID(), deck.getName());
        decksDAO.createDeck(createdDeck);
        return createdDeck;
    }

    @GET
    public List<Deck> listDecks() {
        return decksDAO.getAllDecks();
    }
}
