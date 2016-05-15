package com.bls.patronage.service.configuration;

import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MailConfiguration extends Configuration {

    @Valid
    @NotNull
    private String username;
    @Valid
    @NotNull
    private String password;
    @Valid
    @NotNull
    private String port;
    @Valid
    @NotNull
    private String host;
    @Valid
    @NotNull
    private String fromAddress;
    @Valid
    @NotNull
    private Boolean enableAuth;
    @Valid
    @NotNull
    private Boolean enableTls;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public Boolean getEnableAuth() {
        return enableAuth;
    }

    public Boolean getEnableTls() {
        return enableTls;
    }
}
