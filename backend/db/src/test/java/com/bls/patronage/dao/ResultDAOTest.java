package com.bls.patronage.dao;

import com.bls.patronage.db.dao.ResultDAO;
import com.bls.patronage.db.mapper.FlashcardMapper;
import com.bls.patronage.db.mapper.ResultMapper;
import com.bls.patronage.db.model.Flashcard;
import com.bls.patronage.db.model.Result;
import io.dropwizard.testing.ResourceHelpers;
import liquibase.util.csv.CSVReader;
import org.skife.jdbi.v2.Handle;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class ResultDAOTest extends DAOTest {

    private ResultDAO dao;

    @Override
    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        dao = dbi.open(ResultDAO.class);
    }

    private List<Result> getResultsFromDatabase() throws Exception{
        return getObjectsFromDatabase(Result.class, ResultMapper.class, "results");
    }
    private List<Flashcard> getFlashcardsFromDatabase() throws Exception {
        return getObjectsFromDatabase(Flashcard.class,  FlashcardMapper.class, "flashcards");
    }

    @Override
    @AfterMethod
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void getAllResults() throws Exception {
        final List<Result> results = getResultsFromDatabase().subList(0,2);
        assertThat(dao.getAllResults(getFlashcardsFromDatabase().get(0).getDeckId())).containsAll(results);
    }

    public void getResultById() throws Exception {
        final Result result = getResultsFromDatabase().get(0);
        assertThat(dao.getResult(result.getId())).isEqualTo(result);
    }

    public void createResult() throws Exception {
        final Result result = new Result(UUID.randomUUID(), new Random().nextInt());
        dao.createResult(result);
        assertThat(getResultsFromDatabase()).contains(result);
    }

    public void deleteResult() throws Exception {
        final Result result = getResultsFromDatabase().get(0);
        dao.deleteResult(result.getId());
        assertThat(getResultsFromDatabase()).doesNotContain(result);
    }

    public void updateResult() throws Exception {
        final Result result = getResultsFromDatabase().get(0);
        final Result newResult = new Result(result.getId(), new Random().nextInt());
        dao.updateResult(newResult);
        assertThat(getResultsFromDatabase()).doesNotContain(result);
        assertThat(getResultsFromDatabase()).contains(newResult);
    }
}
