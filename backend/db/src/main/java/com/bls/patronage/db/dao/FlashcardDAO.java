package com.bls.patronage.db.dao;

import com.bls.patronage.db.model.Flashcard;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;
import java.util.UUID;

@RegisterMapper(FlashcardMapper.class)
public interface FlashcardDAO {

    @SqlQuery("select * from flashcards where id = :id")
    Flashcard getFlashcardById(@Bind("id") UUID id);

    @SqlQuery("select * from flashcards")
    List<Flashcard> getAllFlashcards();

    @GetGeneratedKeys
    @SqlUpdate("insert into flashcards values (:id, :question, :answer)")
    UUID createFlashcard(@BindBean Flashcard flashcard);

    @SqlUpdate("update flashcards set question = :question, answer = :answer where id = :id")
    void updateFlashcard(@BindBean Flashcard flashcard);

    @SqlUpdate("delete from flashcards where id = :id")
    void deleteFlashcard(@Bind("id") UUID id);
}
