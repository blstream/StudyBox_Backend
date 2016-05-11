package com.bls.patronage.db.dao;

import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.mapper.AuditMapper;
import com.bls.patronage.db.model.Audit;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RegisterMapper(AuditMapper.class)
abstract public class AuditDAO {

    @SqlQuery("select id,createdAt,modifiedAt,createdBy,modifiedBy from decksAuditFields where id = :id")
    abstract Audit get(@Bind("id") UUID id, @Bind("table") String table);

    @SqlQuery("select id,createdAt,modifiedAt,createdBy,modifiedBy from decksAuditFields")
    abstract Collection<Audit> getAll(@Bind("table") String table);

    @SqlUpdate("insert into decksAuditFields (id,createdAt,modifiedAt,createdBy,modifiedBy) values (:id,:date,:date,:userId,:userId)")
    abstract void insert(@Bind("table") String table, @Bind("id") UUID id, @Bind("date") Date date, @Bind("userId") UUID userId);

    @SqlUpdate("update decksAuditFields set modifiedAt = :date, modifiedBy = :userId where id = :id")
    abstract void update(@Bind("table") String table, @Bind("id") UUID id, @Bind("date") Date date, @Bind("userId") UUID userId);

    public Audit getAudit(UUID id, String table) {
        Optional<Audit> audit = Optional.ofNullable(get(id, table));
        return audit.orElseThrow(() -> new DataAccessException("There is no audit for this id in this table"));
    }

    public Collection<Audit> getAllAudits(String table){
        Collection<Audit> audits = getAll(table);
        return audits;
    }

    public void createAudit(UUID id, UUID userId, String table) {
        insert(table, id, new Date(), userId);
    }

    public void updateAudit(UUID id, UUID userId, String table) {
        update(table, id, new Date(), userId);
    }
}
