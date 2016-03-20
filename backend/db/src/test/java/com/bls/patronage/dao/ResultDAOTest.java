package com.bls.patronage.dao;

import com.bls.patronage.db.dao.ResultDAO;
import com.bls.patronage.db.model.Flashcard;
import com.bls.patronage.db.model.Result;
import org.junit.Test;
import org.skife.jdbi.v2.Handle;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ResultDAOTest extends DAOTest {

    private Flashcard flashcardExample1;
    private Flashcard flashcardExample2;
    private Result resultExample1;
    private Result resultExample2;
    private UUID deckID;
    private ResultDAO dao;

    @Override
    public void setUp() throws Exception {
        deckID = UUID.randomUUID();
        flashcardExample1 = new Flashcard(UUID.randomUUID(), "what are you?", "Just human", deckID);
        flashcardExample2 = new Flashcard(UUID.randomUUID(), "who are you?", "Proud pole", deckID);
        resultExample1 = new Result(flashcardExample1.getId(), new Random().nextInt());
        resultExample2 = new Result(flashcardExample2.getId(), new Random().nextInt());

        super.setUp();
        dao = dbi.open(ResultDAO.class);
    }

    @Override
    protected void setUpDatabaseContent(Handle handle) {
        handle.createCall("DROP TABLE results IF EXISTS").invoke();
        handle.createCall(
                "CREATE TABLE results (flashcardId uuid primary key, correctAnswers int not null)")
                .invoke();
        handle.createStatement("INSERT INTO results VALUES (?, ?)")
                .bind(0, resultExample1.getId())
                .bind(1, resultExample1.getCorrectAnswers())
                .execute();
        handle.createStatement("INSERT INTO results VALUES (?, ?)")
                .bind(0, resultExample2.getId())
                .bind(1, resultExample2.getCorrectAnswers())
                .execute();
        handle.createCall("DROP TABLE flashcards IF EXISTS").invoke();
        handle.createCall(
                "CREATE TABLE flashcards (id uuid primary key, question varchar(50) not null, answer varchar(50) not null, deckID uuid)")
                .invoke();
        handle.createStatement("INSERT INTO flashcards VALUES (?, ?, ?, ?)")
                .bind(0, flashcardExample1.getId())
                .bind(1, flashcardExample1.getQuestion())
                .bind(2, flashcardExample1.getAnswer())
                .bind(3, flashcardExample1.getDeckId())
                .execute();
        handle.createStatement("INSERT INTO flashcards VALUES (?, ?, ?, ?)")
                .bind(0, flashcardExample2.getId())
                .bind(1, flashcardExample2.getQuestion())
                .bind(2, flashcardExample2.getAnswer())
                .bind(3, flashcardExample1.getDeckId())
                .execute();
    }

    @Test
    public void getAllResults() {
        final List<Result> results = dao.getAllResults(deckID);
        assertThat(results).containsSequence(resultExample1, resultExample2);
    }

    @Test
    public void getResultById() {
        final Result result = dao.getResult(resultExample1.getId());
        assertThat(result).isEqualTo(resultExample1);
    }

    @Test
    public void createResult() {
        final Result result = new Result(UUID.randomUUID(), new Random().nextInt());
        dao.createResult(result);
        assertThat(dao.getResult(result.getId())).isEqualTo(result);
    }

    @Test
    public void deleteResult() {
        dao.deleteResult(resultExample2.getId());
        assertThat(dao.getAllResults(deckID)).doesNotContain(resultExample2);
    }

    @Test
    public void updateResult() {
        final Result result = new Result(resultExample1.getId(), new Random().nextInt());
        dao.updateResult(result);
        assertThat(dao.getResult(resultExample1.getId())).isEqualTo(result);
    }
}
