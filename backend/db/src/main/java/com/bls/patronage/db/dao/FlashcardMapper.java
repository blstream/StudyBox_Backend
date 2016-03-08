package com.bls.patronage.db.dao;

import com.bls.patronage.db.model.Flashcard;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

class FlashcardMapper implements ResultSetMapper<Flashcard> {

    public Flashcard map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Flashcard(r.getString("id"), r.getString("question"), r.getString("answer"), r.getString("deckID"));
    }
}
