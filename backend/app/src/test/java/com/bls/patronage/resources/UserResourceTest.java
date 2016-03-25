package com.bls.patronage.resources;

import com.bls.patronage.auth.BasicAuthenticator;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.exception.DataAccessExceptionMapper;
import com.bls.patronage.db.model.User;
import com.bls.patronage.db.model.UserWithoutPassword;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Base64;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {
    private static final UserDAO dao = mock(UserDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UserResource(dao))
            .build();

    @ClassRule
    public static final ResourceTestRule authResources = ResourceTestRule
            .builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addProvider(new DataAccessExceptionMapper())
            .addProvider(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                    .setAuthenticator(new BasicAuthenticator(dao))
                    .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .addResource(new UserResource(dao))
            .build();

    @Captor
    private ArgumentCaptor<User> userCaptor;
    private User user;
    private String userURI;
    private String loggingURI;
    private String encodedCredentials;
    private String badPasswordCredentials;
    private String badEmailCredentials;
    private String fakeEmail;

    @Before
    public void setUp() {
        user = new User(UUID.randomUUID(), "foo@mail.com", "bar",
                BasicAuthenticator.generateSafeHash("Secret"));
        encodedCredentials = Base64.getEncoder().encodeToString("foo@mail.com:Secret" .getBytes());
        badPasswordCredentials = Base64.getEncoder().encodeToString("foo@mail.com:badPass" .getBytes());
        fakeEmail = "asd@mail.com";
        badEmailCredentials = Base64.getEncoder().encodeToString((fakeEmail + ":Secret").getBytes());

        userURI = UriBuilder.fromResource(UserResource.class).build().toString()
                + UriBuilder.fromMethod(UserResource.class, "getUser").build(user.getId()).toString();
        loggingURI = UriBuilder.fromResource(UserResource.class).build().toString()
                + UriBuilder.fromMethod(UserResource.class, "logInUser").build().toString();

    }

    @After
    public void tearDown() {
        reset(dao);
    }

    @Test
    public void getUser() {
        when(dao.getUserById(user.getId())).thenReturn(user);
        final UserWithoutPassword receivedUser = resources.client().target(userURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(UserWithoutPassword.class);

        verify(dao).getUserById(user.getId());
        assertThat(receivedUser.getId()).isEqualTo(user.getId());
        assertThat(receivedUser.getName()).isEqualTo(user.getName());
        assertThat(receivedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void logInUser() {
        when(dao.getUserByEmail(user.getEmail())).thenReturn(user);
        final Response response = getResponseWithCredentials(loggingURI, encodedCredentials);
        UserWithoutPassword receivedUser = response.readEntity(UserWithoutPassword.class);

        verify(dao, times(2)).getUserByEmail(user.getEmail());
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(receivedUser.getId()).isEqualTo(user.getId());
        assertThat(receivedUser.getName()).isEqualTo(user.getName());
        assertThat(receivedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void logInUserWithBadPassword() {
        when(dao.getUserByEmail(user.getEmail())).thenReturn(user);
        final Response response = getResponseWithCredentials(loggingURI, badPasswordCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        verify(dao).getUserByEmail(user.getEmail());
    }

    @Test
    public void logInUserWithBadEmail() {
        when(dao.getUserByEmail(fakeEmail))
                .thenThrow(new DataAccessException(""));
        final Response response = getResponseWithCredentials(loggingURI, badEmailCredentials);

        verify(dao).getUserByEmail(fakeEmail);
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    static private Response getResponseWithCredentials(String uri, String encodedUserInfo) {
        return authResources.getJerseyTest().target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + encodedUserInfo)
                .get();
    }
}
