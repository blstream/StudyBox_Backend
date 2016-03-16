package com.bls.patronage.db.dao;

import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.DeckWithFlashcardsNumber;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;
import java.util.UUID;

@RegisterMapper(DeckMapper.class)
public abstract class  DeckDAO {

    @SqlQuery("select id, name, public from decks where id = :id")
    abstract Deck getDeck(@Bind("id") UUID id);

    @SqlQuery("select id, name, public from decks where name like :name")
    abstract List<Deck> getDecksUsingName(@Bind("name") String name);

    @SqlQuery("select id, name, public from decks")
    public abstract List<Deck> getAllDecks();

    @RegisterMapper(DeckWithFlashcardsNumberMapper.class)
    @SqlQuery("select decks.id, decks.name, decks.public, count(flashcards.question) as count " +
            "from decks " +
            "left join flashcards " +
            "on decks.id = flashcards.deckid " +
            "group by decks.id")
    public abstract List<DeckWithFlashcardsNumber> getAllDecksWithFlashcardsNumber();

    @SqlUpdate("insert into decks (id, name, public) values (:id, :name, :isPublic)")
    public abstract void createDeck(@BindBean Deck deck);

    @SqlUpdate("update decks set name = :name, public = :isPublic where id = :id")
    public abstract void updateDeck(@BindBean Deck deck);

    @SqlUpdate("delete from decks where id = :id")
    public abstract void deleteDeck(@Bind("id") UUID id);


    public Deck getDeckById(UUID uuid) {
        Deck deck = getDeck(uuid);
        if(deck==null) {
            throw new DataAccessException("There is no deck with specified ID");
        }
        return deck;
    }

    public List<Deck> getDecksByName(String name) {
        List<Deck> decks = getDecksUsingName(name);
        if(decks.isEmpty()) {
            throw new DataAccessException("There are no decks matching this name");
        }
        return decks;
    }

}
