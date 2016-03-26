package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.model.Deck;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeckResourceTest extends BasicAuthenticationTest {

    @Captor
    private ArgumentCaptor<Deck> deckCaptor;
    private Deck deck;
    private DeckRepresentation deckRepresentation;
    private UUID deckId;
    private UUID fakeId;
    private String deckURI;
    private String fakeURI;

    @Before
    public void setUp() {
        super.setUp();
        deckId = UUID.fromString("a04692bc-4a70-4696-9815-24b8c0de5398");
        fakeId = UUID.fromString("12345678-9012-3456-7890-123456789012");
        deck = new Deck(deckId, "math");
        deckRepresentation = new DeckRepresentation("biology", false);
        deckURI = UriBuilder.fromResource(DeckResource.class).build(deckId).toString();
        fakeURI = UriBuilder.fromResource(DeckResource.class).build(fakeId).toString();
    }

    @Test
    public void getDeckSuccess() {
        when(deckDao.getDeckById(deckId)).thenReturn(deck);
        when(userDAO.getUserByEmail(user.getEmail())).thenReturn(user);

        final Response response = getResponseWithCredentials(deckURI, encodedCredentials);
        final Deck found = response.readEntity(Deck.class);

        assertThat(found).isEqualTo(deck);
    }

    @Test
    public void getDeckNotFound() {
        when(deckDao.getDeckById(fakeId)).thenThrow(DataAccessException.class);
        when(userDAO.getUserByEmail(user.getEmail())).thenReturn(user);

        final Response response = getResponseWithCredentials(fakeURI, encodedCredentials);

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void deleteDeck() {
        when(deckDao.getDeckById(deckId)).thenReturn(deck);
        when(userDAO.getUserByEmail(user.getEmail())).thenReturn(user);

        final Response response = authResources.client()
                .target(deckURI).request().header("Authorization", "Basic " + encodedCredentials)
                .delete();

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
        verify(deckDao).deleteDeck(any(UUID.class));
    }

    @Test
    public void deleteDeckWhenThereIsNoDeck() {
        when(deckDao.getDeckById(fakeId)).thenThrow(new DataAccessException(""));
        when(userDAO.getUserByEmail(user.getEmail())).thenReturn(user);

        final Response response = authResources.client().target(fakeURI)
                .request().header("Authorization", "Basic " + encodedCredentials)
                .delete();

        verify(deckDao).getDeckById(fakeId);
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        verify(deckDao, never()).deleteDeck(any(UUID.class));
    }

    @Test
    public void updateDeck() {
        when(deckDao.getDeckById(deckId)).thenReturn(deck);
        when(userDAO.getUserByEmail(user.getEmail())).thenReturn(user);

        final Response response = getPutResponse(deckURI, deckRepresentation, encodedCredentials);

        final Deck updatedDeck = response.readEntity(Deck.class);
        verify(deckDao).getDeckById(deckId);
        verify(deckDao).update(any(Deck.class));
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(updatedDeck.getId()).isEqualTo(deckId);
        assertThat(updatedDeck.getName()).isEqualTo(deckRepresentation.getName());
    }

    @Test
    public void updateDeckWhenThereIsNoDeck() {
        when(deckDao.getDeckById(fakeId)).thenThrow(new DataAccessException(""));
        when(userDAO.getUserByEmail(user.getEmail())).thenReturn(user);

        final Response response = getPutResponse(fakeURI, deckRepresentation, encodedCredentials);

        verify(deckDao).getDeckById(fakeId);
        verify(deckDao, never()).update(any(Deck.class));
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void updateDeckWithEmptyName() {
        when(deckDao.getDeckById(deckId)).thenReturn(deck);
        when(userDAO.getUserByEmail(user.getEmail())).thenReturn(user);

        final Response response = getPutResponse(deckURI, new DeckRepresentation("", false), encodedCredentials);

        verify(deckDao, never()).getDeckById(deckId);
        verify(deckDao, never()).update(any(Deck.class));
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(422);
    }

    static private Response getPutResponse(String uri, DeckRepresentation deck, String encodedUserInfo) {
        return authResources.client()
                .target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + encodedUserInfo)
                .put(Entity.entity(deck, MediaType.APPLICATION_JSON));
    }

    @Test
    public void changeDeckAccessToTrue() {
        when(deckDao.getDeckById(deckId)).thenReturn(deck);
        String accessTrueURI = UriBuilder.fromMethod(DeckResource.class, "changeStatus").build(true).toString();

        String testURI = new StringBuilder().append(deckURI).append(accessTrueURI).toString();
        final Response response = authResources.client()
                .target(testURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(""));

        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
        verify(deckDao).update(deckCaptor.capture());
        assertThat(deckCaptor.getValue().getIsPublic()).isTrue();
    }

    @Test
    public void changeDeckAccessToFalse() {
        when(deckDao.getDeckById(deckId)).thenReturn(deck);
        String accessFalseURI = UriBuilder.fromMethod(DeckResource.class, "changeStatus").build(false).toString();

        String testURI = new StringBuilder().append(deckURI).append(accessFalseURI).toString();
        final Response response = authResources.client()
                .target(testURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(""));

        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
        verify(deckDao).update(deckCaptor.capture());
        assertThat(deckCaptor.getValue().getIsPublic()).isFalse();
    }

    @Test
    public void changeDeckAccessToUndefined() {
        when(deckDao.getDeckById(deckId)).thenReturn(deck);
        String accessUndefinedURI = UriBuilder.fromMethod(DeckResource.class, "changeStatus").build("foo").toString();

        String testURI = new StringBuilder().append(deckURI).append(accessUndefinedURI).toString();
        final Response response = authResources.client()
                .target(testURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(""));

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }
}

