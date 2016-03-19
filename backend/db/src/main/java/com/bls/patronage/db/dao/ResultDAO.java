package com.bls.patronage.db.dao;

import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.mappers.ResultMapper;
import com.bls.patronage.db.model.Result;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RegisterMapper(ResultMapper.class)
abstract public class ResultDAO {

    @SqlQuery("select flashcardId, correctAnswers from results where flashcardId = :id")
    abstract Result get(@Bind("id") UUID id);

    @SqlUpdate("insert into results values (:id, :correctAnswers)")
    public abstract void createResult(@BindBean Result result);

    @SqlQuery("select results.flashcardId, results.correctAnswers from results left join flashcards on results.flashcardId = flashcards.id where flashcards.deckId = :deckId")
    public abstract List<Result> getAllResults(@Bind("deckId") UUID deckId);


    @SqlUpdate("update results set correctAnswers = :correctAnswers where flashcardId = :id")
    public abstract void updateResult(@BindBean Result result);

    @SqlUpdate("delete from results where flashcardId = :id")
    public abstract void deleteResult(@Bind("id") UUID id);

    public Result getResult(UUID id) {
        Optional<Result> result = Optional.ofNullable(get(id));
        return result.orElseThrow(() -> new DataAccessException("There is no result with specified id"));
    }
}
