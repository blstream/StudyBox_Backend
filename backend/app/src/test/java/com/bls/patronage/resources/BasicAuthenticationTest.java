package com.bls.patronage.resources;


import com.bls.patronage.auth.BasicAuthenticator;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.exception.DataAccessExceptionMapper;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Base64;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

public class BasicAuthenticationTest {
    protected static final UserDAO userDAO = mock(UserDAO.class);
    protected static final DeckDAO deckDao = mock(DeckDAO.class);

    @ClassRule
    public static final ResourceTestRule authResources = ResourceTestRule
            .builder()
            .addProvider(new DataAccessExceptionMapper())
            .addProvider(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                    .setAuthenticator(new BasicAuthenticator(userDAO))
                    .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .addResource(new UserResource(userDAO))
            .addResource(new DecksResource(deckDao))
            .addResource(new DeckResource(deckDao))
            .build();

    protected User user;
    protected String encodedCredentials;
    protected String badPasswordCredentials;
    protected String badEmailCredentials;
    protected String fakeEmail;

    static protected Response getResponseWithCredentials(String uri, String encodedUserInfo) {
        return authResources.client().target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + encodedUserInfo)
                .get();
    }

    @Before
    public void setUp() {
        user = new User(UUID.randomUUID(), "foo@mail.com", "bar",
                BasicAuthenticator.generateSafeHash("Secret"));
        encodedCredentials = Base64.getEncoder().encodeToString("foo@mail.com:Secret" .getBytes());
        badPasswordCredentials = Base64.getEncoder().encodeToString("foo@mail.com:badPass" .getBytes());
        fakeEmail = "asd@mail.com";
        badEmailCredentials = Base64.getEncoder().encodeToString((fakeEmail + ":Secret").getBytes());
    }

    @After
    public void tearDown() {
        reset(userDAO);
        reset(deckDao);
    }

    @Test
    public void logInAsAnonymousUser() {
        String testURI = UriBuilder
                .fromResource(UserResource.class)
                .build().toString() + UriBuilder.fromMethod(UserResource.class, "logInUser").build().toString();
        Response response = authResources.client().target(testURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK);
    }
}
