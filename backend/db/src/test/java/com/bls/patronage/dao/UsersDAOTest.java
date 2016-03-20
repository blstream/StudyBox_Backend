package com.bls.patronage.dao;

import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.Flashcard;
import com.bls.patronage.db.model.User;
import com.bls.patronage.db.model.UserWithoutPassword;
import org.junit.Test;
import org.skife.jdbi.v2.Handle;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UsersDAOTest extends DAOTest {

    private User userExample1;
    private User userExample2;
    private UserDAO dao;

    @Override
    public void setUp() throws Exception {
        userExample1 = new User(UUID.randomUUID(), "foo@bar.com", "name1", "password1");
        userExample2 = new User(UUID.randomUUID(), "goo@baz.com", "name2", "password2");

        super.setUp();
        dao = dbi.open(UserDAO.class);
    }

    @Override
    protected void setUpDatabaseContent(Handle handle) {
        handle.createCall("DROP TABLE users IF EXISTS").invoke();
        handle.createCall(
                "CREATE TABLE users (id uuid primary key, email text not null, name text not null, password text not null)")
                .invoke();
        handle.createStatement("INSERT INTO users VALUES (?, ?, ?, ?)")
                .bind(0, userExample1.getId())
                .bind(1, userExample1.getEmail())
                .bind(2, userExample1.getName())
                .bind(3, userExample1.getPassword())
                .execute();
        handle.createStatement("INSERT INTO users VALUES (?, ?, ?, ?)")
                .bind(0, userExample2.getId())
                .bind(1, userExample2.getEmail())
                .bind(2, userExample2.getName())
                .bind(3, userExample1.getPassword())
                .execute();
    }

    @Test
    public void getAllUsers() {
        final List<UserWithoutPassword> users = dao.getAllUsers();
        UserWithoutPassword userWithoutPasswordExample1 = new UserWithoutPassword(userExample1.getId(), userExample1.getEmail(), userExample1.getName());
        UserWithoutPassword userWithoutPasswordExample2 = new UserWithoutPassword(userExample2.getId(), userExample2.getEmail(), userExample2.getName());
        assertThat(users).containsSequence(userWithoutPasswordExample1, userWithoutPasswordExample2);
    }

    @Test
    public void getUserById() {
        final User userById = dao.getUserById(userExample1.getId());
        assertThat(userById).isEqualTo(userExample1);
    }

    @Test
    public void createUser() {
        final User user = new User(UUID.randomUUID(), "foos@mail.com", "bars", "mypassword2");
        dao.createUser(user);
        assertThat(dao.getUserById(user.getId())).isEqualTo(user);
    }

    @Test
    public void deleteUser() {
        dao.deleteUser(userExample2.getId());
        assertThat(dao.getAllUsers()).doesNotContain(userExample2);
    }

    @Test
    public void updateUser() {
        final User user = new User(userExample1.getId(), "goos@mail.com", "bazz", "supersecret2");
        dao.updateUser(user);
        assertThat(dao.getUserById(userExample1.getId())).isEqualTo(user);
    }
}
