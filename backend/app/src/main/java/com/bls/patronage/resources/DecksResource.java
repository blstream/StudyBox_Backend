package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.DeckWithFlashcardsNumber;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
                                      @QueryParam("includeOwn") Boolean includeOwn,
                                      @QueryParam("random") Boolean random) {
        return new DeckCollectionBuilder(user.getId())
                .includeOwn(Optional.ofNullable(includeOwn))
                .filterByName(Optional.ofNullable(name))
                .enableFlashcardsNumber(Optional.ofNullable(isEnabled))
                .getRandom(Optional.ofNullable(random))
                .build();
    }

    @Path("/me")
    @GET
    public Collection<Deck> listMyDecks(@Auth User user, @QueryParam("isEnabled") Boolean isEnabled) {

        Collection<Deck> decks = decksDAO.getAllUserDecks(user.getId());

        if (Optional.ofNullable(isEnabled).isPresent()) {
            decks = new DeckCollectionBuilder().addFlashcardsNumbersToDeck(decks);
        }

        return decks;
    }

    private class DeckCollectionBuilder {
        private Collection<Deck> deckCollection;
        private UUID userId;
        private boolean includeOwn;
        private Optional<String> filteredName;
        private Boolean enableFlashcardsNumber;
        private Boolean random;

        private DeckCollectionBuilder() {
        }
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

        public DeckCollectionBuilder getRandom(Optional<Boolean> random) {
            this.random = random.orElse(false);
            return this;
        }

        public Collection<Deck> build() {
            //pre-building deckCollection tasks
            if(filteredName.isPresent()) {
                deckCollection = decksDAO.getDecksByName(filteredName.get());
                deckCollection.addAll(
                        includeOwn ? decksDAO.getUserDecksByName(filteredName.get(), userId) : Collections.emptyList()
                );
            }

            if (random) {
                deckCollection = decksDAO.getRandomDecks(userId);
            }

            //end of building phase. If none of the above worked, collection is created now
            boolean wasPrebuild = Optional.ofNullable(deckCollection).isPresent();
            deckCollection = wasPrebuild ?  deckCollection : decksDAO.getAllDecks();

            //now the other tasks are run;
            if (includeOwn) {
                deckCollection.addAll(
                        wasPrebuild ? Collections.emptyList() : decksDAO.getAllUserDecks(userId)
                );
            }
            if (enableFlashcardsNumber) {
                deckCollection = addFlashcardsNumbersToDeck(deckCollection);
            }
            return deckCollection;
        }

        private Collection<Deck> addFlashcardsNumbersToDeck(Collection<Deck> decks) {
            Collection<Integer> flashcardsNumbers = decksDAO.getFlashcardsNumber(
                    decks.stream().map(Deck::getId).collect(Collectors.toList())
            );
            List tempDecks = new ArrayList<>();
            Iterator<Deck> deckIterator = decks.iterator();
            Iterator<Integer> numberIterator = flashcardsNumbers.iterator();
            while (deckIterator.hasNext() && numberIterator.hasNext()) {
                tempDecks.add(new DeckWithFlashcardsNumber(deckIterator.next(), numberIterator.next()));
            }

            decks = tempDecks;
            return decks;
        }
    }
}
