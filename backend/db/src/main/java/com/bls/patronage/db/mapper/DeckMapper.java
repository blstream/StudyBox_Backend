package com.bls.patronage.db.mapper;

import com.bls.patronage.db.model.Deck;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DeckMapper implements ResultSetMapper<Deck> {

    public Deck map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Deck((UUID) r.getObject("id"), r.getString("name"), r.getBoolean("public"));
    }
}
