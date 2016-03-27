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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DecksResourceTest extends BasicAuthenticationTest {

    @Captor
    private ArgumentCaptor<Deck> deckCaptor;
    private Deck deck;
    private String decksURI;
    private String decksByNameURI;
    private String decksByBadNameURI;
    private String decksWithFlashcardNumberURI;
    private String decksByEmptyNameURI;
    private String randomDeckURI;

    @Before
    public void setUp() {
        super.setUp();
        deck = new Deck("12345678-9012-3456-7890-123456789012", "math");
        decksURI = UriBuilder.fromResource(DecksResource.class).build().toString();
        decksByNameURI = UriBuilder.fromResource(DecksResource.class)
                .queryParam("name", "something").build().toString();
        decksByBadNameURI = UriBuilder.fromResource(DecksResource.class)
                .queryParam("name", "anotherThing").build().toString();
        decksByEmptyNameURI = UriBuilder.fromResource(DecksResource.class)
                .queryParam("name", "").build().toString();
        decksWithFlashcardNumberURI = UriBuilder.fromResource(DecksResource.class)
                .queryParam("isEnabled", true).build().toString();
        randomDeckURI = UriBuilder.fromResource(DecksResource.class)
                .queryParam("random", true).build().toString();
    }

    @Test
    public void createDeck() {
        when(dao.getUserByEmail(user.getEmail())).thenReturn(user);
        final Response response = postDeck(decksURI, deck.getName(), deck.getIsPublic(), encodedCredentials);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(deckDao).createDeck(deckCaptor.capture());
        assertThat(deckCaptor.getValue().getId()).isNotNull();
        assertThat(deckCaptor.getValue().getName()).isEqualTo(deck.getName());
    }

    @Test
    public void createDeckWithoutName() {
        when(dao.getUserByEmail(user.getEmail())).thenReturn(user);
        final Response response = postDeck(decksURI, "", false, encodedCredentials);
        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void createDeckWithTooLongName() {
        when(dao.getUserByEmail(user.getEmail())).thenReturn(user);
        final Response response = postDeck(decksURI, RandomStringUtils.random(51), false, encodedCredentials);
        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void listDecks() {
        final ImmutableList<Deck> decks = ImmutableList.of(deck);
        when(deckDao.getAllDecks()).thenReturn(decks);
        when(dao.getUserByEmail(user.getEmail())).thenReturn(user);

        final List<Deck> response = getListFromResponse(decksURI, encodedCredentials);

        verify(deckDao).getAllDecks();
        assertThat(response).containsAll(decks);
    }

    @Test
    public void listDecksByNames() {
        final ImmutableList<Deck> decks = ImmutableList.of(deck);
        when(dao.getUserByEmail(user.getEmail())).thenReturn(user);
        when(deckDao.getDecksByName("something")).thenReturn(decks);

        final List<Deck> response = getListFromResponse(decksByNameURI, encodedCredentials);

        verify(deckDao).getDecksByName("something");
        assertThat(response).containsAll(decks);
    }

    @Test
    public void listDecksByNamesWhenThereIsBadNameTyped() {
        when(deckDao.getDecksByName("anotherThing")).thenReturn(null);
        when(dao.getUserByEmail(user.getEmail())).thenReturn(user);

        final List<Deck> response = getListFromResponse(decksByBadNameURI, encodedCredentials);

        verify(deckDao).getDecksByName("anotherThing");
        assertThat(response).isNull();
    }

    @Test
    public void listDecksByNamesWhenThereIsNoNameTyped() {
        when(deckDao.getDecksByName("")).thenReturn(null);
        when(dao.getUserByEmail(user.getEmail())).thenReturn(user);

        final List<Deck> response = getListFromResponse(decksByEmptyNameURI, encodedCredentials);

        verify(deckDao).getDecksByName("");
        assertThat(response).isNull();
    }

    @Test
    public void listDecksWithFlashcardsNumber() {
        final DeckWithFlashcardsNumber deckExample = new DeckWithFlashcardsNumber(UUID.randomUUID(),
                "math", true, 3);
        final ImmutableList<DeckWithFlashcardsNumber> decks = ImmutableList.of(deckExample);
        when(deckDao.getAllDecksWithFlashcardsNumber()).thenReturn(decks);
        when(dao.getUserByEmail(user.getEmail())).thenReturn(user);

        final Response response = getResponseWithCredentials(decksWithFlashcardNumberURI, encodedCredentials);
        final List<DeckWithFlashcardsNumber> decksInResponse = response
                .readEntity(new GenericType<List<DeckWithFlashcardsNumber>>() {
        });

        verify(deckDao).getAllDecksWithFlashcardsNumber();
        assertThat(decksInResponse).containsAll(decks);
        assertThat(decksInResponse.get(0).getCount()).isNotNull();
    }

    @Test
    public void listDecksWithBadPassword() {
        when(dao.getUserByEmail(user.getEmail())).thenReturn(user);
        final Response response = getResponseWithCredentials(decksURI, badPasswordCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        verify(dao).getUserByEmail(user.getEmail());
    }

    @Test
    public void listDecksWithBadEmail() {
        when(dao.getUserByEmail(fakeEmail))
                .thenThrow(new DataAccessException(""));
        final Response response = getResponseWithCredentials(decksURI, badEmailCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        verify(dao).getUserByEmail(fakeEmail);
    }

    @Test
    public void randomDeck(){
        when(deckDao.getRandomDeck()).thenReturn(deck);
        when(dao.getUserByEmail(user.getEmail())).thenReturn(user);

        final List<Deck> response = getListFromResponse(randomDeckURI, encodedCredentials);
        final Deck found = response.get(0);

        verify(deckDao).getRandomDeck();
        assertThat(found).isEqualTo(deck);
    }


    @Test
    public void createDeckWithBadPassword() {
        when(dao.getUserByEmail(user.getEmail())).thenReturn(user);
        final Response response = postDeck(decksURI, deck.getName(), deck.getIsPublic(), badPasswordCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        verify(dao).getUserByEmail(user.getEmail());
    }

    @Test
    public void createDeckWithBadEmail() {
        when(dao.getUserByEmail(fakeEmail))
                .thenThrow(new DataAccessException(""));
        final Response response = postDeck(decksURI, deck.getName(), deck.getIsPublic(), badEmailCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        verify(dao).getUserByEmail(fakeEmail);
    }

    static private Response postDeck(String uri, String name, Boolean isPublic, String encodedUserInfo) {
        return authResources.client().target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + encodedUserInfo)
                .post(Entity.entity(new DeckRepresentation(name, isPublic), MediaType.APPLICATION_JSON_TYPE));
    }

    static private List<Deck> getListFromResponse(String uri, String encodedUserInfo) {

        final Response response = getResponseWithCredentials(uri, encodedUserInfo);
        return response.readEntity(new GenericType<List<Deck>>() {
        });
    }
}
