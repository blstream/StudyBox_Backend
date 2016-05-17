package com.bls.patronage.resources;

import com.bls.patronage.StorageService;
import com.bls.patronage.auth.BasicAuthenticator;
import com.bls.patronage.auth.PreAuthenticationFilter;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.dao.ResultDAO;
import com.bls.patronage.db.dao.StorageDAO;
import com.bls.patronage.db.dao.TipDAO;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.User;
import com.bls.patronage.mapper.DataAccessExceptionMapper;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Base64;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class BasicAuthenticationTest {
    protected static final UserDAO userDAO = mock(UserDAO.class);
    protected static final DeckDAO deckDao = mock(DeckDAO.class);
    protected static final ResultDAO resultDAO = mock(ResultDAO.class);
    protected static final FlashcardDAO flashcardDAO = mock(FlashcardDAO.class);
    protected static final TipDAO tipDAO = mock(TipDAO.class);
    protected static final StorageDAO storageDAO = mock(StorageDAO.class);
    protected static final StorageService storageService = mock(StorageService.class);

    @ClassRule
    public static final ResourceTestRule authResources = ResourceTestRule
            .builder()
            .addProvider(new DataAccessExceptionMapper())
            .addProvider(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                    .setAuthenticator(new BasicAuthenticator(userDAO))
                    .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .addProvider(PreAuthenticationFilter.class)
            .addProvider(MultiPartFeature.class)
            .addResource(new UserResource(userDAO))
            .addResource(new DecksResource(deckDao))
            .addResource(new DeckResource(deckDao))
            .addResource(new TipResource(tipDAO, storageService, storageDAO))
            .addResource(new FlashcardResource(flashcardDAO, storageService, storageDAO))
            .addResource(new StorageResource(storageService))
            .addResource(new ResultsResource(flashcardDAO, resultDAO))
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

        when(userDAO.getUserByEmail(user.getEmail())).thenReturn(user);
    }

    @After
    public void tearDown() {
        reset(userDAO);
        reset(deckDao);
        reset(resultDAO);
        reset(tipDAO);
        reset(storageService);
        reset(flashcardDAO);
    }
}
