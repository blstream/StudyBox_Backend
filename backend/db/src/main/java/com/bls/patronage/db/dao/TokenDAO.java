package com.bls.patronage.db.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

abstract public class TokenDAO {

    @SqlQuery("select token from passwordTokens where email = :email")
    public abstract String findByEmail(@Bind("email") String email);

    @SqlUpdate("insert into passwordTokens values (:token, :isActive, :email, :expirationDate)")
    public abstract void create();
}
