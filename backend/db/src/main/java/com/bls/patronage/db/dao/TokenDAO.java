package com.bls.patronage.db.dao;

import com.bls.patronage.db.model.Token;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

abstract public class TokenDAO {

    @SqlQuery("select token,isActive,email,expirationDate from passwordTokens where email = :email")
    public abstract Token findByEmail(@Bind("email") String email);

    @SqlUpdate("insert into passwordTokens values (:token, :isActive, :email, :expirationDate)")
    public abstract void create(@BindBean Token token);
}
