package com.bls.patronage.dao;

import com.bls.patronage.db.dao.TipDAO;
import com.bls.patronage.db.mapper.TipMapper;
import com.bls.patronage.db.model.Tip;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class TipDAOTest extends DAOTest {

    private TipDAO tipDAO;

    @Override
    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        tipDAO = dbi.onDemand(TipDAO.class);
    }

    private List<Tip> getTipsFromDatabase() throws Exception {
        return getAllEntities(Tip.class, TipMapper.class, "tips");
    }

    @Override
    @AfterMethod
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
        tipDAO.createTip(tip);
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
        tipDAO.updateTip(newTip);
        assertThat(getTipsFromDatabase()).doesNotContain(tip);
        assertThat(getTipsFromDatabase()).contains(newTip);
    }
}
