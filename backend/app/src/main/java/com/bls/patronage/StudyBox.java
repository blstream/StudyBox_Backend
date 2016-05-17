package com.bls.patronage;

import com.bls.patronage.auth.BasicAuthenticator;
import com.bls.patronage.auth.PreAuthenticationFilter;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.dao.ResultDAO;
import com.bls.patronage.db.dao.StorageDAO;
import com.bls.patronage.db.dao.TipDAO;
import com.bls.patronage.db.dao.TokenDAO;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.User;
import com.bls.patronage.mapper.DataAccessExceptionMapper;
import com.bls.patronage.mapper.PasswordResetExceptionMapper;
import com.bls.patronage.mapper.StorageExceptionMapper;
import com.bls.patronage.resources.DeckResource;
import com.bls.patronage.resources.DecksCvMagicResource;
import com.bls.patronage.resources.DecksResource;
import com.bls.patronage.resources.FlashcardResource;
import com.bls.patronage.resources.FlashcardsResource;
import com.bls.patronage.resources.ResetPasswordResource;
import com.bls.patronage.resources.ResultsResource;
import com.bls.patronage.resources.StorageResource;
import com.bls.patronage.resources.TipResource;
import com.bls.patronage.resources.TipsResource;
import com.bls.patronage.resources.UserResource;
import com.bls.patronage.resources.UsersResource;
import com.bls.patronage.task.TokenExpirationTask;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.CachingAuthenticator;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.skife.jdbi.v2.DBI;

import java.nio.file.Path;

public class StudyBox extends Application<StudyBoxConfiguration> {

    private static final String APP_NAME = "backend";
    private static final String HEALTH_CHECK_DATABASE_NAME = "database";

    private final StorageBundle<StudyBoxConfiguration> storageBundle = new StorageBundle<StudyBoxConfiguration>() {
        @Override
        public Path getStoragePath(StudyBoxConfiguration configuration) {
            //Return local storage path
            return configuration.getFileContentBaseLocation();
        }
    };

    public static void main(String[] args) throws Exception {
        new StudyBox().run(args);
    }

    @Override
    public String getName() {
        return APP_NAME;
    }

    @Override
    public void initialize(Bootstrap<StudyBoxConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(new MigrationsBundle<StudyBoxConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(StudyBoxConfiguration configuration) {
                return configuration.getDatabase();
            }
        });

        bootstrap.addBundle(storageBundle);
        bootstrap.addBundle(new MultiPartBundle());
    }

    @Override
    public void run(StudyBoxConfiguration configuration, Environment environment) throws Exception {

        final DBI jdbi = new DBIFactory().build(environment, configuration.getDatabase(), HEALTH_CHECK_DATABASE_NAME);

        // DAOs
        final FlashcardDAO flashcardDAO = jdbi.onDemand(FlashcardDAO.class);
        final DeckDAO decksDAO = jdbi.onDemand(DeckDAO.class);
        final UserDAO userDAO = jdbi.onDemand(UserDAO.class);
        final TokenDAO tokenDAO = jdbi.onDemand(TokenDAO.class);
        final TipDAO tipDAO = jdbi.onDemand(TipDAO.class);
        final ResultDAO resultDAO = jdbi.onDemand(ResultDAO.class);
        final StorageDAO storageDAO = jdbi.onDemand(StorageDAO.class);

        // Jersey clients
        final JerseyWebTarget cvServer = JerseyClientBuilder.createClient().target(configuration.getCvServerURL().toString());

        // services
        final StorageService storageService = storageBundle.createStorageService();

        // resources
        environment.jersey().register(new DeckResource(decksDAO));
        environment.jersey().register(new DecksResource(decksDAO));
        environment.jersey().register(new FlashcardResource(flashcardDAO, storageService, storageDAO));
        environment.jersey().register(new FlashcardsResource(flashcardDAO));
        environment.jersey().register(new UserResource(userDAO));
        environment.jersey().register(new UsersResource(userDAO));
        environment.jersey().register(new ResetPasswordResource(userDAO, tokenDAO, configuration.getResetPasswordConfig()));
        environment.jersey().register(new TipResource(tipDAO, storageService, storageDAO));
        environment.jersey().register(new TipsResource(tipDAO));
        environment.jersey().register(new ResultsResource(flashcardDAO, resultDAO));
        environment.jersey().register(new DecksCvMagicResource(storageService, decksDAO, flashcardDAO, cvServer));
        environment.jersey().register(new StorageResource(storageService));

        // Exception mappers
        environment.jersey().register(new DataAccessExceptionMapper());
        environment.jersey().register(new PasswordResetExceptionMapper());
        environment.jersey().register(new StorageExceptionMapper());

        // authentication
        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new CachingAuthenticator(environment.metrics(),
                        new BasicAuthenticator(userDAO), configuration.getAuthCacheBuilder()))
                .buildAuthFilter()));

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(PreAuthenticationFilter.class);

        // tasks
        environment.admin().addTask(new TokenExpirationTask(tokenDAO));
    }
}
