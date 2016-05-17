package com.bls.patronage.dao;

import com.bls.patronage.db.dao.ResultDAO;
import com.bls.patronage.db.mapper.FlashcardMapper;
import com.bls.patronage.db.mapper.ResultMapper;
import com.bls.patronage.db.model.Flashcard;
import com.bls.patronage.db.model.Result;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class ResultDAOTest extends DAOTest {

    private ResultDAO dao;
    private UUID defaultUserUUID;

    @Override
    @BeforeTest
    public void buildDatabase() {
        super.buildDatabase();
        dao = dbi.onDemand(ResultDAO.class);
        defaultUserUUID = UUID.fromString("b3f3882b-b138-4bc0-a96b-cd25e087ff4e");
    }


    @Override
    @BeforeMethod
    public void loadContent() throws Exception {
        super.loadContent();
    }

    private List<Result> getResultsFromDatabase() throws Exception{
        return getAllEntities(Result.class, ResultMapper.class, "results");
    }
    private List<Flashcard> getFlashcardsFromDatabase() throws Exception {
        return getAllEntities(Flashcard.class,  FlashcardMapper.class, "flashcards");
    }

    @Override
    @AfterTest
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void getAllResults() throws Exception {
        final List<Result> results = getResultsFromDatabase().subList(0,2);
        assertThat(dao.getAllResults(getFlashcardsFromDatabase().get(0).getDeckId())).containsAll(results);
    }

    public void getResultById() throws Exception {
        final Result result = getResultsFromDatabase().get(0);
        assertThat(dao.getResult(result.getId(), result.getUserId()).get()).isEqualTo(result);
    }

    public void createResult() throws Exception {
        final Result result = new Result(UUID.randomUUID(), new Random().nextInt(), UUID.randomUUID());
        dao.createResult(result, defaultUserUUID);
        assertThat(getResultsFromDatabase()).contains(result);
    }

    public void deleteResult() throws Exception {
        final Result result = getResultsFromDatabase().get(0);
        dao.deleteResult(result.getId());
        assertThat(getResultsFromDatabase()).doesNotContain(result);
    }

    public void updateResult() throws Exception {
        final Result result = getResultsFromDatabase().get(0);
        final Result newResult = new Result(result.getId(), new Random().nextInt(), result.getUserId());
        dao.updateResult(newResult, defaultUserUUID);
        assertThat(getResultsFromDatabase()).doesNotContain(result);
        assertThat(getResultsFromDatabase()).contains(newResult);
    }
}
