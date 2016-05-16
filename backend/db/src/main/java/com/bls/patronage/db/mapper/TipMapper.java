package com.bls.patronage.db.mapper;

import com.bls.patronage.db.model.Tip;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TipMapper implements ResultSetMapper<Tip> {
    @Override
    public Tip map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Tip((UUID) resultSet.getObject("id"), resultSet.getString("essence"),
                resultSet.getInt("difficult"), (UUID) resultSet.getObject("flashcardId"),
                (UUID) resultSet.getObject("deckId"), resultSet.getString("essenceImageURL"));
    }
}
