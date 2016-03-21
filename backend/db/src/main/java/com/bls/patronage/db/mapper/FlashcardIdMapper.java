package com.bls.patronage.db.mapper;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class FlashcardIdMapper implements ResultSetMapper<UUID> {
    @Override
    public UUID map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return (UUID) r.getObject("id");
    }
}
