package com.bls.patronage.db.dao;

import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.mapper.TipMapper;
import com.bls.patronage.db.model.Tip;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RegisterMapper(TipMapper.class)
public abstract class TipDAO extends AuditDAO{

    @SqlQuery("select id, essence, difficult, flashcardId, deckId, essenceImageURL from tips where id = :id")
    abstract Tip getTip(@Bind("id") UUID id);

    @SqlQuery("select id, essence, difficult, flashcardId, deckId, essenceImageURL from tips where flashcardId = :flashcardId")
    public abstract List<Tip> getAllTips(@Bind("flashcardId") UUID flashcardId);

    @SqlUpdate("insert into tips (id, essence, difficult, flashcardId, deckId, essenceImageURL) values (:id, :essence, :difficult, :flashcardId, :deckId, :essenceImageURL)")
    abstract void create(@BindBean Tip tip);

    @SqlUpdate("update tips set essence = :essence, difficult = :difficult,essenceImageURL = :essenceImageURL where id = :id")
    abstract void update(@BindBean Tip tip);

    @SqlUpdate("delete from tips where id = :id")
    public abstract void deleteTip(@Bind("id") UUID id);

    public void createTip(Tip tip, UUID userId){
        create(tip);
        createTipAudit(tip.getId(), new Date(), userId);
    }

    public void updateTip(Tip tip, UUID userId){
        update(tip);
        updateTipAudit(tip.getId(), new Date(), userId);
    }

    public Tip getTipById(UUID id){
        Optional<Tip> tip = Optional.ofNullable(getTip(id));
        return tip.orElseThrow(() -> new DataAccessException("There is no tip with specified id"));
    }

}
