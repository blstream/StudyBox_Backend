package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.DeckWithFlashcardsNumber;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DecksResourceTest extends BasicAuthenticationTest {

    @Captor
    private ArgumentCaptor<Deck> deckCaptor;
    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;
    private DeckRepresentation deck;
    private List<DeckRepresentation> decksRepresentations;
    private List<Deck> decks;
    private DeckRepresentation userDeck;
    private List<DeckRepresentation> userDecksRepresentations;
    private List<Deck> userDecks;
    private String decksURI;
    private String decksByNameURI;
    private String decksByBadNameURI;
    private String decksWithFlashcardNumberURI;
    private String userDecksWithFlashcardNumberURI;
    private String decksByEmptyNameURI;
    private String userDecksURI;
    private String randomDeckURI;

    static private Response postDeck(String uri, String name, Boolean isPublic, String encodedUserInfo) {
        return authResources.client().target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + encodedUserInfo)
                .post(Entity.entity(new DeckRepresentation(name, isPublic), MediaType.APPLICATION_JSON_TYPE));
    }

    static private List<DeckRepresentation> getListFromResponse(String uri, String encodedUserInfo) {

        final Response response = getResponseWithCredentials(uri, encodedUserInfo);
        return response.readEntity(new GenericType<List<DeckRepresentation>>() {
        });
    }

    @Before
    public void setUp() {
        super.setUp();
        deck = new DeckRepresentation("foo", false).setId(UUID.randomUUID());
        decksRepresentations = Collections.singletonList(deck);
        decks = Collections.singletonList(deck.map());
        userDeck = new DeckRepresentation("baz", false).setId(UUID.randomUUID());
        userDecksRepresentations = Collections.singletonList(userDeck);
        userDecks = Collections.singletonList(userDeck.map());
        decksURI = UriBuilder.fromResource(DecksResource.class).build().toString();
        randomDeckURI = UriBuilder.fromResource(DecksResource.class)
                .queryParam("random", true).build().toString();
        decksByNameURI = UriBuilder.fromResource(DecksResource.class)
                .queryParam("name", deck.getName()).build().toString();
        decksByBadNameURI = UriBuilder.fromResource(DecksResource.class)
                .queryParam("name", "anotherThing").build().toString();
        decksByEmptyNameURI = UriBuilder.fromResource(DecksResource.class)
                .queryParam("name", "").build().toString();
        decksWithFlashcardNumberURI = UriBuilder.fromResource(DecksResource.class)
                .queryParam("isEnabled", true).build().toString();
        userDecksWithFlashcardNumberURI = UriBuilder.fromResource(DecksResource.class).build().toString()
                + UriBuilder.fromMethod(DecksResource.class, "listMyDecks")
                .queryParam("isEnabled", true).build().toString();
        userDecksURI = UriBuilder.fromResource(DecksResource.class).build().toString()
                + UriBuilder.fromMethod(DecksResource.class, "listMyDecks").build().toString();

        when(deckDao.getAllDecks()).thenReturn(decks);
        when(deckDao.getAllUserDecks(user.getId())).thenReturn(userDecks);
        when(deckDao.getDeckById(deck.getId(), user.getId())).thenReturn(deck.map());
        when(deckDao.getDeckById(userDeck.getId(), user.getId())).thenReturn(userDeck.map());
        when(deckDao.getUserDecksByName(any(String.class), eq(userDeck.getId()))).thenReturn(userDecks);
        when(deckDao.getDecksByName(deck.getName())).thenReturn(decks);

        when(userDAO.getUserByEmail(user.getEmail())).thenReturn(user);
        when(userDAO.getUserById(user.getId())).thenReturn(user);
    }

    @Test
    public void createDeck() {
        final Response response = postDeck(decksURI, deck.getName(), deck.isPublicVisible(), encodedCredentials);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CREATED);
        verify(deckDao).createDeck(deckCaptor.capture(), uuidCaptor.capture());
        assertThat(deckCaptor.getValue().getId()).isNotNull();
        assertThat(deckCaptor.getValue().getName()).isEqualTo(deck.getName());
        assertThat(uuidCaptor.getValue()).isEqualTo(user.getId());
    }

    @Test
    public void createDeckWithoutName() {
        final Response response = postDeck(decksURI, "", false, encodedCredentials);
        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void createDeckWithTooLongName() {
        final Response response = postDeck(decksURI, RandomStringUtils.random(51), false, encodedCredentials);
        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void listUserDecks() {
        final List<DeckRepresentation> response = getListFromResponse(userDecksURI, encodedCredentials);

        verify(deckDao).getAllUserDecks(user.getId());
        assertThat(response).containsAll(userDecksRepresentations);
    }

    @Test
    public void listDecks() {
        final List<DeckRepresentation> response = getListFromResponse(decksURI, encodedCredentials);

        verify(deckDao).getAllDecks();
        assertThat(response).containsAll(decksRepresentations);
    }

    @Test
    public void listDecksByNames() {
        final List<DeckRepresentation> response = getListFromResponse(decksByNameURI, encodedCredentials);

        verify(deckDao).getDecksByName(deck.getName());
        assertThat(response).containsAll(decksRepresentations);
    }

    @Test
    public void listDecksByNamesWhenThereIsBadNameTyped() {
        when(deckDao.getDecksByName("anotherThing")).thenThrow(new DataAccessException(""));

        final Response response = getResponseWithCredentials(decksByBadNameURI, encodedCredentials);
        verify(deckDao).getDecksByName("anotherThing");
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void listDecksByNamesWhenThereIsNoNameTyped() {
        when(deckDao.getDecksByName("")).thenThrow(new DataAccessException(""));

        final Response response = getResponseWithCredentials(decksByEmptyNameURI, encodedCredentials);
        verify(deckDao).getDecksByName("");
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void listUserDecksWithFlashcardsNumber() {
        final DeckWithFlashcardsNumber deckExample = new DeckWithFlashcardsNumber(userDeck.map(), 3);
        when(deckDao.getFlashcardsCount(deckExample.getId()))
                .thenReturn(deckExample.getCount());

        final ImmutableList<DeckRepresentation> decks
                = ImmutableList.of(new DeckRepresentation(deckExample)
                .setFlashcardsCount(deckExample.getCount()));
        final Response response = getResponseWithCredentials(userDecksWithFlashcardNumberURI, encodedCredentials);
        final List<DeckRepresentation> decksInResponse = response
                .readEntity(new GenericType<List<DeckRepresentation>>() {
                });

        verify(deckDao).getAllUserDecks(user.getId());
        verify(userDAO).getUserByEmail(user.getEmail());
        verify(deckDao).getFlashcardsCount(deckExample.getId());

        assertThat(decksInResponse).containsAll(decks);
        assertThat(decksInResponse.get(0).getFlashcardsCount()).isEqualTo(deckExample.getCount());
    }

    @Test
    public void listDecksWithBadPassword() {
        final Response response = getResponseWithCredentials(decksURI, badPasswordCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        verify(userDAO).getUserByEmail(user.getEmail());
    }

    @Test
    public void listDecksWithBadEmail() {
        when(userDAO.getUserByEmail(fakeEmail))
                .thenThrow(new DataAccessException(""));
        final Response response = getResponseWithCredentials(decksURI, badEmailCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        verify(userDAO).getUserByEmail(fakeEmail);
    }

    @Test
    public void createDeckWithBadPassword() {
        final Response response = postDeck(decksURI, deck.getName(), deck.isPublicVisible(), badPasswordCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        verify(userDAO).getUserByEmail(user.getEmail());
    }

    @Test
    public void createDeckWithBadEmail() {
        when(userDAO.getUserByEmail(fakeEmail))
                .thenThrow(new DataAccessException(""));
        final Response response = postDeck(decksURI, deck.getName(), deck.isPublicVisible(), badEmailCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        verify(userDAO).getUserByEmail(fakeEmail);
    }

    @Test
    public void getRandomDeck() {
        final List<DeckRepresentation> response = getListFromResponse(randomDeckURI, encodedCredentials);

        verify(deckDao).getRandomDecks(user.getId());
        assertThat(decksRepresentations).containsAll(response);
    }

}
