package com.bls.patronage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheBuilder;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class StudyBoxConfiguration extends Configuration {
    @NotNull
    public static final int PW_HASH_SECURITY_LEVEL = 12;
    private static final String DEFAULT_AUTH_CACHE_SPEC = "maximumSize=1000,expireAfterAccess=1h";
    public Boolean authHeaderRequired;
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    public DataSourceFactory getDatabase() {
        return database;
    }

    @JsonProperty
    public Boolean getAuthHeaderRequired() {
        return this.authHeaderRequired;
    }

    @JsonProperty
    public void setAuthHeaderRequired(Boolean authHeaderRequired) {
        this.authHeaderRequired = authHeaderRequired;
    }

    public CacheBuilder<Object, Object> getAuthCacheBuilder() {
        return CacheBuilder.from(DEFAULT_AUTH_CACHE_SPEC);
    }

}
