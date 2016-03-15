package com.bls.patronage.dao;

import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.model.Deck;
import org.junit.Test;
import org.skife.jdbi.v2.Handle;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DeckDAOTest extends DAOTest {

    private Deck deckExample1;
    private Deck deckExample2;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        deckExample1 = new Deck(UUID.fromString("020276e9-285c-4162-b585-32f272a947b1"),"math");
        deckExample2 = new Deck(UUID.fromString("b5e9aa75-64d6-4d21-87a6-d0a91c70f997"),"bio");
    }

    @Override
    protected void setUpDatabaseContent(Handle handle) {
        handle.createCall("DROP TABLE decks IF EXISTS").invoke();
        handle.createCall(
                "CREATE TABLE decks (id uuid primary key, name varchar(50) not null)")
                .invoke();
        handle.createStatement("INSERT INTO decks VALUES (?, ?)")
                .bind(0, "020276e9-285c-4162-b585-32f272a947b1")
                .bind(1, "math")
                .execute();
        handle.createStatement("INSERT INTO decks VALUES (?, ?)")
                .bind(0, "b5e9aa75-64d6-4d21-87a6-d0a91c70f997")
                .bind(1, "bio")
                .execute();
    }
    @Test
    public void getAllDecks(){
        final DeckDAO dao = dbi.open(DeckDAO.class);
        final List<Deck> decks = dao.getAllDecks();
        assertThat(decks).containsSequence(deckExample1,deckExample2);
    }

    @Test
    public void getDeckById(){
        final DeckDAO dao = dbi.open(DeckDAO.class);
        final Deck foundDeck = dao.getDeckById(deckExample1.getId());
        assertThat(foundDeck).isEqualTo(deckExample1);
    }

    @Test
    public void getDeckByName(){
        final DeckDAO dao = dbi.open(DeckDAO.class);
        final List<Deck> decks = dao.getDecksByName(deckExample1.getName());
        assertThat(decks).containsOnly(deckExample1);
    }

    @Test
    public void createDeck(){
        final Deck createdDeck = new Deck(UUID.fromString("a04692bc-4a70-4696-9815-24b8c0de5398"),"sport");
        final DeckDAO dao = dbi.open(DeckDAO.class);
        dao.createDeck(createdDeck);
        assertThat(dao.getDeckById(createdDeck.getId())).isEqualTo(createdDeck);
    }

    @Test
    public void deleteDeck(){
        final DeckDAO dao = dbi.open(DeckDAO.class);
        dao.deleteDeck(deckExample2.getId());
        assertThat(dao.getDeckById(deckExample2.getId())).isNull();
    }

    @Test
    public void updateDeck(){
        final Deck updatedDeck = new Deck(deckExample1.getId(),"miscellanous");
        final DeckDAO dao = dbi.open(DeckDAO.class);
        dao.updateDeck(updatedDeck);
        assertThat(dao.getDeckById(deckExample1.getId())).isEqualTo(updatedDeck);
    }
}
