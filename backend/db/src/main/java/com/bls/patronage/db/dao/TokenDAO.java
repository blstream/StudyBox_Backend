package com.bls.patronage.db.dao;

import com.bls.patronage.db.mapper.ResetPasswordTokenMapper;
import com.bls.patronage.db.model.ResetPasswordToken;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(ResetPasswordTokenMapper.class)
abstract public class TokenDAO {

    @SqlQuery("select token,isActive,email,expirationDate from passwordTokens where email = :email")
    public abstract ResetPasswordToken findByEmail(@Bind("email") String email);

    @SqlUpdate("insert into passwordTokens values (:token, :isActive, :email, :expirationDate)")
    public abstract void create(@BindBean ResetPasswordToken token);
}
