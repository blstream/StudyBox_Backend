package com.bls.patronage.dao;

import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Flashcard;
import org.junit.Test;
import org.skife.jdbi.v2.Handle;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FlashcardDAOTest extends DAOTest {

    private Flashcard flashcardExample1;
    private Flashcard flashcardExample2;
    private UUID deckID;
    private FlashcardDAO dao;

    @Override
    public void setUp() throws Exception {
        deckID = UUID.randomUUID();
        flashcardExample1 = new Flashcard(UUID.randomUUID(),"what are you?", "Just human", deckID);
        flashcardExample2 = new Flashcard(UUID.randomUUID(),"who are you?", "Proud pole", deckID);

        super.setUp();
        dao = dbi.open(FlashcardDAO.class);
    }

    @Override
    protected void setUpDatabaseContent(Handle handle) {
        handle.createCall("DROP TABLE flashcards IF EXISTS").invoke();
        handle.createCall(
                "CREATE TABLE flashcards (id uuid primary key, question varchar(50) not null, answer varchar(50) not null, deckID uuid)")
                .invoke();
        handle.createStatement("INSERT INTO flashcards VALUES (?, ?, ?, ?)")
                .bind(0, flashcardExample1.getId())
                .bind(1, flashcardExample1.getQuestion())
                .bind(2, flashcardExample1.getAnswer())
                .bind(3, flashcardExample1.getDeckID())
                .execute();
        handle.createStatement("INSERT INTO flashcards VALUES (?, ?, ?, ?)")
                .bind(0, flashcardExample2.getId())
                .bind(1, flashcardExample2.getQuestion())
                .bind(2, flashcardExample2.getAnswer())
                .bind(3, flashcardExample1.getDeckID())
                .execute();
    }

    @Test
    public void getAllFlashcards(){
        final List<Flashcard> flashcards = dao.getAllFlashcards(deckID);
        assertThat(flashcards).containsSequence(flashcardExample1, flashcardExample2);
    }

    @Test
    public void getFlashcardById(){
        final Flashcard flashcardById = dao.getFlashcardById(flashcardExample1.getId());
        assertThat(flashcardById).isEqualTo(flashcardExample1);
    }

    @Test
    public void createFlashcard(){
        final Flashcard flashcard = new Flashcard(UUID.randomUUID(),"Will you test me?", "Yes, I will", UUID.randomUUID());
        dao.createFlashcard(flashcard);
        assertThat(dao.getFlashcardById(flashcard.getId())).isEqualTo(flashcard);
    }

    @Test
    public void deleteFlashcard(){
        dao.deleteFlashcard(flashcardExample2.getId());
        assertThat(dao.getFlashcardById(flashcardExample2.getId())).isNull();
    }

    @Test
    public void updateFlashcard(){
        final Flashcard flashcard = new Flashcard(flashcardExample1.getId(),"Am i modified?", "Surely yes", flashcardExample1.getDeckID());
        dao.updateFlashcard(flashcard);
        assertThat(dao.getFlashcardById(flashcardExample1.getId())).isEqualTo(flashcard);
    }
}
