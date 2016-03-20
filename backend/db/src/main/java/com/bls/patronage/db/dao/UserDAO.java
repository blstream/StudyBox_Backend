package com.bls.patronage.db.dao;

import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.mappers.UserMapper;
import com.bls.patronage.db.mappers.UserWithoutPasswordMapper;
import com.bls.patronage.db.model.Flashcard;
import com.bls.patronage.db.model.User;
import com.bls.patronage.db.model.UserWithoutPassword;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RegisterMapper(UserMapper.class)
abstract public class UserDAO {

    @SqlQuery("select id,email,name,password from users where id = :id")
    abstract User get(@Bind("id") UUID id);

    @RegisterMapper(UserWithoutPasswordMapper.class)
    @SqlQuery("select id,email,name from users")
    public abstract List<UserWithoutPassword> getAllUsers(@Bind("deckId") UUID deckId);

    @GetGeneratedKeys
    @SqlUpdate("insert into users values (:id, :email, :name, :password)")
    public abstract UUID createUser(@BindBean User user);

    @SqlUpdate("update users set email = :email, name = :name, password = :password where id = :id")
    public abstract void updateUser(@BindBean User user);

    @SqlUpdate("delete from users where id = :id")
    public abstract void deleteUser(@Bind("id") UUID id);

    public User getUserById(UUID id) {
        Optional<User> user = Optional.ofNullable(get(id));
        return user.orElseThrow(() -> new DataAccessException("There is no user with specified id"));
    }
}
