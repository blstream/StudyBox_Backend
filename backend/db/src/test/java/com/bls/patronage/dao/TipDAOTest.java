package com.bls.patronage.dao;

import com.bls.patronage.db.dao.TipDAO;
import com.bls.patronage.db.model.Tip;
import org.junit.Test;
import org.skife.jdbi.v2.Handle;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by arek on 3/22/16.
 */
public class TipDAOTest extends DAOTest {

    private Tip exampleTip1;
    private Tip exampleTip2;
    private UUID flashcardId;
    private UUID deckId;
    private TipDAO tipDAO;

    @Override
    public void setUp() throws Exception {
        flashcardId = UUID.randomUUID();
        deckId = UUID.randomUUID();
        exampleTip1 = new Tip(UUID.randomUUID(), "foo", 5, flashcardId, deckId);
        exampleTip2 = new Tip(UUID.randomUUID(), "goo", 8, flashcardId, deckId);

        super.setUp();
        tipDAO = dbi.open(TipDAO.class);
    }

    @Override
    protected void setUpDatabaseContent(Handle handle) {
        handle.createCall("DROP TABLE tips IF EXISTS").invoke();
        handle.createCall(
                "CREATE TABLE tips (id uuid primary key, essence varchar(1000) not null, difficult int not null, flashcardId uuid, deckId uuid)")
                .invoke();
        handle.createStatement("INSERT INTO tips VALUES (?, ?, ?, ?, ?)")
                .bind(0, exampleTip1.getId())
                .bind(1, exampleTip1.getEssence())
                .bind(2, exampleTip1.getDifficult())
                .bind(3, exampleTip1.getFlashcardId())
                .bind(4, exampleTip1.getDeckId())
                .execute();
        handle.createStatement("INSERT INTO tips VALUES (?, ?, ?, ?, ?)")
                .bind(0, exampleTip2.getId())
                .bind(1, exampleTip2.getEssence())
                .bind(2, exampleTip2.getDifficult())
                .bind(3, exampleTip2.getFlashcardId())
                .bind(4, exampleTip2.getDeckId())
                .execute();
    }

    @Test
    public void getAllTips(){
        final List<Tip> tips = tipDAO.getAllTips(flashcardId);
        assertThat(tips).containsSequence(exampleTip1, exampleTip2);
    }

    @Test
    public void getTipById(){
        final Tip tip = tipDAO.getTipById(exampleTip1.getId());
        assertThat(tip).isEqualTo(exampleTip1);
    }

    @Test
    public void createTip(){
        final Tip tip = new Tip(UUID.randomUUID(), "foos", 3, UUID.randomUUID(), UUID.randomUUID());
        tipDAO.createTip(tip);
        assertThat(tipDAO.getTipById(tip.getId())).isEqualTo(tip);
    }

    @Test
    public void deleteTip(){
        tipDAO.deleteTip(exampleTip2.getId());
        assertThat(tipDAO.getAllTips(exampleTip2.getFlashcardId())).doesNotContain(exampleTip2);
    }

    @Test
    public void updateTip(){
        final Tip tip = new Tip(exampleTip1.getId(), "goos", 1, exampleTip1.getFlashcardId(), exampleTip1.getDeckId());
        tipDAO.updateTip(tip);
        assertThat(tipDAO.getTipById(exampleTip1.getId())).isEqualTo(tip);
    }
}
