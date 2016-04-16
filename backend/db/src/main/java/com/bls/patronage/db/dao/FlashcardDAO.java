package com.bls.patronage.db.dao;

import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.mapper.FlashcardMapper;
import com.bls.patronage.db.model.Flashcard;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RegisterMapper(FlashcardMapper.class)
abstract public class FlashcardDAO {

    @SqlQuery("select id,question,answer,deckId,isHidden from flashcards where id = :id")
    abstract Flashcard get(@Bind("id") UUID id);

    @SqlQuery("select id,question,answer,deckId,isHidden from flashcards where deckId = :deckId")
    public abstract List<Flashcard> getAllFlashcards(@Bind("deckId") UUID deckId);

    @SqlQuery("select id from flashcards where deckId = :deckId")
    public abstract List<UUID> getFlashcardsIdFromSelectedDeck(@Bind("deckId") UUID deckId);

    @GetGeneratedKeys
    @SqlUpdate("insert into flashcards values (:id, :question, :answer, :deckId, :isHidden)")
    public abstract UUID createFlashcard(@BindBean Flashcard flashcard);

    @SqlUpdate("update flashcards set question = :question, answer = :answer, isHidden = :isHidden where id = :id")
    public abstract void updateFlashcard(@BindBean Flashcard flashcard);

    @SqlUpdate("delete from flashcards where id = :id")
    public abstract void deleteFlashcard(@Bind("id") UUID id);

    //  Temporary solution for getting random flashcards
    @SqlQuery("select id,question,answer,deckId,isHidden from flashcards where deckId = :deckId limit 1 " +
            "offset floor(:random*:number)")
    abstract Flashcard getRandomFlashcard(@Bind("random") Double random, @Bind("number") Integer number, @Bind("deckId") UUID deckId);

    @SqlQuery("select count(*) from flashcards where deckId = :deckId")
    abstract Integer getCount(@Bind("deckId") UUID deckId);

    @SqlQuery("select count(tips.id) from tips where flashcardId = :flashcardId")
    public abstract Integer getTipsCount(@Bind("flashcardId") UUID flashcardId);

    public Flashcard getFlashcardById(UUID id) {
        Optional<Flashcard> flashcard = Optional.ofNullable(get(id));
        return flashcard.orElseThrow(() -> new DataAccessException("There is no flashcard with specified id"));
    }

    public List<Flashcard> getRandomFlashcards(Integer number, UUID deckId) {
        List<Flashcard> flashcards = new ArrayList<>();
        Integer count = getCount(deckId);
        while (flashcards.size() < number && flashcards.size() < count) {
            Flashcard flashcard = getRandomFlashcard(Math.random(), count, deckId);
            if (!flashcards.contains(flashcard)) {
                flashcards.add(flashcard);
            }
        }
        return flashcards;
    }
}
