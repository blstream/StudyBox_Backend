package com.bls.patronage.task;

import com.bls.patronage.db.dao.TokenDAO;
import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;

import java.io.PrintWriter;

public class TokenExpirationTask extends Task {
    private final TokenDAO tokenDAO;

    public TokenExpirationTask(TokenDAO tokenDAO) {
        super("tokenExpiration");
        this.tokenDAO = tokenDAO;
    }

    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        tokenDAO.deleteExpired();
    }
}

