package com.bls.patronage.dao;

import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.mapper.DeckMapper;
import com.bls.patronage.db.model.Deck;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class DeckDAOTest extends DAOTest {

    private DeckDAO dao;
    private UUID defaultUserUUID;

    @Override
    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        dao = dbi.onDemand(DeckDAO.class);
        defaultUserUUID = UUID.fromString("b3f3882b-b138-4bc0-a96b-cd25e087ff4e");
    }

    private List<Deck> getDecksFromDatabase() throws Exception {
        return getAllEntities(Deck.class, DeckMapper.class, "decks");
    }

    @Override
    @AfterMethod
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void getAllDecks() throws Exception {
        List<Deck> decksFromDatabase = getDecksFromDatabase();
        List<Deck> decks = decksFromDatabase.stream().filter(Deck::getIsPublic).collect(Collectors.toList());

        assertThat(dao.getAllDecks(defaultUserUUID)).isSubsetOf(decks);
    }


    public void getAllDecksWithFlashcardsNumber() throws Exception {
//        final DeckWithFlashcardsNumber deckWithFlashcards1
//                = new DeckWithFlashcardsNumber(deckExample1.getId(),
//                "math", false, 1);
//        final DeckWithFlashcardsNumber deckWithFlashcards2
//                = new DeckWithFlashcardsNumber(deckExample2.getId(),
//                "bio", true, 0);
//
//        final DeckDAO dao = dbi.open(DeckDAO.class);
//        final Collection<DeckWithFlashcardsNumber> foundDecks = dao.getAllDecksWithFlashcardsNumber();
//        assertThat(foundDecks).contains(deckWithFlashcards1, deckWithFlashcards2);
    }


    public void getDeckById() throws Exception {
        Deck deck = getDecksFromDatabase().get(0);
        assertThat(dao.getDeckById(deck.getId())).isEqualTo(deck);
    }

    public void getUserDeckByName() throws Exception {
        Deck deck = getDecksFromDatabase().get(0);
        assertThat(dao.getUserDecksByName(deck.getName(), defaultUserUUID)).containsOnly(deck);
    }


    public void getDeckByName() throws Exception {
        Deck deck = getDecksFromDatabase().get(3);
        assertThat(dao.getDecksByName(deck.getName(), defaultUserUUID)).containsOnly(deck);
    }
/*
    public void getRandomDeck() throws Exception{
        assertThat(getDecksFromDatabase()).contains(dao.getRandomDeck(UUID.fromString("99b3b59f-6d13-4dea-b23c-827f1a67f1c3")));
    }*/

    public void createDeck() throws Exception {
        final Deck createdDeck = new Deck(UUID.randomUUID(), "foo", true);
        dao.createDeck(createdDeck, defaultUserUUID);
        assertThat(dao.getDeckById(createdDeck.getId())).isEqualTo(createdDeck);
    }

    public void deleteDeck() throws Exception {
        Deck deck = getDecksFromDatabase().get(0);
        dao.deleteDeck(deck.getId());
        assertThat(getDecksFromDatabase()).doesNotContain(deck);
    }

    public void updateDeck() throws Exception {
        Deck deck = getDecksFromDatabase().get(0);
        Deck updatedDeck = new Deck(deck.getId(), "foo", true);
        dao.update(updatedDeck);

        assertThat(getDecksFromDatabase()).doesNotContain(deck);
        assertThat(getDecksFromDatabase()).contains(updatedDeck);
    }
}
