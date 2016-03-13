package com.bls.patronage.db.dao;

import java.util.List;
import java.util.UUID;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.dao.DeckMapper;

@RegisterMapper(DeckMapper.class)
public interface DeckDAO {

    @SqlQuery("select id, name from decks where id = :id")
    Deck getDeckById(@Bind("id") UUID id);

    @SqlQuery("select id, name from decks")
    List<Deck> getAllDecks();

    @SqlQuery("select id, name from decks where name like :name")
    List<Deck> getDecksByName(@Bind("name") String name);

    @GetGeneratedKeys
    @SqlUpdate("insert into decks (id, name) values (:id, :name)")
    UUID createDeck(@BindBean Deck deck);

    @SqlUpdate("update decks set name = :name where id = :id")
    void updateDeck(@BindBean Deck deck);

    @SqlUpdate("delete from decks where id = :id")
    void deleteDeck(@Bind("id") UUID id);
}
