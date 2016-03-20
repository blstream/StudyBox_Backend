package com.bls.patronage.resources;

import com.bls.patronage.api.UserRepresentation;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.User;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {
    private static final UserDAO dao = mock(UserDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UserResource(dao))
            .build();
    @Captor
    private ArgumentCaptor<User> userCaptor;
    private User user;
    private UserRepresentation userRepresentation;
    private String userURI;

    @Before
    public void setUp() {
        user = new User(UUID.randomUUID(), "foo@mail.com", "bar", "secretSecret");
        userRepresentation = new UserRepresentation("goo@mail.com", "baz", "secretPassword");
        userURI = UriBuilder.fromResource(UserResource.class).build(user.getId()).toString();
    }

    @After
    public void tearDown() {
        reset(dao);
    }

    @Test
    public void updateUser() {
        final Response response = resources.client().target(userURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(userRepresentation, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void deleteUser() {
        when(dao.getUserById(user.getId())).thenReturn(user);
        final Response response = resources.client().target(userURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void getUser() {
        when(dao.getUserById(user.getId())).thenReturn(user);
        final User recievedFlashcard = resources.client().target(userURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(User.class);

        verify(dao).getUserById(user.getId());
        assertThat(recievedFlashcard).isEqualTo(user);
    }
}