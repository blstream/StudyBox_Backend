package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.Deck;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
        Deck createdDeck = new Deck(UUID.randomUUID(), deck.getName(), deck.getIsPublic());
        decksDAO.createDeck(createdDeck);
        return createdDeck;
    }

    @GET
    public Response listDecks(@QueryParam("name") String name,
                              @QueryParam("statusEnabled") boolean statusEnabled) {
        if (name == null) {
            if (statusEnabled == false)
                return Response.ok(decksDAO.getAllDecks(), MediaType.APPLICATION_JSON).build();
            else
                return Response.ok(decksDAO.getAllDecksWithFlashcardsNumber(), MediaType.APPLICATION_JSON).build();
        } else
            return Response.ok(decksDAO.getDecksByName(name), MediaType.APPLICATION_JSON).build();
    }
}
