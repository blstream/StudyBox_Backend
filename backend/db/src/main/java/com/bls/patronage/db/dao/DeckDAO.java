package com.bls.patronage.db.dao;

import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.mapper.DeckMapper;
import com.bls.patronage.db.mapper.DeckWithFlashcardsNumberMapper;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.DeckWithFlashcardsNumber;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RegisterMapper(DeckMapper.class)
public abstract class DeckDAO {

    @SqlQuery("select decks.id, decks.name, decks.public from decks join usersDecks on usersDecks.deckId = decks.id " +
            "where usersDecks.userId = :userId group by decks.id")
    public abstract Collection<Deck> getAllUserDecks(@Bind("userId") UUID userId);

    @RegisterMapper(DeckWithFlashcardsNumberMapper.class)
    @SqlQuery("select decks.id, decks.name, decks.public, count(flashcards.question) as count " +
            "from decks " +
            "left join flashcards " +
            "on decks.id = flashcards.deckid " +
            "inner join usersDecks on usersDecks.deckId = decks.id " +
            "where usersDecks.userId = :userId " +
            "group by decks.id")
    public abstract Collection<DeckWithFlashcardsNumber> getAllUserDecksWithFlashcardsNumber(
            @Bind("userId") UUID userId);

    @SqlUpdate("insert into decks (id, name, public) values (:id, :name, :isPublic)")
    abstract void insertDeck(@BindBean Deck deck);

    @SqlUpdate("insert into usersDecks (deckId, userId) values (:id, :userId)")
    abstract void insertUsersDeck(@BindBean Deck deck, @Bind("userId") UUID userId);

    @SqlUpdate("update decks set name = :name, public = :isPublic where id = :id")
    public abstract void update(@BindBean Deck deck);

    @SqlUpdate("delete from decks where id = :id")
    public abstract void deleteDeck(@Bind("id") UUID id);

    @SqlQuery("select id, name, public from decks where id = :id")
    abstract Deck getDeck(@Bind("id") UUID id);

    @SqlQuery("select id, name, public from decks where name like :name")
    abstract List<Deck> getDecksUsingName(@Bind("name") String name);

    @SqlQuery("select id, name, public from decks " +
            "inner join usersDecks on decks.id = usersDecks.deckId " +
            "where decks.name like :name and usersDecks.userId = :id")
    abstract List<Deck> getUserDecksUsingName(@Bind("name") String name, @Bind("id") UUID userId);

    @SqlQuery("select id, name, public from decks")
    abstract Collection<Deck> getDecks();

    @RegisterMapper(DeckWithFlashcardsNumberMapper.class)
    @SqlQuery("select decks.id, decks.name, decks.public, count(flashcards.question) as count " +
            "from decks " +
            "left join flashcards " +
            "on decks.id = flashcards.deckid " +
            "group by decks.id")
    abstract Collection<DeckWithFlashcardsNumber> getDecksWithFlashcardsNumber();


    public Collection<DeckWithFlashcardsNumber> getAllDecksWithFlashcardsNumber(UUID userId) {
        Collection<DeckWithFlashcardsNumber> decksWithFlashcardsNumber = getDecksWithFlashcardsNumber();
        decksWithFlashcardsNumber.removeAll(getAllUserDecksWithFlashcardsNumber(userId));
        return decksWithFlashcardsNumber;
    }
    public void createDeck(Deck deck, UUID userId) {
        insertDeck(deck);
        insertUsersDeck(deck, userId);
    }

    public Deck getDeckById(UUID uuid) {
        Deck deck = getDeck(uuid);
        if (deck == null) {
            throw new DataAccessException("There is no deck with specified ID");
        }
        return deck;
    }

    public Collection<Deck> getDecksByName(String name, UUID userId) {
        List<Deck> decks = getDecksUsingName(name);
        decks.removeAll(getAllUserDecks(userId));
        if (decks.isEmpty()) {
            throw new DataAccessException("There are no decks matching this name");
        }
        return decks;
    }

    public Collection<Deck> getUserDecksByName(String name, UUID userId) {
        List<Deck> decks = getUserDecksUsingName(name, userId);
        if (decks.isEmpty()) {
            throw new DataAccessException("There are no decks matching this name");
        }
        return decks;
    }

    public Collection<Deck> getAllDecks(UUID userId) {
        Collection<Deck> decks = getDecks();
        decks.removeAll(getAllUserDecks(userId));
        return decks;
    }

}
