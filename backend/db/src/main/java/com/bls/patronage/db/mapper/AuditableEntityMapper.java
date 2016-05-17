package com.bls.patronage.db.mapper;

import com.bls.patronage.db.model.AuditableEntity;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuditableEntityMapper implements ResultSetMapper<AuditableEntity> {
    public AuditableEntity map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new AuditableEntity(r.getTimestamp("createdAt"),
                r.getTimestamp("modifiedAt"),
                (UUID) r.getObject("createdBy"),
                (UUID) r.getObject("modifiedBy"));
    }
}
