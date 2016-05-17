package com.bls.patronage.dao;

import com.bls.patronage.db.dao.TipDAO;
import com.bls.patronage.db.mapper.TipMapper;
import com.bls.patronage.db.model.Tip;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class TipDAOTest extends DAOTest {

    private TipDAO tipDAO;
    private UUID defaultUserUUID;

    @Override
    @BeforeTest
    public void buildDatabase() {
        super.buildDatabase();
        tipDAO = dbi.onDemand(TipDAO.class);
        defaultUserUUID = UUID.fromString("b3f3882b-b138-4bc0-a96b-cd25e087ff4e");
    }

    @Override
    @BeforeMethod
    public void loadContent() throws Exception {
        super.loadContent();
    }

    private List<Tip> getTipsFromDatabase() throws Exception {
        return getAllEntities(Tip.class, TipMapper.class, "tips");
    }

    @Override
    @AfterTest
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void getAllTips() throws Exception {
        final List<Tip> tips = getTipsFromDatabase().subList(0,1);
        assertThat(tipDAO.getAllTips(tips.get(0).getFlashcardId())).containsAll(tips);
    }

    public void getTipById() throws Exception {
        final Tip tip = getTipsFromDatabase().get(0);
        assertThat(tipDAO.getTipById(tip.getId())).isEqualTo(tip);
    }

    public void createTip() throws Exception {
        final Tip tip = new Tip(UUID.randomUUID(), "foos", 3, UUID.randomUUID(), UUID.randomUUID());
        tipDAO.createTip(tip, defaultUserUUID);
        assertThat(getTipsFromDatabase()).contains(tip);
    }

    public void deleteTip() throws Exception {
        final Tip tip = getTipsFromDatabase().get(0);
        tipDAO.deleteTip(tip.getId());
        assertThat(getTipsFromDatabase()).doesNotContain(tip);
    }

    public void updateTip() throws Exception {
        final Tip tip = getTipsFromDatabase().get(0);
        final Tip newTip = new Tip(tip.getId(), "goos", 1, tip.getFlashcardId(), tip.getDeckId());
        tipDAO.updateTip(newTip, defaultUserUUID);
        assertThat(getTipsFromDatabase()).doesNotContain(tip);
        assertThat(getTipsFromDatabase()).contains(newTip);
    }
}
