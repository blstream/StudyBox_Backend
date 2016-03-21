package com.bls.patronage.resources;

import com.bls.patronage.api.UserRepresentation;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.User;
import com.bls.patronage.db.model.UserWithoutPassword;
import com.google.common.collect.ImmutableList;
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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsersResourceTest {
    private static final UserDAO dao = mock(UserDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UsersResource(dao))
            .build();
    @Captor
    private ArgumentCaptor<User> userCaptor;
    private User user;
    private UserWithoutPassword userWithoutPassword;
    private UserRepresentation userRepresentation;
    private String userURI;

    @Before
    public void setUp() {
        user = new User(UUID.randomUUID(), "foo@mail.com", "bar", "secretSecret");
        userWithoutPassword = new UserWithoutPassword(UUID.randomUUID(), "foo@mail.com", "bar");
        userRepresentation = new UserRepresentation("goo@mail.com", "baz", "secretPassword");
        userURI = UriBuilder.fromResource(UsersResource.class).build().toString();
    }

    @After
    public void tearDown() {
        reset(dao);
    }

    @Test
    public void createUser() {
        final Response response = resources.client().target(userURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(userRepresentation, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(dao).createUser(userCaptor.capture());
        assertThat(userCaptor.getValue().getId()).isNotNull();
        assertThat(userCaptor.getValue().getEmail()).isEqualTo(userRepresentation.getEmail());
        assertThat(userCaptor.getValue().getName()).isEqualTo(userRepresentation.getName());
        assertThat(userCaptor.getValue().getPassword()).isEqualTo(userRepresentation.getPassword());
    }

    @Test
    public void createUserWithNoDataProvided() {
        final Response response = resources.client().target(userURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(new UserRepresentation("", "", ""), MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus()).isEqualTo(422);
    }
}