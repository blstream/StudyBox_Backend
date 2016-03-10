package com.bls.patronage;

import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.resources.DeckResource;
import com.bls.patronage.resources.DecksResource;
import com.bls.patronage.resources.FlashcardResource;
import com.bls.patronage.resources.FlashcardsResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class StudyBox extends Application<StudyBoxConfiguration> {

    private static final String APP_NAME = "backend";
    private static final String HEALTH_CHECK_DATABASE_NAME = "database";

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
    }

    @Override
    public void run(StudyBoxConfiguration configuration, Environment environment) throws Exception {
        final DBI jdbi = new DBIFactory().build(environment, configuration.getDatabase(), HEALTH_CHECK_DATABASE_NAME);
        environment.jersey().register(new DeckResource(jdbi.onDemand(DeckDAO.class)));
        environment.jersey().register(new DecksResource(jdbi.onDemand(DeckDAO.class)));
        environment.jersey().register(new FlashcardResource(jdbi.onDemand(FlashcardDAO.class)));
        environment.jersey().register(new FlashcardsResource(jdbi.onDemand(FlashcardDAO.class)));

    }
}
