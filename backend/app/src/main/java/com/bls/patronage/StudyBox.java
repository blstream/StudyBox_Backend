package com.bls.patronage;

import com.bls.patronage.auth.BasicAuthenticator;
import com.bls.patronage.auth.PreAuthenticationFilter;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.dao.ResultDAO;
import com.bls.patronage.db.dao.TipDAO;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.User;
import com.bls.patronage.helpers.FileHelper;
import com.bls.patronage.mapper.DataAccessExceptionMapper;
import com.bls.patronage.resources.DeckResource;
import com.bls.patronage.resources.DecksResource;
import com.bls.patronage.resources.FilesResource;
import com.bls.patronage.resources.FlashcardResource;
import com.bls.patronage.resources.FlashcardsResource;
import com.bls.patronage.resources.ResultsResource;
import com.bls.patronage.resources.TipResource;
import com.bls.patronage.resources.TipsResource;
import com.bls.patronage.resources.UserResource;
import com.bls.patronage.resources.UsersResource;
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
import org.skife.jdbi.v2.DBI;

import java.net.URI;

public class StudyBox extends Application<StudyBoxConfiguration> {

    private static final String APP_NAME = "backend";
    private static final String HEALTH_CHECK_DATABASE_NAME = "database";

    private final StreamPersistenceBundle<StudyBoxConfiguration> streamPersistenceBundle = new StreamPersistenceBundle<StudyBoxConfiguration>() {
        @Override
        public URI getServiceURI(final StudyBoxConfiguration configuration) {
            //Return CV server URI
            return configuration.getCvServerURI();
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

        bootstrap.addBundle(streamPersistenceBundle);
        bootstrap.addBundle(new MultiPartBundle());
    }

    @Override
    public void run(StudyBoxConfiguration configuration, Environment environment) throws Exception {
        final DBI jdbi = new DBIFactory().build(environment, configuration.getDatabase(), HEALTH_CHECK_DATABASE_NAME);
        environment.jersey().register(new DeckResource(jdbi.onDemand(DeckDAO.class)));
        environment.jersey().register(new DecksResource(jdbi.onDemand(DeckDAO.class)));
        environment.jersey().register(new FlashcardResource(jdbi.onDemand(FlashcardDAO.class)));
        environment.jersey().register(new FlashcardsResource(jdbi.onDemand(FlashcardDAO.class)));
        environment.jersey().register(new UserResource(jdbi.onDemand(UserDAO.class)));
        environment.jersey().register(new UsersResource(jdbi.onDemand(UserDAO.class)));
        environment.jersey().register(new TipResource(jdbi.onDemand(TipDAO.class)));
        environment.jersey().register(new TipsResource(jdbi.onDemand(TipDAO.class)));
        environment.jersey().register(new ResultsResource(jdbi.onDemand(FlashcardDAO.class),
                jdbi.onDemand(ResultDAO.class)));
        environment.jersey().register(new FilesResource(new FileHelper(streamPersistenceBundle), configuration.getFilesBaseLocation(),
                jdbi.onDemand(DeckDAO.class), jdbi.onDemand(FlashcardDAO.class)));
        environment.jersey().register(new DataAccessExceptionMapper());

        final BasicAuthenticator basicAuthenticator = new BasicAuthenticator(jdbi.onDemand(UserDAO.class));
        final CachingAuthenticator cachingAuthenticator = new CachingAuthenticator(environment.metrics(), basicAuthenticator,
                configuration.getAuthCacheBuilder());

        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(cachingAuthenticator)
                        .buildAuthFilter()));

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(PreAuthenticationFilter.class);
    }
}
