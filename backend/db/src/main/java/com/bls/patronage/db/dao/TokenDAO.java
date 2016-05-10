package com.bls.patronage.db.dao;

import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.mapper.ResetPasswordTokenMapper;
import com.bls.patronage.db.model.ResetPasswordToken;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Optional;

@RegisterMapper(ResetPasswordTokenMapper.class)
abstract public class TokenDAO {

    @SqlQuery("select token,isActive,email,expirationDate from passwordTokens where email = :email")
    public abstract ResetPasswordToken findByEmail(@Bind("email") String email);

    @SqlUpdate("insert into passwordTokens values (:token, :isActive, :email, :expirationDate)")
    public abstract void insert(@BindBean ResetPasswordToken token);

    public void createToken(ResetPasswordToken token) {
        final Optional<ResetPasswordToken> optionalToken = Optional.of(findByEmail(token.getEmail()));
        if(optionalToken.get().getIsActive()) {
            throw new DataAccessException("A token for this user is already generated.");
        }
        insert(token);
    }
}
