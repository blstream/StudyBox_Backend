package com.bls.patronage.dao;

import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.mapper.FlashcardMapper;
import com.bls.patronage.db.model.Flashcard;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class FlashcardDAOTest extends DAOTest {

    private FlashcardDAO dao;

    @Override
    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        dao = dbi.onDemand(FlashcardDAO.class);
    }

    private List<Flashcard> getFlashcardsFromDatabase() throws Exception {
        return getAllEntities(Flashcard.class,  FlashcardMapper.class, "flashcards");
    }

    @Override
    @AfterMethod
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void getAllFlashcards() throws Exception {
        List<Flashcard> flashcardList = getFlashcardsFromDatabase().subList(0, 2);
        final List<Flashcard> flashcards = dao.getAllFlashcards(flashcardList.get(0).getDeckId());
        assertThat(flashcards).containsAll(flashcardList);
    }

    public void getFlashcardById() throws Exception {
        Flashcard flashcard = getFlashcardsFromDatabase().get(0);
        final Flashcard flashcardById = dao.getFlashcardById(flashcard.getId());
        assertThat(flashcardById).isEqualTo(flashcard);
    }

    public void createFlashcard(){
        final Flashcard flashcard = new Flashcard(UUID.randomUUID(), "foos", "bars", UUID.randomUUID());
        dao.createFlashcard(flashcard);
        assertThat(dao.getFlashcardById(flashcard.getId())).isEqualTo(flashcard);
    }
    //c6c4d451-65dd-4ac0-9e53-974397c7bea7

    public void deleteFlashcard() throws Exception {
        Flashcard flashcard = getFlashcardsFromDatabase().get(0);
        dao.deleteFlashcard(flashcard.getId());
        assertThat(getFlashcardsFromDatabase()).doesNotContain(flashcard);
    }

    public void updateFlashcard() throws Exception {
        Flashcard flashcard = getFlashcardsFromDatabase().get(0);
        Flashcard newFlascard = new Flashcard(flashcard.getId(), "foo", "baz", flashcard.getDeckId());
        dao.updateFlashcard(newFlascard);
        assertThat(getFlashcardsFromDatabase()).doesNotContain(flashcard);
        assertThat(getFlashcardsFromDatabase()).contains(newFlascard);
    }

    public void getRandomFlashcards() throws Exception {
        final List<Flashcard> flashcards = getFlashcardsFromDatabase();
        for (Integer number: new Integer[]{1,5,10,15,20}) {
            List<Flashcard> randomFlashcards = dao.getRandomFlashcards(number, flashcards.get(11).getDeckId());
            assertThat(randomFlashcards).hasSize(number);
            assertThat(flashcards).containsAll(randomFlashcards);
        }
    }

    public void getFlashcardsNumber() throws Exception {
        final Flashcard flashcard = getFlashcardsFromDatabase().get(0);
        assertThat(dao.getFlashcardsNumber(flashcard.getDeckId())).isEqualTo(3);
    }
}
