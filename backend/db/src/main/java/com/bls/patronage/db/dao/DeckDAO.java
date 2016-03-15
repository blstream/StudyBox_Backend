package com.bls.patronage.db.dao;

import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.DeckWithFlashcardsNumber;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;
import java.util.UUID;

@RegisterMapper(DeckMapper.class)
public interface DeckDAO {

    @SqlQuery("select id, name, public from decks where id = :id")
    Deck getDeckById(@Bind("id") UUID id);

    @SqlQuery("select id, name, public from decks")
    List<Deck> getAllDecks();

    @RegisterMapper(DeckWithFlashcardsNumberMapper.class)
    @SqlQuery("select decks.id, decks.name, decks.public, count(flashcards.question) as count " +
            "from decks " +
            "left join flashcards " +
            "on decks.id = flashcards.deckid " +
            "group by decks.id")
    List<DeckWithFlashcardsNumber> getAllDecksWithFlashcardsNumber();

    @SqlQuery("select id, name, public from decks where name like :name")
    List<Deck> getDecksByName(@Bind("name") String name);

    @SqlUpdate("insert into decks (id, name, public) values (:id, :name, :isPublic)")
    void createDeck(@BindBean Deck deck);

    @SqlUpdate("update decks set name = :name, public = :isPublic where id = :id")
    void updateDeck(@BindBean Deck deck);

    @SqlUpdate("delete from decks where id = :id")
    void deleteDeck(@Bind("id") UUID id);
}
