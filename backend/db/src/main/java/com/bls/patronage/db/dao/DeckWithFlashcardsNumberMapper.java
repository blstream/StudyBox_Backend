package com.bls.patronage.db.dao;

import com.bls.patronage.db.model.DeckWithFlashcardsNumber;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DeckWithFlashcardsNumberMapper implements ResultSetMapper<DeckWithFlashcardsNumber> {

    public DeckWithFlashcardsNumber map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new DeckWithFlashcardsNumber((UUID) r.getObject("id"), r.getString("name"),
                r.getBoolean("public"), r.getInt("count"));
    }
}
