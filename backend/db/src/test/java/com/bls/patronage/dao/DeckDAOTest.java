package com.bls.patronage.dao;

import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.mapper.DeckMapper;
import com.bls.patronage.db.mapper.FlashcardMapper;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.Flashcard;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class DeckDAOTest extends DAOTest {

    private DeckDAO dao;
    private UUID defaultUserUUID;

    @Override
    @BeforeTest
    public void buildDatabase() {
        super.buildDatabase();
        dao = dbi.onDemand(DeckDAO.class);
        defaultUserUUID = UUID.fromString("b3f3882b-b138-4bc0-a96b-cd25e087ff4e");
    }

    @Override
    @BeforeMethod
    public void loadContent() throws Exception {
        super.loadContent();
    }

    private List<Deck> getDecksFromDatabase() throws Exception {
        return getAllEntities(Deck.class, DeckMapper.class, "decks");
    }

    @Override
    @AfterTest
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void getPublicDecks() throws Exception {
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
        assertThat(dao.getDeckById(deck.getId(), defaultUserUUID)).isEqualTo(deck);
    }

    public void getUserDeckByName() throws Exception {
        Deck deck = getDecksFromDatabase().get(0);
        assertThat(dao.getUserDecksByName(deck.getName(), defaultUserUUID)).containsOnly(deck);
    }

    public void getUsersDeckByPartOfTheName() throws Exception {
        Deck deck = getDecksFromDatabase().get(0);
        assertThat(dao.getUserDecksByName(deck.getName().substring(0,2), defaultUserUUID)).contains(deck);
    }

    public void getDeckByName() throws Exception {
        Deck deck = getDecksFromDatabase().get(3);
        assertThat(dao.getDecksByName(deck.getName(), defaultUserUUID)).containsOnly(deck);
    }

    public void getOwnDeckByName() throws Exception {
        Deck deck = getDecksFromDatabase().get(0);
        assertThat(dao.getDecksByName(deck.getName(), defaultUserUUID)).isEmpty();
    }

    public void getDeckByPartOfTheName() throws Exception {
        Deck deck = getDecksFromDatabase().get(3);
        assertThat(dao.getDecksByName(deck.getName().substring(0,2), defaultUserUUID)).contains(deck);
    }

    public void getRandomDeck() throws Exception {
        List<Deck> decksFromDatabase = getDecksFromDatabase();
        assertThat(decksFromDatabase).contains(dao.getRandomDeck(defaultUserUUID));
    }

    public void createDeck() throws Exception {
        final Deck createdDeck = new Deck(UUID.randomUUID(), "foo", true);
        dao.createDeck(createdDeck, defaultUserUUID);
        assertThat(dao.getDeckById(createdDeck.getId(), defaultUserUUID)).isEqualTo(createdDeck);
    }

    public void deleteDeck() throws Exception {
        Deck deck = getDecksFromDatabase().get(0);
        dao.deleteDeck(deck.getId());
        assertThat(getDecksFromDatabase()).doesNotContain(deck);
    }

    public void updateDeck() throws Exception {
        Deck deck = getDecksFromDatabase().get(0);
        Deck updatedDeck = new Deck(deck.getId(), "foo", true);
        dao.updateDeck(updatedDeck, defaultUserUUID);

        assertThat(getDecksFromDatabase()).doesNotContain(deck);
        assertThat(getDecksFromDatabase()).contains(updatedDeck);
    }

    public void getFlashcardsNumber() throws Exception {
        final List<Flashcard> flashcards = getAllEntities(Flashcard.class, FlashcardMapper.class, "flashcards");
        UUID deckId = flashcards.get(0).getDeckId();
        List<Flashcard> flashcardsInOneDeck = flashcards
                .stream()
                .filter(flashcard -> flashcard.getDeckId()
                        .equals(deckId)).collect(Collectors.toList());

        assertThat(dao.getFlashcardsCount(deckId)).isEqualTo(flashcardsInOneDeck.size());
    }
}
