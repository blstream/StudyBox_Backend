package com.bls.patronage.db.dao;

import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.mapper.UserMapper;
import com.bls.patronage.db.model.User;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Optional;
import java.util.UUID;

@RegisterMapper(UserMapper.class)
abstract public class UserDAO {

    @SqlQuery("select id,email,name,password from users where id = :id")
    abstract User get(@Bind("id") UUID id);

    @SqlQuery("select id,email,name,password from users where email = :email")
    abstract User getByEmail(@Bind("email") String email);

    @GetGeneratedKeys
    @SqlUpdate("insert into users values (:id, :email, :name, :password)")
    abstract UUID create(@BindBean User user);

    @SqlUpdate("update users set password = :password where id = :id")
    public abstract void updatePassword(@BindBean User user);

    public User getUserById(UUID id) {
        Optional<User> user = Optional.ofNullable(get(id));
        return user.orElseThrow(() -> new DataAccessException("There is no user with specified id"));
    }

    public UUID createUser(User user) {
        Optional<User> userOptional = Optional.ofNullable(getByEmail(user.getEmail()));
        if (userOptional.isPresent()) throw new DataAccessException("There is already user with specified email");
        return create(user);

    }

    public User getUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(getByEmail(email));
        return user.orElseThrow(() -> new DataAccessException("There is no user with specified email"));
    }
}
