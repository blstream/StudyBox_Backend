package com.bls.patronage.resources;

import com.bls.patronage.api.UserRepresentation;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.User;
import io.dropwizard.testing.junit.ResourceTestRule;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UsersResourceTest {
    private static final UserDAO userDAO = mock(UserDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UsersResource(userDAO))
            .build();

    @Captor
    private ArgumentCaptor<User> userCaptor;
    private String usersURI;
    private User user;

    @Before
    public void setUp() {
        usersURI = UriBuilder.fromResource(UsersResource.class).build().toString();
        user = new User("12345678-9012-3456-7890-123456789012", "asd@mail.com", "asd", "12345678");
    }

    @Test
    public void createUser() {
        Response response = postUser(usersURI, user.getEmail(), user.getName(), user.getPassword());
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CREATED);
        verify(userDAO).createUser(userCaptor.capture());
        assertThat(userCaptor.getValue().getId()).isNotNull();
        assertThat(userCaptor.getValue().getName()).isEqualTo(user.getName());
        assertThat(userCaptor.getValue().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void createUserWithTooShortPassword() {
        Response response = postUser(usersURI, user.getEmail(), user.getName(), "1234567");
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(422);
    }

    @Test
    public void createUserWithBadEmail() {
        Response response = postUser(usersURI, "zxc", user.getName(), user.getPassword());
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(422);
    }

    static private Response postUser(String uri, String email, String name, String password) {
        return resources.client().target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(new UserRepresentation(email, name, password), MediaType.APPLICATION_JSON_TYPE));
    }
}
