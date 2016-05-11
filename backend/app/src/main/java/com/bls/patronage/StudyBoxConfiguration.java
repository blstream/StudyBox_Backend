package com.bls.patronage;

import com.google.common.cache.CacheBuilder;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

public class StudyBoxConfiguration extends Configuration {

    public static final int PW_HASH_SECURITY_LEVEL = 12;

    private static final String DEFAULT_AUTH_CACHE_SPEC = "maximumSize=1000,expireAfterAccess=1h";

    @Valid
    @NotNull
    private URI cvServerURI;

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    public DataSourceFactory getDatabase() {
        return database;
    }

    public CacheBuilder<Object, Object> getAuthCacheBuilder() {
        return CacheBuilder.from(DEFAULT_AUTH_CACHE_SPEC);
    }

    public URI getCvServerURI() {
        return cvServerURI;
    }
}
