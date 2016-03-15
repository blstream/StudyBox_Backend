package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.DeckWithFlashcardsNumber;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DecksResourceTest {
    private static final DeckDAO dao = mock(DeckDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new DecksResource(dao))
            .build();

    @Captor
    private ArgumentCaptor<Deck> deckCaptor;
    private Deck deck;
    private DeckRepresentation deckRepresentation;

    @Before
    public void setUp() {
        deck = new Deck("12345678-9012-3456-7890-123456789012", "math");
        deckRepresentation = new DeckRepresentation("math", false);
    }

    @After
    public void tearDown() {
        reset(dao);
    }

    @Test
    public void createDeck() throws JsonProcessingException {
        final Response response = resources.client().target("/decks")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(deckRepresentation, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(dao).createDeck(deckCaptor.capture());
        assertThat(deckCaptor.getValue().getId()).isNotNull();
        assertThat(deckCaptor.getValue().getName()).isEqualTo(deck.getName());
    }

    @Test
    public void createDeckWithoutName() throws JsonProcessingException {
        final Response response = resources.client().target("/decks")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(new DeckRepresentation("", false), MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.BAD_REQUEST);
    }

    @Test
    public void listDecks() throws Exception {
        final ImmutableList<Deck> decks = ImmutableList.of(deck);
        when(dao.getAllDecks()).thenReturn(decks);

        final List<Deck> response = resources.client().target("/decks")
                .request().get(new GenericType<List<Deck>>() {
                });

        verify(dao).getAllDecks();
        assertThat(response).containsAll(decks);
    }

    @Test
    public void listDecksByNames() throws Exception {
        final ImmutableList<Deck> decks = ImmutableList.of(deck);
        when(dao.getDecksByName("something")).thenReturn(decks);

        final List<Deck> response = resources.client().target("/decks?name=something")
                .request().get(new GenericType<List<Deck>>() {
                });

        verify(dao).getDecksByName("something");
        assertThat(response).containsAll(decks);
    }

    @Test
    public void listDecksByNamesWhenThereIsBadNameTyped() throws Exception {
        when(dao.getDecksByName("anotherThing")).thenReturn(null);

        final List<Deck> response = resources.client().target("/decks?name=anotherThing")
                .request().get(new GenericType<List<Deck>>() {
                });

        verify(dao).getDecksByName("anotherThing");
        assertThat(response).isNull();
    }

    @Test
    public void listDecksByNamesWhenThereIsNoNameTyped() throws Exception {
        when(dao.getDecksByName("")).thenReturn(null);

        final List<Deck> response = resources.client().target("/decks?name=")
                .request().get(new GenericType<List<Deck>>() {
                });

        verify(dao).getDecksByName("");
        assertThat(response).isNull();
    }

    @Test
    public void listDecksWithFlashcardsNumber() throws Exception {
        final DeckWithFlashcardsNumber deckExample = new DeckWithFlashcardsNumber(UUID.randomUUID(),
                "math", true, 3);
        final ImmutableList<DeckWithFlashcardsNumber> decks = ImmutableList.of(deckExample);
        when(dao.getAllDecksWithFlashcardsNumber()).thenReturn(decks);

        final List<DeckWithFlashcardsNumber> response = resources.client().target("/decks?statusEnabled=true")
                .request().get(new GenericType<List<DeckWithFlashcardsNumber>>() {
                });

        verify(dao).getAllDecksWithFlashcardsNumber();
        assertThat(response).containsAll(decks);
        assertThat(response.get(0).getCount()).isNotNull();
    }
}
