package com.bls.patronage.db;

import com.bls.patronage.core.Deck;
import com.bls.patronage.mapper.DeckMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;
import java.util.UUID;

@RegisterMapper(DeckMapper.class)
public interface DeckDAO {

    @SqlQuery("select * from decks where id = :id")
    Deck getDeckById(@Bind("id") UUID id);

    @SqlQuery("select * from decks")
    List<Deck> getAllDecks();

    @GetGeneratedKeys
    @SqlUpdate("insert into decks (id, name) values (:id, :name)")
    UUID createDeck(@BindBean Deck deck);

    @SqlUpdate("update decks set name = :name where id = :id")
    void updateDeck(@BindBean Deck deck);

    @SqlUpdate("delete from decks where id = :id")
    void deleteDeck(@Bind("id") UUID id);

}
