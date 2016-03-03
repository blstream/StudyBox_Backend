package com.bls.patronage;


import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MyApplication extends Application<MyConfiguration> {

    public static void main(String[] args) throws Exception {
        new MyApplication().run("server", "my-app/configuration.yml");
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

    }
}
