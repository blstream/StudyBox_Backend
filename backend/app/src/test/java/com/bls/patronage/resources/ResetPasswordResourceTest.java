package com.bls.patronage.resources;

import com.bls.patronage.api.EmailRepresentation;
import com.bls.patronage.api.PasswordChangeRepresentation;
import com.bls.patronage.db.dao.TokenDAO;
import com.bls.patronage.db.exception.DataAccessException;
import com.bls.patronage.db.model.ResetPasswordToken;
import com.bls.patronage.db.model.User;
import com.bls.patronage.mapper.DataAccessExceptionMapper;
import com.bls.patronage.service.configuration.ResetPasswordConfiguration;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResetPasswordResourceTest extends BasicAuthenticationTest{

    private static final TokenDAO tokenDAO = mock(TokenDAO.class);
//    private static final ResetPasswordConfiguration mailConfig = mock(ResetPasswordConfiguration.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new ResetPasswordResource(userDAO, tokenDAO, new ResetPasswordConfiguration()))
            .addProvider(new DataAccessExceptionMapper())
            .build();

    @Captor
    private ArgumentCaptor<ResetPasswordToken> tokenCaptor;
    @Captor
    private ArgumentCaptor<User> userCaptor;
    private String recoveryURI;
    private String changeURI;
    private ResetPasswordToken token;
    private ResetPasswordToken deactivatedToken;
    private String newPassword;

    static private <T> Response postUserInfo(String uri, T entity) {
        return resources.client().target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(entity,
                        MediaType.APPLICATION_JSON_TYPE));
    }

    @Before
    public void setUp() {
        super.setUp();
        recoveryURI = UriBuilder.fromResource(ResetPasswordResource.class).build().toString()
            + UriBuilder.fromMethod(ResetPasswordResource.class, "recoverPassword");
        changeURI = UriBuilder.fromResource(ResetPasswordResource.class).build().toString()
                + UriBuilder.fromMethod(ResetPasswordResource.class, "changePassword");
        token = new ResetPasswordToken(UUID.randomUUID(), user.getEmail(), new Date(), true);
        deactivatedToken = new ResetPasswordToken(UUID.randomUUID(), user.getEmail(), new Date(), false);
        newPassword = "newSecret";

        when(userDAO.getUserByEmail(user.getEmail())).thenReturn(user);
        when(userDAO.getUserByEmail(fakeEmail)).thenThrow(new DataAccessException(""));

//        when(mailConfig.getMail().getEnableAuth()).thenReturn(true);
//        when(mailConfig.getMail().getEnableTls()).thenReturn(true);
//        when(mailConfig.getMail().getPort()).thenReturn("587");
//        when(mailConfig.getMail().getPort()).thenReturn("587");
    }

    @After
    public void tearDown() {
        reset(userDAO);
        reset(tokenDAO);
    }

    @Test
    public void passwordRecovery(){

        try {
            final Response response = postUserInfo(recoveryURI, new EmailRepresentation(user.getEmail()));
        } catch (ProcessingException e) {}

        verify(tokenDAO).createToken(tokenCaptor.capture());
        assertThat(tokenCaptor.getValue().getToken()).isNotNull();
        assertThat(tokenCaptor.getValue().getIsActive()).isTrue();
        assertThat(tokenCaptor.getValue().getEmail()).isEqualTo(user.getEmail());
        assertThat(tokenCaptor.getValue().getExpirationDate()).isNotNull();
//        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    public void passwordRecoveryWithBadEmail(){

        final Response response  = postUserInfo(recoveryURI, new EmailRepresentation(fakeEmail));

        verify(tokenDAO, never()).createToken(tokenCaptor.capture());
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void passwordChange(){
        when(tokenDAO.getToken(token.getToken(), user.getEmail())).thenReturn(token);

        final Response response  = postUserInfo(changeURI,
                new PasswordChangeRepresentation(user.getEmail(), newPassword, token.getToken()));

        verify(userDAO).updatePassword(userCaptor.capture());
        verify(tokenDAO).deactivate(token.getToken());
        assertThat(userCaptor.getValue().getId()).isEqualTo(user.getId());
        assertThat(userCaptor.getValue().getEmail()).isEqualTo(user.getEmail());
        assertThat(userCaptor.getValue().getName()).isEqualTo(user.getName());
        assertThat(BCrypt.checkpw(newPassword, userCaptor.getValue().getPassword())).isTrue();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    public void passwordChangeWithBadEmail(){

        final Response response  = postUserInfo(changeURI,
                new PasswordChangeRepresentation(fakeEmail, newPassword, token.getToken()));

        verify(userDAO, never()).updatePassword(userCaptor.capture());
        verify(tokenDAO, never()).deactivate(token.getToken());
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void passwordChangeWithDeactivatedToken(){
        when(tokenDAO.getToken(deactivatedToken.getToken(), user.getEmail())).thenThrow(new DataAccessException(""));

        final Response response  = postUserInfo(changeURI,
                new PasswordChangeRepresentation(user.getEmail(), newPassword, deactivatedToken.getToken()));

        verify(userDAO, never()).updatePassword(userCaptor.capture());
        verify(tokenDAO, never()).deactivate(deactivatedToken.getToken());
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void passwordChangeWithNonExistentToken(){
        when(tokenDAO.getToken(token.getToken(), user.getEmail())).thenThrow(new DataAccessException(""));

        final Response response  = postUserInfo(changeURI,
                new PasswordChangeRepresentation(user.getEmail(), newPassword, token.getToken()));

        verify(userDAO, never()).updatePassword(userCaptor.capture());
        verify(tokenDAO, never()).deactivate(token.getToken());
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }
}
