package com.bls.patronage.db.mapper;

import com.bls.patronage.db.model.Result;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ResultMapper implements ResultSetMapper<Result> {

    public Result map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Result((UUID) r.getObject("flashcardId"), r.getInt("correctAnswers"));
    }
}
