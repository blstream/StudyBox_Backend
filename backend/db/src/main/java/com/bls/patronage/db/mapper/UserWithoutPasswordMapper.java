package com.bls.patronage.db.mapper;

import com.bls.patronage.db.model.UserWithoutPassword;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserWithoutPasswordMapper implements ResultSetMapper<UserWithoutPassword> {
    @Override
    public UserWithoutPassword map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new UserWithoutPassword((UUID) r.getObject("id"), r.getString("email"), r.getString("name"));
    }
}
