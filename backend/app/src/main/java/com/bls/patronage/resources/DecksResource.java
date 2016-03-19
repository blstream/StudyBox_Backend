package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.Deck;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
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
    public Deck createDeck(@Valid DeckRepresentation deck) {
        Deck createdDeck = new Deck(UUID.randomUUID(), deck.getName(), deck.getIsPublic());
        decksDAO.createDeck(createdDeck);
        return createdDeck;
    }

    @GET
    public Collection<Deck> listDecks(@QueryParam("name") String name,
                                      @QueryParam("isEnabled") Boolean isEnabled) {
        if (name == null) {
            if (isEnabled == null || !isEnabled) {
                return decksDAO.getAllDecks();
            } else {
                Collection<Deck> decks = new ArrayList<>();
                decks.addAll(decksDAO.getAllDecksWithFlashcardsNumber());
                return decks;
            }
        } else {
            return decksDAO.getDecksByName(name);
        }

    }
}
