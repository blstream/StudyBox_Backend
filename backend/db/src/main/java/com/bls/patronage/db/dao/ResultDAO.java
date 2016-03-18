package com.bls.patronage.db.dao;

import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.model.Flashcard;
import com.bls.patronage.db.model.Result;
import org.skife.jdbi.v2.sqlobject.*;
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

    @SqlUpdate("update results set correctAnswers = :correctAnswers where flashcardId = :id")
    public abstract void updateResult(@BindBean Result result);

    @SqlUpdate("delete from results where flashcardId = :id")
    public abstract void deleteResult(@Bind("id") UUID id);

    public Result getResult(UUID id) {
        Optional<Result> result = Optional.ofNullable(get(id));
        return result.orElseGet(() -> {
            Result newResult = new Result(id, 0);
            createResult(newResult);
            return newResult;
        });
    }
}
