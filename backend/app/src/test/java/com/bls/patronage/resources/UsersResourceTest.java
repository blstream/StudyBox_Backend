package com.bls.patronage.resources;

import com.bls.patronage.api.UserRepresentation;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.Deck;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UsersResourceTest extends BasicAuthenticationTest {
    private static final UserDAO userDAO = mock(UserDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UsersResource(userDAO))
            .build();

    @Captor
    private ArgumentCaptor<User> userCaptor;
    private String usersURI;
    private User user;
    private String decksURI;
    private String deckURI;

    static private Response postUser(String uri, String email, String name, String password) {
        return resources.client().target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(new UserRepresentation(email, name, password), MediaType.APPLICATION_JSON_TYPE));
    }

    @Before
    public void setUp() {
        usersURI = UriBuilder.fromResource(UsersResource.class).build().toString();
        user = new User("12345678-9012-3456-7890-123456789012", "asd@mail.com", "asd", "12345678");
        decksURI = UriBuilder.fromResource(DecksResource.class).toString();
        deckURI = UriBuilder.fromResource(DeckResource.class).build(UUID.randomUUID()).toString();
    }

    @After
    public void cleanUp() {
        reset(userDAO);
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

    @Test
    public void createUserWithNoName() {
        Response response = postUser(usersURI, "zxc", "", user.getPassword());
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(422);
    }

    @Test
    public void logInAsAnonymousUser() throws Exception {
        String testURI = UriBuilder
                .fromResource(UserResource.class)
                .build().toString() + UriBuilder.fromMethod(UserResource.class, "logInUser").build().toString();
        Response response = authResources.client().target(testURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode()
        );
    }

    @Test
    public void AnonymousUserCanGet() throws Exception {
        Response response = authResources.client().target(decksURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    public void AnonymousUserCantPost() throws Exception {
        Response response = authResources.client().target(decksURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new UserRepresentation("test@foo.baz", "bar")));

        assertThat(response.getStatus()).isEqualTo(Response.Status.FORBIDDEN.getStatusCode()
        );
    }

    @Test
    public void AnonymousUserCanPostNewUser() throws Exception {
        Response response = resources.client().target(usersURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new UserRepresentation("test@foo.baz", "fooz", "barbarbar")));

        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
    }

    @Test
    public void AnonymousUserCantDelete() throws Exception {

        Response response = authResources.client().target(deckURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();

        assertThat(response.getStatus()).isEqualTo(Response.Status.FORBIDDEN.getStatusCode()
        );
    }

    @Test
    public void AnonymousUserCantUpdate() throws Exception {
        Response response = authResources.client().target(decksURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new Deck(UUID.randomUUID(), "foo")));

        assertThat(response.getStatus()).isEqualTo(Response.Status.FORBIDDEN.getStatusCode()
        );
    }
}
