package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.DeckWithFlashcardsNumber;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.BooleanParam;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/decks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DecksResource {
    private final DeckDAO decksDAO;

    public DecksResource(DeckDAO decksDAO) {
        this.decksDAO = decksDAO;
    }

    @POST
    public Response createDeck(@Auth @Valid DeckRepresentation deck, @Context SecurityContext context) {
        Deck createdDeck = new Deck(UUID.randomUUID(), deck.getName(), deck.getIsPublic());
        User userPrincipal = (User) context.getUserPrincipal();
        decksDAO.createDeck(createdDeck, userPrincipal.getId());
        return Response.ok(createdDeck).status(Response.Status.CREATED).build();
    }

    @GET
    public Collection<Deck> listDecks(@Auth User user,
                                      @QueryParam("name") String name,
                                      @QueryParam("isEnabled") Boolean isEnabled,
                                      @QueryParam("includeOwn") Boolean includeOwn) {
        return new DeckCollectionBuilder(user.getId())
                .includeOwn(Optional.ofNullable(includeOwn))
                .filterByName(Optional.ofNullable(name))
                .enableFlashcardsNumber(Optional.ofNullable(isEnabled))
                .build();
    }

    @Path("/me")
    @GET
    public Collection<Deck> listMyDecks(@Auth User user, @QueryParam("isEnabled") Boolean isEnabled) {

        if (isEnabled == null || !isEnabled) {
            return decksDAO.getAllUserDecks(user.getId());
        } else {
            Collection<Deck> decks = new ArrayList<>();
            decks.addAll(decksDAO.getAllUserDecks(user.getId()).stream().map(deck -> new DeckWithFlashcardsNumber(deck, decksDAO.getFlashcardsNumber(deck.getId()))).collect(Collectors.toCollection(ArrayList::new)));
            return decks;
        }
    }

    private class DeckCollectionBuilder {
        private Collection<Deck> deckCollection;
        private UUID userId;
        private boolean includeOwn;
        private Optional<String> filteredName;
        private Boolean enableFlashcardsNumber;

        public DeckCollectionBuilder(UUID userId) {
            this.userId = userId;
        }

        public DeckCollectionBuilder includeOwn(Optional<Boolean> value) {
            this.includeOwn = value.orElse(false);
            return this;
        }

        public DeckCollectionBuilder filterByName(Optional<String> name) {
            this.filteredName = name;
            return this;
        }


        public DeckCollectionBuilder enableFlashcardsNumber(Optional<Boolean> isEnabled) {
            this.enableFlashcardsNumber = isEnabled.orElse(false);
            return this;
        }

        public Collection<Deck> build() {
            if(filteredName.isPresent()) {
                deckCollection = decksDAO.getDecksByName(filteredName.get());
                deckCollection.addAll(
                        includeOwn ? decksDAO.getUserDecksByName(filteredName.get(), userId) : Collections.emptyList()
                );
            }

            deckCollection = Optional.ofNullable(deckCollection).isPresent() ?  deckCollection : decksDAO.getAllDecks();

            if (includeOwn) {
                deckCollection.addAll(
                        filteredName.isPresent() ? Collections.emptyList() : decksDAO.getAllUserDecks(userId)
                );
            }
            if (enableFlashcardsNumber) {
                deckCollection = deckCollection.stream().map(deck -> new DeckWithFlashcardsNumber(deck, decksDAO.getFlashcardsNumber(deck.getId()))).collect(Collectors.toList());
            }
            return deckCollection;
        }
    }
}
