package com.bls.patronage;

import com.bls.patronage.service.configuration.ResetPasswordConfiguration;
import com.google.common.cache.CacheBuilder;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URL;
import java.nio.file.Path;

public class StudyBoxConfiguration extends Configuration {

    public static final int PW_HASH_SECURITY_LEVEL = 12;

    private static final String DEFAULT_AUTH_CACHE_SPEC = "maximumSize=1000,expireAfterAccess=1h";

    @Valid
    @NotNull
    private URL cvServerURL;

    @Valid
    @NotNull
    private Path fileContentBaseLocation;

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private ResetPasswordConfiguration resetPasswordConfig = new ResetPasswordConfiguration();

    public DataSourceFactory getDatabase() {
        return database;
    }

    public CacheBuilder<Object, Object> getAuthCacheBuilder() {
        return CacheBuilder.from(DEFAULT_AUTH_CACHE_SPEC);
    }

    public ResetPasswordConfiguration getResetPasswordConfig() {
        return resetPasswordConfig;
    }

    public URL getCvServerURL() {
        return cvServerURL;
    }

    public Path getFileContentBaseLocation() {
        return fileContentBaseLocation;
    }
}
