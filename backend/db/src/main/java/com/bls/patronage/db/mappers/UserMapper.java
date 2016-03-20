package com.bls.patronage.db.mappers;

import com.bls.patronage.db.model.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserMapper implements ResultSetMapper<User> {
    @Override
    public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new User((UUID) r.getObject("userId"), r.getString("email"), r.getString("name"), r.getString("password"));
    }
}
