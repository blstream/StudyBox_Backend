package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.model.AuditableEntity;
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
import java.sql.Timestamp;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeckResourceTest extends BasicAuthenticationTest {

    @Captor
    private ArgumentCaptor<Deck> deckCaptor;
    private DeckRepresentation deck;
    private UUID deckId;
    private UUID fakeId;
    private String deckURI;
    private String fakeURI;
    private AuditableEntity auditEntity;


    static private Response getPutResponse(String uri,
                                           DeckRepresentation deck,
                                           String encodedUserInfo) {
        return authResources.client()
                .target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + encodedUserInfo)
                .put(Entity.entity(deck, MediaType.APPLICATION_JSON));
    }

    static private Response getPostResponse(String uri, String encodedUserInfo) {
        return authResources.client().target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + encodedUserInfo)
                .post(Entity.json(""));
    }

    @Before
    public void setUp() {
        super.setUp();
        deckId = UUID.fromString("a04692bc-4a70-4696-9815-24b8c0de5398");
        fakeId = UUID.fromString("12345678-9012-3456-7890-123456789012");
        auditEntity = new AuditableEntity(deckId,
                new Timestamp(new Long("1461219791000")),
                new Timestamp(new Long("1463234622000")),
                user.getId().toString(),
                user.getId().toString());
        deck = new DeckRepresentation.DeckRepresentationBuilder("biology", false).withId(deckId).withAuditFields(auditEntity).build();
        deckURI = UriBuilder.fromResource(DeckResource.class).build(deckId).toString();
        fakeURI = UriBuilder.fromResource(DeckResource.class).build(fakeId).toString();

        when(deckDao.getDeckById(deckId, user.getId())).thenReturn(deck.map());
        when(deckDao.getDeckById(fakeId, user.getId())).thenThrow(new DataAccessException(""));
        when(userDAO.getUserByEmail(user.getEmail())).thenReturn(user);
        when(deckDao.getDeckAuditFields(deckId)).thenReturn(auditEntity);
    }

    @Test
    public void getDeckSuccess() {
        final Response response = getResponseWithCredentials(deckURI, encodedCredentials);
        final DeckRepresentation found = response.readEntity(DeckRepresentation.class);

        assertThat(found).isEqualTo(deck);
    }

    @Test
    public void getDeckNotFound() {
        final Response response = getResponseWithCredentials(fakeURI, encodedCredentials);

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void getNonAccessibleDeck() {
        when(deckDao.getDeckById(deckId, user.getId()))
                .thenThrow(new DataAccessException("",Response.Status.FORBIDDEN.getStatusCode()));

        final Response response = getResponseWithCredentials(deckURI, encodedCredentials);

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void deleteDeck() {
        final Response response = authResources.client()
                .target(deckURI).request().header("Authorization", "Basic " + encodedCredentials)
                .delete();

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
        verify(deckDao).deleteDeck(any(UUID.class));
    }

    @Test
    public void deleteDeckWhenThereIsNoDeck() {
        final Response response = authResources.client().target(fakeURI)
                .request().header("Authorization", "Basic " + encodedCredentials)
                .delete();

        verify(deckDao).getDeckById(fakeId, user.getId());
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        verify(deckDao, never()).deleteDeck(any(UUID.class));
    }

    @Test
    public void updateDeck() {
        final Response response = getPutResponse(deckURI,
                new DeckRepresentation.DeckRepresentationBuilder(deck.getName(), deck.getIsPublic()).build(),
                encodedCredentials);

        final DeckRepresentation updatedDeck = response.readEntity(DeckRepresentation.class);
        verify(deckDao).updateDeck(updatedDeck.map(), user.getId());
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(updatedDeck.getId()).isEqualTo(deckId);
        assertThat(updatedDeck.getName()).isEqualTo(deck.getName());
    }

    @Test
    public void updateDeckWhenThereIsNoDeck() {
        final Response response = getPutResponse(fakeURI,
                new DeckRepresentation.DeckRepresentationBuilder(deck.getName(), deck.getIsPublic()).build(),
                encodedCredentials);

        verify(deckDao).getDeckById(fakeId, user.getId());
        verify(deckDao, never()).updateDeck(any(Deck.class), eq(user.getId()));
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void updateDeckWithEmptyName() {
        final Response response = getPutResponse(deckURI,
                new DeckRepresentation.DeckRepresentationBuilder("", false).build(),
                encodedCredentials);

        verify(deckDao, never()).updateDeck(any(Deck.class), eq(user.getId()));
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(422);
    }

    @Test
    public void changeDeckAccessToTrue() {
        String accessTrueURI = UriBuilder.fromMethod(DeckResource.class, "changeStatus")
                .build(true).toString();

        String testURI = new StringBuilder().append(deckURI).append(accessTrueURI).toString();
        final Response response = getPostResponse(testURI, encodedCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
        verify(deckDao).updateDeck(deckCaptor.capture(), eq(user.getId()));
        assertThat(deckCaptor.getValue().getIsPublic()).isTrue();
    }

    @Test
    public void changeDeckAccessToFalse() {
        String accessFalseURI = UriBuilder.fromMethod(DeckResource.class, "changeStatus").build(false).toString();

        String testURI = new StringBuilder().append(deckURI).append(accessFalseURI).toString();
        final Response response = getPostResponse(testURI, encodedCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
        verify(deckDao).updateDeck(deckCaptor.capture(), eq(user.getId()));
        assertThat(deckCaptor.getValue().getIsPublic()).isFalse();
    }

    @Test
    public void changeDeckAccessToUndefined() {
        String accessUndefinedURI = UriBuilder.fromMethod(DeckResource.class, "changeStatus").build("foo").toString();

        String testURI = new StringBuilder().append(deckURI).append(accessUndefinedURI).toString();
        final Response response = getPostResponse(testURI, encodedCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }
}
