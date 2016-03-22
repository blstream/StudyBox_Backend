package com.bls.patronage.db.mapper;

import com.bls.patronage.db.model.Flashcard;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class FlashcardMapper implements ResultSetMapper<Flashcard> {

    public Flashcard map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Flashcard((UUID) r.getObject("id"), r.getString("question"), r.getString("answer"), (UUID) r.getObject("deckId"));
    }
}
