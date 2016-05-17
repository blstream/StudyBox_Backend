package com.bls.patronage.resources;

import com.bls.patronage.api.UserRepresentation;
import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.model.AuditableEntity;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import java.sql.Timestamp;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest extends BasicAuthenticationTest {

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UserResource(userDAO))
            .build();

    private String userURI;
    private String loggingURI;
    private AuditableEntity auditEntity;

    @Before
    public void setUp() {
        super.setUp();

        userURI = UriBuilder.fromResource(UserResource.class).build().toString()
                + UriBuilder.fromMethod(UserResource.class, "getUser").build(user.getId()).toString();
        loggingURI = UriBuilder.fromResource(UserResource.class).build().toString()
                + UriBuilder.fromMethod(UserResource.class, "logInUser").build().toString();
        auditEntity = new AuditableEntity(UUID.fromString("8ad4b503-5bfc-4d8a-a761-0908374892b1"),
                new Timestamp(new Long("1461219791000")),
                new Timestamp(new Long("1463234622000")),
                user.getId(),
                user.getId());
        user.setAuditFields(auditEntity);
        when(userDAO.getUserAuditFields(user.getId())).thenReturn(auditEntity);
    }

    @Test
    public void getUser() {
        when(userDAO.getUserById(user.getId())).thenReturn(user);
        final UserRepresentation receivedUser = resources.client().target(userURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(UserRepresentation.class);

        verify(userDAO).getUserById(user.getId());
        assertThat(receivedUser.getId()).isEqualTo(user.getId());
        assertThat(receivedUser.getName()).isEqualTo(user.getName());
        assertThat(receivedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void logInUser() {
        when(userDAO.getUserByEmail(user.getEmail())).thenReturn(user);
        final Response response = getResponseWithCredentials(loggingURI, encodedCredentials);
        final UserRepresentation receivedUser = response.readEntity(UserRepresentation.class);

        verify(userDAO).getUserByEmail(user.getEmail());
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(receivedUser.getId()).isEqualTo(user.getId());
        assertThat(receivedUser.getName()).isEqualTo(user.getName());
        assertThat(receivedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void logInUserWithBadPassword() {
        when(userDAO.getUserByEmail(user.getEmail())).thenReturn(user);
        final Response response = getResponseWithCredentials(loggingURI, badPasswordCredentials);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        verify(userDAO).getUserByEmail(user.getEmail());
    }

    @Test
    public void logInUserWithBadEmail() {
        when(userDAO.getUserByEmail(fakeEmail))
                .thenThrow(new DataAccessException(""));
        final Response response = getResponseWithCredentials(loggingURI, badEmailCredentials);

        verify(userDAO).getUserByEmail(fakeEmail);
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }
}
