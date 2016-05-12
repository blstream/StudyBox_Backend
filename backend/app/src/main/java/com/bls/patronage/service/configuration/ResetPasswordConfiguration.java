package com.bls.patronage.service.configuration;

import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ResetPasswordConfiguration extends Configuration {

    @Valid
    @NotNull
    private String resetPasswordUrl;

    @Valid
    @NotNull
    private MailConfiguration mail;

    public String getResetPasswordUrl() {
        return resetPasswordUrl;
    }

    public MailConfiguration getMail() {
        return mail;
    }
}
