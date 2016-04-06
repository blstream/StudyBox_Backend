package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.Auth;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
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
    public Deck createDeck(@Auth @Valid DeckRepresentation deck, @Context SecurityContext context) {
        Deck createdDeck = new Deck(UUID.randomUUID(), deck.getName(), deck.getIsPublic());
        User userPrincipal = (User) context.getUserPrincipal();
        decksDAO.createDeck(createdDeck, userPrincipal.getId());
        return createdDeck;
    }

    @GET
    public Collection<Deck> listDecks(@Auth User user,
                                      @QueryParam("name") String name,
                                      @QueryParam("isEnabled") Boolean isEnabled,
                                      @QueryParam("includeOwn") Boolean includeOwn,
                                      @QueryParam("random") Boolean random) {

        Collection<Deck> decks;

        if (name == null) {
            if (isEnabled == null || !isEnabled) {
                if (includeOwn == null || !includeOwn) {
                    if (random == null || !random) {
                        decks = decksDAO.getAllDecks(user.getId());
                        return decks;
                    } else {
                        decks = decksDAO.getRandomDecks(user.getId());
                        return decks;
                    }
                } else {
                    decks = decksDAO.getAllDecks(user.getId());
                    decks.addAll(decksDAO.getAllUserDecks(user.getId()));
                    return decks;
                }

            } else {
                decks = new ArrayList<>();
                decks.addAll(decksDAO.getAllDecksWithFlashcardsNumber(user.getId()));

                if (includeOwn == null || !includeOwn) return decks;
                else {
                    decks.addAll(decksDAO.getAllUserDecksWithFlashcardsNumber(user.getId()));
                    return decks;
                }
            }
        } else {
            decks = decksDAO.getDecksByName(name, user.getId());

            if(includeOwn == null || !includeOwn) return decks;
            else {
                decks.addAll(decksDAO.getUserDecksByName(name, user.getId()));
                return decks;
            }
        }
    }

    @Path("/me")
    @GET
    public Collection<Deck> listMyDecks(@Auth User user, @QueryParam("isEnabled") Boolean isEnabled) {

        if (isEnabled == null || !isEnabled) {
            return decksDAO.getAllUserDecks(user.getId());
        } else {
            Collection<Deck> decks = new ArrayList<>();
            decks.addAll(decksDAO.getAllUserDecksWithFlashcardsNumber(user.getId()));
            return decks;
        }
    }
}
