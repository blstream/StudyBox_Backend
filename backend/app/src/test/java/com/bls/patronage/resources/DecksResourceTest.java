package com.bls.patronage.resources;

import com.bls.patronage.api.DeckRepresentation;
import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.model.AuditableEntity;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private DeckRepresentation deck2;
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
    private String randomDeckWithFlashcardsCountURI;
    private AuditableEntity auditEntity;
    private AuditableEntity auditEntity2;
    private AuditableEntity userDeckAuditEntity;
    private UUID deckId;
    private UUID deck2Id;
    private UUID userDeckId;

    static private Response postDeck(String uri, String name, Boolean isPublic, String encodedUserInfo) {
        return authResources.client().target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + encodedUserInfo)
                .post(Entity.entity(new DeckRepresentation.DeckRepresentationBuilder(name, isPublic).build(),
                        MediaType.APPLICATION_JSON_TYPE));
    }

    static private List<DeckRepresentation> getListFromResponse(String uri, String encodedUserInfo) {

        final Response response = getResponseWithCredentials(uri, encodedUserInfo);
        return response.readEntity(new GenericType<List<DeckRepresentation>>() {
        });
    }

    @Before
    public void setUp() {
        super.setUp();
        deckId=UUID.randomUUID();
        deck2Id=UUID.randomUUID();
        userDeckId=UUID.randomUUID();
        auditEntity = new AuditableEntity(deckId,
                new Timestamp(new Long("1461219791000")),
                new Timestamp(new Long("1463234622000")),
                user.getId().toString(),
                user.getId().toString());
        auditEntity2 = new AuditableEntity(deck2Id,
                new Timestamp(new Long("1464502991000")),
                new Timestamp(new Long("1464592375000")),
                user.getId().toString(),
                user.getId().toString());
        deck = new DeckRepresentation.DeckRepresentationBuilder("foo", false)
                .withId(UUID.randomUUID())
                .withCreationDate("2016-04-21 08:23:11.0")
                .withCreatorEmail("test@mail.com")
                .withAuditFields(auditEntity)
                .build();
        deck2 = new DeckRepresentation.DeckRepresentationBuilder("bar", false)
                .withId(UUID.randomUUID())
                .withCreationDate("2016-05-29 08:23:11.0")
                .withCreatorEmail("test@mail.com")
                .withAuditFields(auditEntity2)
                .build();
        decksRepresentations = new ArrayList<>();
        decksRepresentations.add(deck);
        decksRepresentations.add(deck2);
        decks = new ArrayList<>();
        decks.add(deck.map());
        decks.add(deck2.map());
        userDeckAuditEntity = new AuditableEntity(userDeckId,
                new Timestamp(new Long("1462185942000")),
                new Timestamp(new Long("1462293942000")),
                user.getId().toString(),
                user.getId().toString());
        userDeck = new DeckRepresentation.DeckRepresentationBuilder("baz", false)
                .withId(UUID.randomUUID())
                .withAuditFields(userDeckAuditEntity)
                .build();
        userDecksRepresentations = Collections.singletonList(userDeck);
        userDecks = Collections.singletonList(userDeck.map());
        decksURI = UriBuilder.fromResource(DecksResource.class).build().toString();
        randomDeckURI = UriBuilder.fromResource(DecksResource.class).build().toString()
                + UriBuilder.fromMethod(DecksResource.class, "getRandomDeck");
        randomDeckWithFlashcardsCountURI = UriBuilder.fromResource(DecksResource.class).build().toString()
                + UriBuilder.fromMethod(DecksResource.class, "getRandomDeck")
                .queryParam("flashcardsCount", true);
        decksByNameURI = UriBuilder.fromResource(DecksResource.class)
                .queryParam("name", deck.getName()).build().toString();
        decksByBadNameURI = UriBuilder.fromResource(DecksResource.class)
                .queryParam("name", "anotherThing").build().toString();
        decksByEmptyNameURI = UriBuilder.fromResource(DecksResource.class)
                .queryParam("name", "").build().toString();
        decksWithFlashcardNumberURI = UriBuilder.fromResource(DecksResource.class)
                .queryParam("flashcardsCount", true).build().toString();
        userDecksWithFlashcardNumberURI = UriBuilder.fromResource(DecksResource.class).build().toString()
                + UriBuilder.fromMethod(DecksResource.class, "listMyDecks")
                .queryParam("flashcardsCount", true).build().toString();
        userDecksURI = UriBuilder.fromResource(DecksResource.class).build().toString()
                + UriBuilder.fromMethod(DecksResource.class, "listMyDecks").build().toString();

        when(deckDao.getAllDecks(user.getId())).thenReturn(decks);
        when(deckDao.getDeckCreationDate(deck.getId())).thenReturn(deck.getCreationDate());
        when(deckDao.getDeckCreationDate(deck2.getId())).thenReturn(deck2.getCreationDate());
        when(deckDao.getAllUserDecks(user.getId())).thenReturn(userDecks);
        when(deckDao.getDeckById(deck.getId(), user.getId())).thenReturn(deck.map());
        when(deckDao.getDeckById(userDeck.getId(), user.getId())).thenReturn(userDeck.map());
        when(deckDao.getUserDecksByName(any(String.class), eq(userDeck.getId()))).thenReturn(userDecks);
        when(deckDao.getDecksByName(deck.getName(), user.getId())).thenReturn(decks);
        when(deckDao.getRandomDeck(user.getId())).thenReturn(deck.map());
        when(userDAO.getUserByEmail(user.getEmail())).thenReturn(user);
        when(userDAO.getUserById(user.getId())).thenReturn(user);
        when(deckDao.getDeckAuditFields(deck.getId())).thenReturn(auditEntity);
        when(deckDao.getDeckAuditFields(deck2.getId())).thenReturn(auditEntity2);
        when(deckDao.getDeckAuditFields(userDeck.getId())).thenReturn(userDeckAuditEntity);
        when(deckDao.getCreatorEmailFromDeckId(deck.map().getId())).thenReturn(deck.getCreatorEmail());
        when(deckDao.getCreatorEmailFromDeckId(deck2.map().getId())).thenReturn(deck2.getCreatorEmail());
    }

    @Test
    public void createDeck() {
        final Response response = postDeck(decksURI, deck.getName(), deck.getIsPublic(), encodedCredentials);

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
        userDecksRepresentations
                .sort(Comparator.comparing(DeckRepresentation::getCreationDate, Comparator.reverseOrder()));
        assertThat(response.get(0)).isEqualTo(userDecksRepresentations.get(0));
    }

    @Test
    public void listDecks() {
        final List<DeckRepresentation> response = getListFromResponse(decksURI, encodedCredentials);

        verify(deckDao).getAllDecks(user.getId());
        assertThat(response).containsAll(decksRepresentations);
        decksRepresentations
                .sort(Comparator.comparing(DeckRepresentation::getCreationDate, Comparator.reverseOrder()));
        assertThat(response.get(0)).isEqualTo(decksRepresentations.get(0));
    }

    @Test
    public void listDecksWithoutCreationDate() {
        when(deckDao.getDeckCreationDate(deck.getId())).thenReturn(null);
        final List<DeckRepresentation> response = getListFromResponse(decksURI, encodedCredentials);

        verify(deckDao).getAllDecks(user.getId());
        assertThat(response).isNotNull();
    }

    @Test
    public void listDecksByNames() {
        final List<DeckRepresentation> response = getListFromResponse(decksByNameURI, encodedCredentials);

        verify(deckDao).getDecksByName(deck.getName(), user.getId());
        assertThat(response).containsAll(decksRepresentations);
        decksRepresentations
                .sort(Comparator.comparing(DeckRepresentation::getCreationDate, Comparator.reverseOrder()));
        assertThat(response.get(0)).isEqualTo(decksRepresentations.get(0));
    }

    @Test
    public void listDecksByNamesWhenThereIsBadNameTyped() {
        when(deckDao.getDecksByName("anotherThing", user.getId())).thenThrow(new DataAccessException(""));

        final Response response = getResponseWithCredentials(decksByBadNameURI, encodedCredentials);
        verify(deckDao).getDecksByName("anotherThing", user.getId());
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void listDecksByNamesWhenThereIsNoNameTyped() {
        when(deckDao.getDecksByName("", user.getId())).thenThrow(new DataAccessException(""));

        final Response response = getResponseWithCredentials(decksByEmptyNameURI, encodedCredentials);
        verify(deckDao).getDecksByName("", user.getId());
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void listUserDecksWithFlashcardsNumber() {
        final DeckWithFlashcardsNumber deckExample = new DeckWithFlashcardsNumber(userDeck.map(), 3);
        when(deckDao.getFlashcardsCount(deckExample.getId()))
                .thenReturn(deckExample.getCount());

        final ImmutableList<DeckRepresentation> decks
                = ImmutableList.of(new DeckRepresentation.DeckRepresentationBuilder(deckExample)
                .withFlashcardsCount(deckExample.getCount())
                .withAuditFields(userDeckAuditEntity)
                .build());
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
        final Response response = postDeck(decksURI, deck.getName(), deck.getIsPublic(), badPasswordCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        verify(userDAO).getUserByEmail(user.getEmail());
    }

    @Test
    public void createDeckWithBadEmail() {
        when(userDAO.getUserByEmail(fakeEmail))
                .thenThrow(new DataAccessException(""));
        final Response response = postDeck(decksURI, deck.getName(), deck.getIsPublic(), badEmailCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        verify(userDAO).getUserByEmail(fakeEmail);
    }

    @Test
    public void getRandomDeck() {
        final Response response = getResponseWithCredentials(randomDeckURI, encodedCredentials);
        final DeckRepresentation deck = response.readEntity(DeckRepresentation.class);

        verify(deckDao).getRandomDeck(user.getId());
        assertThat(decksRepresentations).contains(deck);
    }

    @Test
    public void getRandomDeckWithFlashcardsCount() {
        final Response response = getResponseWithCredentials(randomDeckWithFlashcardsCountURI, encodedCredentials);
        final DeckRepresentation deck = response.readEntity(DeckRepresentation.class);

        verify(deckDao).getRandomDeck(user.getId());
        assertThat(deck.getFlashcardsCount()).isNotNull();
    }
}
