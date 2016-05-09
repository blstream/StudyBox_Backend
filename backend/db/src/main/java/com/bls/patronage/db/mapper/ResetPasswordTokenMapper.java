package com.bls.patronage.db.mapper;

import com.bls.patronage.db.model.ResetPasswordToken;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class ResetPasswordTokenMapper implements ResultSetMapper<ResetPasswordToken> {

    public ResetPasswordToken map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new ResetPasswordToken(
                (UUID) r.getObject("token"),
                r.getString("email"),
                (Date) r.getObject("expirationDate"),
                r.getBoolean("isActive"));
    }
}
