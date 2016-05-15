package com.bls.patronage;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.nio.file.Path;

public abstract class StorageBundle<E extends Configuration> implements ConfiguredBundle<E> {

    private StorageService storageService;

    @Override
    public void run(E configuration, Environment environment) throws Exception {
        storageService = new LocalFileService(getStoragePath(configuration));
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    abstract public Path getStoragePath(E configuration);

    public StorageService createStorageService() {
        return storageService;
    }
}
