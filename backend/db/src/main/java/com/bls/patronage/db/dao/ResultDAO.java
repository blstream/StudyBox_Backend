package com.bls.patronage.db.dao;

import com.bls.patronage.db.mapper.ResultMapper;
import com.bls.patronage.db.model.Result;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RegisterMapper(ResultMapper.class)
abstract public class ResultDAO extends AuditDAO{

    @SqlQuery("select flashcardId, correctAnswers, userId from results " +
            "where flashcardId = :flashcardId and userId = :userId")
    abstract Result get(@Bind("flashcardId") UUID flashcardId, @Bind("userId") UUID userId);

    @SqlUpdate("insert into results (flashcardId, correctAnswers, userId) " +
            "values (:id, :correctAnswers, :userId)")
    abstract void create(@BindBean Result result);

    @SqlQuery("select results.flashcardId, results.correctAnswers, results.userId " +
            "from results left join flashcards on results.flashcardId = flashcards.id " +
            "where flashcards.deckId = :deckId")
    public abstract List<Result> getAllResults(@Bind("deckId") UUID deckId);

    @SqlUpdate("update results set correctAnswers = :correctAnswers where flashcardId = :id and userId = :userId")
    abstract void update(@BindBean Result result);

    @SqlUpdate("delete from results where flashcardId = :id")
    public abstract void deleteResult(@Bind("id") UUID id);

    public void createResult(Result result, UUID userId){
        create(result);
        createResultAudit(result.getId(), new Date(), userId);
    }

    public void updateResult(Result result, UUID userId){
        update(result);
        updateResultAudit(result.getId(), new Date(), userId);
    }

    public Optional getResult(UUID flashcardId, UUID userId) {
        return Optional.ofNullable(get(flashcardId, userId));
    }
}
