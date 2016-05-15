package com.bls.patronage.db.dao;

import com.bls.patronage.db.mapper.AuditableEntityMapper;
import com.bls.patronage.db.model.AuditableEntity;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Date;
import java.util.UUID;

abstract public class AuditDAO {

    @SqlUpdate("update decks set createdAt = :date, modifiedAt = :date, createdBy = :userId, modifiedBy = :userId where id = :id")
    abstract void createAudit(@Bind("table") String table, @Bind("id") UUID id, @Bind("date") Date date, @Bind("userId") UUID userId);


    @SqlUpdate("update decks set modifiedAt = :date, modifiedBy = :userId where id = :id")
    abstract void updateAudit(@Bind("table") String table, @Bind("id") UUID id, @Bind("date") Date date, @Bind("userId") UUID userId);

    @RegisterMapper(AuditableEntityMapper.class)
    @SqlQuery("select createdAt, modifiedAt, createdBy, modifiedBy from decks where id = :id")
    public abstract AuditableEntity getAuditFields(@Bind("id") UUID id);

}
