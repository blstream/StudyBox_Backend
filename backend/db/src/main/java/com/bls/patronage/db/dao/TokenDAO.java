package com.bls.patronage.db.dao;

import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.mapper.ResetPasswordTokenMapper;
import com.bls.patronage.db.model.ResetPasswordToken;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RegisterMapper(ResetPasswordTokenMapper.class)
abstract public class TokenDAO {

    @SqlQuery("select token,isActive,email,expirationDate from passwordTokens " +
            "where token = :token and isActive = 'true' and email = :email")
    abstract ResetPasswordToken find(@Bind("token") UUID token,@Bind("email") String email);

    @SqlUpdate("insert into passwordTokens values (:token, :isActive, :email, :expirationDate)")
    abstract void insert(@BindBean ResetPasswordToken token);

    @SqlUpdate("delete from passwordTokens " +
            "where isActive = 'false' or expirationDate <= NOW()")
    public abstract void deleteExpired();

    @SqlQuery("select token,isActive,email,expirationDate from passwordTokens " +
            "where email = :email and isActive = 'true'")
    public abstract ResetPasswordToken findByEmail(@Bind("email") String email);

    @SqlUpdate("update passwordTokens set isActive = 'false' where token = :token")
    public abstract void deactivate(@Bind("token") UUID token);

    @SqlUpdate("delete from passwordTokens where token = :token")
    public abstract void delete(@Bind("token") UUID token);

    public void createToken(ResetPasswordToken token) {
        final Optional<ResetPasswordToken> optionalToken = Optional.ofNullable(findByEmail(token.getEmail()));

        if(optionalToken.isPresent()) {
            throw new DataAccessException("A token for this user is already generated.");
        }

        insert(token);
    }

    public ResetPasswordToken getToken(UUID token, String email) {

        final Optional<ResetPasswordToken> optionalToken = Optional.ofNullable(find(token,email));
        return optionalToken.orElseThrow(() -> new DataAccessException("Reset password failed."));
    }
}
