package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.exception.DataAccessExceptionMapper;
import com.bls.patronage.db.model.Deck;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeckResourceTest {
    private static final DeckDAO dao = mock(DeckDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new DeckResource(dao))
            .addProvider(new DataAccessExceptionMapper())
            .build();

    @Captor
    private ArgumentCaptor<Deck> deckCaptor;
    private Deck deck;
    private DeckRepresentation deckRepresentation;
    private UUID deckId;
    private UUID fakeId;

    @Before
    public void setUp() {
        deckId = UUID.fromString("a04692bc-4a70-4696-9815-24b8c0de5398");
        fakeId = UUID.fromString("12345678-9012-3456-7890-123456789012");
        deck = new Deck(deckId, "math");
        deckRepresentation = new DeckRepresentation("biology",false);
    }

    @After
    public void tearDown() {
        reset(dao);
    }

    @Test
    public void getDeckSuccess() {
        when(dao.getDeckById(deckId)).thenReturn(deck);

        Deck found = resources.getJerseyTest().target("/decks/" + deckId).request().get(Deck.class);

        verify(dao).getDeckById(deckId);
        assertThat(found).isEqualTo(deck);
    }

    @Test
    public void getDeckNotFound() {
        when(dao.getDeckById(fakeId)).thenThrow(DataAccessException.class);
        final Response response = resources.getJerseyTest().target("/decks/" + fakeId).request().get();

        verify(dao).getDeckById(fakeId);
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void deleteDeck() {
        when(dao.getDeckById(deckId)).thenReturn(deck);
        final Response response = resources.client().target("/decks/" + deckId)
                .request()
                .delete();

        verify(dao).getDeckById(deckId);
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
        verify(dao).deleteDeck(any(UUID.class));
    }

    @Test
    public void deleteDeckWhenThereIsNoDeck() {
        when(dao.getDeckById(fakeId)).thenThrow(DataAccessException.class);
        final Response response = resources.client().target("/decks/" + fakeId)
                .request()
                .delete();

        verify(dao).getDeckById(fakeId);
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        verify(dao, never()).deleteDeck(any(UUID.class));
    }

    @Test
    public void updateDeck() {
        when(dao.getDeckById(deckId)).thenReturn(deck);
        final Response response = resources.client().target("/decks/" + deckId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(deckRepresentation, MediaType.APPLICATION_JSON_TYPE));
        Deck updatedDeck = response.readEntity(Deck.class);
        verify(dao).getDeckById(deckId);
        verify(dao).updateDeck(any(Deck.class));
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(updatedDeck.getId()).isEqualTo(deckId);
        assertThat(updatedDeck.getName()).isEqualTo(deckRepresentation.getName());
    }

    @Test
    public void updateDeckWhenThereIsNoDeck() {
        when(dao.getDeckById(fakeId)).thenThrow(DataAccessException.class);
        final Response response = resources.client().target("/decks/" + fakeId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(deckRepresentation, MediaType.APPLICATION_JSON_TYPE));

        verify(dao).getDeckById(fakeId);
        verify(dao, never()).updateDeck(any(Deck.class));
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void updateDeckWithEmptyName() {
        when(dao.getDeckById(deckId)).thenReturn(deck);
        final Response response = resources.client().target("/decks/" + deckId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(new DeckRepresentation("",false), MediaType.APPLICATION_JSON));

        verify(dao, never()).getDeckById(deckId);
        verify(dao, never()).updateDeck(any(Deck.class));
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(422);
    }
}

