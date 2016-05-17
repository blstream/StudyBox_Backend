package com.bls.patronage.db.dao;

import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.mapper.FlashcardMapper;
import com.bls.patronage.db.model.Flashcard;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RegisterMapper(FlashcardMapper.class)
abstract public class FlashcardDAO extends AuditDAO{

    @SqlQuery("select id, question, answer, deckId, isHidden, questionImageURL, answerImageURL from flashcards where id = :id")
    abstract Flashcard get(@Bind("id") UUID id);

    @SqlQuery("select id, question, answer, deckId, isHidden, questionImageURL, answerImageURL from flashcards where deckId = :deckId")
    public abstract List<Flashcard> getAllFlashcards(@Bind("deckId") UUID deckId);

    @SqlQuery("select id from flashcards where deckId = :deckId")
    public abstract List<String> getFlashcardsIdFromSelectedDeck(@Bind("deckId") UUID deckId);

    @SqlUpdate("insert into flashcards (id, question, answer, deckId, isHidden, questionImageURL, answerImageURL) values (:id, :question, :answer, :deckId, :isHidden, :questionImageURL, :answerImageURL)")
    abstract void create(@BindBean Flashcard flashcard);

    @SqlUpdate("update flashcards set question = :question, answer = :answer, isHidden = :isHidden,questionImageURL = :questionImageURL, answerImageURL = :answerImageURL where id = :id")
    abstract void update(@BindBean Flashcard flashcard);

    @SqlUpdate("delete from flashcards where id = :id")
    public abstract void deleteFlashcard(@Bind("id") UUID id);

    //  Temporary solution for getting random flashcards
    @SqlQuery("select id, question, answer, deckId, isHidden, questionImageURL, answerImageURL from flashcards where deckId = :deckId limit 1 " +
            "offset floor(:random*:number)")
    abstract Flashcard getRandomFlashcard(@Bind("random") Double random, @Bind("number") Integer number, @Bind("deckId") UUID deckId);

    @SqlQuery("select count(*) from flashcards where deckId = :deckId")
    abstract Integer getCount(@Bind("deckId") UUID deckId);

    @SqlQuery("select count(tips.id) from tips where flashcardId = :flashcardId")
    public abstract Integer getTipsCount(@Bind("flashcardId") UUID flashcardId);

    public void createFlashcard(Flashcard flashcard, UUID userId){
        create(flashcard);
        createFlashcardAudit(flashcard.getId(), new Date(), userId);
    }

    public void updateFlashcard(Flashcard flashcard, UUID userId){
        update(flashcard);
        updateFlashcardAudit(flashcard.getId(), new Date(), userId);
    }

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
