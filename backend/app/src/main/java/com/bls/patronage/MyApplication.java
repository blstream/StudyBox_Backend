package com.bls.patronage;


import com.bls.patronage.dao.FlashcardDAO;
import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class MyApplication extends Application<MyConfiguration> {

    public static void main(String[] args) throws Exception {
        new MyApplication().run("server", "configuration.yml");
    }

    @Override
    public String getName() {
        return "StudyBox-Backend";
    }

    @Override
    public void initialize(Bootstrap<MyConfiguration> bootstrap) {
    }


    @Override
    public void run(MyConfiguration myConfiguration, Environment environment) throws Exception {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, myConfiguration.getDataSourceFactory(), "postgresql");
        final FlashcardDAO dao = jdbi.onDemand(FlashcardDAO.class);
        environment.jersey().register(new FlashcardDAO(dao));
    }
}
