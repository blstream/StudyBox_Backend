package com.bls.patronage.db.dao;

import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.mapper.TipMapper;
import com.bls.patronage.db.model.Tip;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by arek on 3/22/16.
 */
@RegisterMapper(TipMapper.class)
public abstract class TipDAO {

    @SqlQuery("select id, essence, difficult, flashcardId, deckId from tips where id = :id")
    abstract Tip getTip(@Bind("id") UUID id);

    @SqlQuery("select id, essence, difficult, flashcardId, deckId from tips where flashcardId = :flashcardId")
    public abstract List<Tip> getAllTips(@Bind("flashcardId") UUID flashcardId);

    @GetGeneratedKeys
    @SqlUpdate("insert into tips values (:id, :essence, :difficult, :flashcardId, :deckId)")
    public abstract UUID createTip(@BindBean Tip tip);

    @SqlUpdate("update tips set essence = :essence, difficult = :difficult where id = :id")
    public abstract void updateTip(@BindBean Tip tip);

    @SqlUpdate("delete from tips where id = :id")
    public abstract void deleteTip(@Bind("id") UUID id);

    public Tip getTipById(UUID id){
        Optional<Tip> tip = Optional.ofNullable(getTip(id));
        return tip.orElseThrow(() -> new DataAccessException("There is no tip with specified id"));
    }

}
