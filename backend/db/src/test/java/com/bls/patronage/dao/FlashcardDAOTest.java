package com.bls.patronage.dao;

import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.mapper.FlashcardMapper;
import com.bls.patronage.db.mapper.TipMapper;
import com.bls.patronage.db.model.Amount;
import com.bls.patronage.db.model.Flashcard;
import com.bls.patronage.db.model.Tip;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class FlashcardDAOTest extends DAOTest {

    private FlashcardDAO dao;
    private UUID defaultUserUUID;

    @Override
    @BeforeTest
    public void buildDatabase() {
        super.buildDatabase();
        dao = dbi.onDemand(FlashcardDAO.class);
        defaultUserUUID = UUID.fromString("b3f3882b-b138-4bc0-a96b-cd25e087ff4e");
    }

    @Override
    @BeforeMethod
    public void loadContent() throws Exception {
        super.loadContent();
    }

    private List<Flashcard> getFlashcardsFromDatabase() throws Exception {
        return getAllEntities(Flashcard.class, FlashcardMapper.class, "flashcards");
    }

    @Override
    @AfterTest
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

    public void createFlashcard() {
        final Flashcard flashcard = new Flashcard(UUID.randomUUID(), "foos", "bars", UUID.randomUUID(), false);
        dao.createFlashcard(flashcard, defaultUserUUID);
        assertThat(dao.getFlashcardById(flashcard.getId())).isEqualTo(flashcard);
    }

    public void deleteFlashcard() throws Exception {
        Flashcard flashcard = getFlashcardsFromDatabase().get(0);
        dao.deleteFlashcard(flashcard.getId());
        assertThat(getFlashcardsFromDatabase()).doesNotContain(flashcard);
    }

    public void updateFlashcard() throws Exception {
        Flashcard flashcard = getFlashcardsFromDatabase().get(0);
        Flashcard newFlascard = new Flashcard(flashcard.getId(), "foo", "baz", flashcard.getDeckId(), true);
        dao.updateFlashcard(newFlascard, defaultUserUUID);
        assertThat(getFlashcardsFromDatabase()).doesNotContain(flashcard);
        assertThat(getFlashcardsFromDatabase()).contains(newFlascard);
    }

    public void getRandomFlashcards() throws Exception {
        final List<Flashcard> flashcards = getFlashcardsFromDatabase();
        for (Amount amount : Amount.values()) {
            List<Flashcard> randomFlashcards = dao.getRandomFlashcards(amount.getValue(),
                    flashcards.get(11).getDeckId());
            assertThat(randomFlashcards).hasSize(amount.getValue());
            assertThat(flashcards).containsAll(randomFlashcards);
        }
    }

    public void getTipsNumber() throws Exception {
        final List<Tip> tips = getAllEntities(Tip.class, TipMapper.class, "tips");
        final UUID flashcardId = tips.get(0).getFlashcardId();
        final List<Tip> tipsInFlashcard = tips.stream().filter(tip -> tip.getFlashcardId().equals(flashcardId))
                .collect(Collectors.toList());

        assertThat(dao.getTipsCount(flashcardId)).isEqualTo(tipsInFlashcard.size());
    }
}
