package com.bls.patronage.resources;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.Deck;

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
        if (deck.getName().isEmpty())
            throw new WebApplicationException(400);
        Deck createdDeck = new Deck(UUID.randomUUID(), deck.getName());
        decksDAO.createDeck(createdDeck);
        return createdDeck;
    }

    @GET
    public List<Deck> listDecks() {
        return decksDAO.getAllDecks();
    }
}
