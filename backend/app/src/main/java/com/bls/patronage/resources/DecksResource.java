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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

        final User user = (User) context.getUserPrincipal();

        decksDAO.createDeck(deck.setId(UUID.randomUUID()).map(), user.getId());

        return Response.ok(deck).status(Response.Status.CREATED).build();
    }

    @GET
    public Collection<DeckRepresentation> listDecks(@Auth User user,
                                                    @QueryParam("name") String name,
                                                    @QueryParam("flashcardsCount") Boolean flashcardsCount,
                                                    @QueryParam("includeOwn") Boolean includeOwn,
                                                    @QueryParam("random") Boolean random) {

        return new DeckCollectionBuilder(user.getId())
                .includeOwn(Optional.ofNullable(includeOwn))
                .filterByName(Optional.ofNullable(name))
                .enableFlashcardsCounts(Optional.ofNullable(flashcardsCount))
                .getRandom(Optional.ofNullable(random))
                .build();
    }

    @Path("/me")
    @GET
    public Collection<DeckRepresentation> listMyDecks(@Auth User user,
                                                      @QueryParam("flashcardsCount") Boolean flashcardsCount) {

        Collection<Deck> decks = decksDAO.getAllUserDecks(user.getId());

        if (Optional.ofNullable(flashcardsCount).isPresent()) {
            return new DeckCollectionBuilder().addFlashcardsCountsToDeck(decks);
        } else {
            return decks
                    .stream()
                    .map(deck -> new DeckRepresentation(deck)
                            .setCreationDate(decksDAO.getDeckCreationDate(deck.getId())))
                    .sorted(Comparator.comparing(DeckRepresentation::getCreationDate, Comparator.reverseOrder()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    private class DeckCollectionBuilder {
        private Collection<Deck> deckCollection;
        private UUID userId;
        private boolean includeOwn;
        private Optional<String> filteredName;
        private Boolean enableFlashcardsCounts;
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

        public DeckCollectionBuilder enableFlashcardsCounts(Optional<Boolean> flashcardsCount) {
            this.enableFlashcardsCounts = flashcardsCount.orElse(false);
            return this;
        }

        public DeckCollectionBuilder getRandom(Optional<Boolean> random) {
            this.random = random.orElse(false);
            return this;
        }

        public Collection<DeckRepresentation> build() {
            //pre-building deckCollection tasks
            if (filteredName.isPresent()) {
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
            deckCollection = wasPrebuild ? deckCollection : decksDAO.getAllDecks();

            //now the other tasks are run;
            if (includeOwn) {
                deckCollection.addAll(
                        wasPrebuild ? Collections.emptyList() : decksDAO.getAllUserDecks(userId)
                );
                deckCollection = deckCollection
                        .parallelStream()
                        .distinct()
                        .collect(Collectors.toList());
            }

            if (enableFlashcardsCounts) {
                return addFlashcardsCountsToDeck(deckCollection);
            }

            return deckCollectionToDeckRepresentationCollection(deckCollection);
        }

        private Collection<DeckRepresentation> addFlashcardsCountsToDeck(Collection<Deck> decks) {
            List<Integer> flashcardsCounts =
                    decks.stream()
                            .map(deck -> decksDAO.getFlashcardsCount(deck.getId()))
                            .collect(Collectors.toList());

            List tempDecks = new ArrayList<>();
            Iterator<Integer> numberIterator = flashcardsCounts.iterator();

            tempDecks.addAll(decks.stream()
                    .map(deck -> new DeckRepresentation(deck)
                            .setFlashcardsCount(numberIterator.next())
                            .setCreationDate(decksDAO.getDeckCreationDate(deck.getId())))
                    .sorted(Comparator.comparing(DeckRepresentation::getCreationDate, Comparator.reverseOrder()))
                    .collect(Collectors.toList()));

            return tempDecks;
        }
    }

    private Collection<DeckRepresentation> deckCollectionToDeckRepresentationCollection(Collection<Deck> deckCollection) {

        return deckCollection
                .stream()
                .map(deck -> new DeckRepresentation(deck)
                        .setCreatorEmail(decksDAO.getCreatorEmailFromDeckId(deck.getId()))
                        .setCreationDate(decksDAO.getDeckCreationDate(deck.getId())))
                .sorted(Comparator.comparing(DeckRepresentation::getCreationDate, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}
