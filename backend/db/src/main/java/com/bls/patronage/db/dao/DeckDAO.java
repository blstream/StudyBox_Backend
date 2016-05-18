package com.bls.patronage.db.dao;

import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.mapper.DeckMapper;
import com.bls.patronage.db.model.Deck;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RegisterMapper(DeckMapper.class)
@UseStringTemplate3StatementLocator
public abstract class DeckDAO extends AuditDAO {

    @SqlQuery("select decks.id, decks.name, decks.isPublic from decks join usersDecks on usersDecks.deckId = decks.id " +
            "where usersDecks.userId = :userId group by decks.id")
    public abstract Collection<Deck> getAllUserDecks(@Bind("userId") UUID userId);

    @SqlQuery("select count(flashcards.id) from flashcards where flashcards.deckId = :deckId")
    public abstract Integer getFlashcardsCount(@Bind("deckId") UUID deckId);

    @SqlUpdate("insert into decks (id, name, isPublic) values (:id, :name, :isPublic)")
    abstract void insertDeck(@BindBean Deck deck);

    @SqlUpdate("insert into usersDecks (deckId, userId, creationDate) values (:id, :userId, :date)")
    abstract void insertUsersDeck(@BindBean Deck deck, @Bind("userId") UUID userId, @Bind("date") Date date);

    @SqlUpdate("update decks set name = :name, isPublic = :isPublic where id = :id")
    abstract void update(@BindBean Deck deck);

    @SqlUpdate("delete from decks where id = :id")
    public abstract void deleteDeck(@Bind("id") UUID id);

    @SqlQuery("select id, name, isPublic from decks where id = :id")
    abstract Deck getDeck(@Bind("id") UUID id);

    @SqlQuery("select userId from usersDecks where deckId = :id")
    abstract String getDeckUserId(@Bind("id") UUID id);

    @SqlQuery("select email from users where id = :id")
    abstract String getCreatorEmailFromUserId(@Bind("id") UUID id);

    @SqlQuery("select id, name, isPublic from decks " +
            "join usersDecks on decks.id = usersDecks.deckId " +
            "where decks.name like :name and decks.isPublic='true' " +
            "and usersDecks.userId != :id")
    abstract List<Deck> getDecksUsingName(@Bind("name") String name, @Bind("id") UUID userId);

    @SqlQuery("select id, name, isPublic from decks " +
            "inner join usersDecks on decks.id = usersDecks.deckId " +
            "where decks.name like :name and usersDecks.userId = :id")
    abstract List<Deck> getUserDecksUsingName(@Bind("name") String name, @Bind("id") UUID userId);

    @SqlQuery("select id, name, isPublic from decks " +
            "join usersDecks on decks.id = usersDecks.deckId " +
            "where decks.isPublic = 'true' and usersDecks.userId != :id")
    abstract Collection<Deck> getDecks(@Bind("id") UUID userId);

    @SqlQuery("select decks.id, decks.name, decks.isPublic from decks " +
            "join usersDecks on usersDecks.deckId = decks.id " +
            "where usersDecks.userId = :userId or decks.isPublic = 'true' " +
            "limit 1 offset floor(:random*:number)")
    abstract Deck getRandom(@Bind("userId") UUID userId,
                            @Bind("random") Double random,
                            @Bind("number") Integer number);

    @SqlQuery("select count(*) from decks join usersDecks on usersDecks.deckId = decks.id " +
            "where usersDecks.userId = :userId or decks.isPublic = 'true' ")
    abstract Integer getCountUserDecks(@Bind("userId") UUID userId);

    @SqlQuery("select creationDate from usersDecks where deckId = :id")
    public abstract String getDeckCreationDate(@Bind("id") UUID id);

    public void createDeck(Deck deck, UUID userId) {
        insertDeck(deck);
        insertUsersDeck(deck, userId, new Date());
        createDeckAudit("decks", deck.getId(), new Date(), userId);
    }

    public void updateDeck(Deck deck, UUID userId){
        update(deck);
        updateDeckAudit("decks", deck.getId(), new Date(), userId);
    }

    public Deck getDeckById(UUID deckId, UUID userId) {
        Deck deck = getDeck(deckId);
        if (deck == null) {
            throw new DataAccessException("There is no deck with specified ID");
        }
        if (!(getDeckUserId(deckId).equals(userId.toString()) || deck.getIsPublic())) {
            throw new DataAccessException("You have no permission.", Response.Status.FORBIDDEN.getStatusCode());
        }
        return deck;
    }

    public Collection<Deck> getDecksByName(String name, UUID userId) {
        List<Deck> decks = getDecksUsingName(name, userId);
        if (decks.isEmpty()) {
            decks = getDecksUsingName(String.format("%%%s%%", name), userId);
        }
        return decks;
    }

    public Collection<Deck> getUserDecksByName(String name, UUID userId) {
        List<Deck> decks = getUserDecksUsingName(name, userId);
        if (decks.isEmpty()) {
            decks = getUserDecksUsingName(String.format("%%%s%%", name), userId);
        }
        return decks;
    }

    public Collection<Deck> getAllDecks(UUID userId) {
        Collection<Deck> decks = getDecks(userId);
        return decks;
    }

    public String getCreatorEmailFromDeckId(UUID deckId){
        return getCreatorEmailFromUserId(UUID.fromString(getDeckUserId(deckId)));
    }

    public Deck getRandomDeck(UUID userId){
        Integer number = getCountUserDecks(userId);
        return getRandom(userId, Math.random(), number);
    }
}
