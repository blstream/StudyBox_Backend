package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.exception.EntityBadRequestException;

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
        if (deck.getName().isEmpty())
            throw new EntityBadRequestException("Deck name cannot be empty.");
        Deck createdDeck = new Deck(UUID.randomUUID(), deck.getName(), deck.isPublic());
        decksDAO.createDeck(createdDeck);
        return createdDeck;
    }

    @GET
    public List<Deck> listDecks(@QueryParam("name") String name) {
        if (name == null)
            return decksDAO.getAllDecks();
        else
            return decksDAO.getDecksByName(name);
    }
}
