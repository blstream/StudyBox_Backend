package com.bls.patronage.db.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import java.util.List;
import java.util.UUID;

public interface StorageDAO {

    @SqlQuery("select dataId from storage where entityId = :entityId")
    List<UUID> getDataIdsFromEntityId(@Bind("entityId") UUID entityId);

    @SqlQuery("select entityId from storage where dataId = :dataId")
    List<UUID> getEntityIdFromDataId(@Bind("dataId") UUID dataId);

    @SqlQuery("insert into storage values (:entityId, :dataId)")
    void createEntry(@Bind("entityId") UUID entityId, @Bind("dataId") UUID dataId);
}
