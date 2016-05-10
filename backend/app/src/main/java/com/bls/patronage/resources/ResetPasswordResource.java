package com.bls.patronage.resources;

import com.bls.patronage.api.EmailRepresentation;
import com.bls.patronage.api.PasswordChangeRepresentation;
import com.bls.patronage.db.dao.TokenDAO;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.ResetPasswordToken;
import com.bls.patronage.service.ResetPasswordService;
import com.bls.patronage.service.TokenService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/password")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResetPasswordResource {
    private final UserDAO userDAO;
    private final TokenDAO tokenDAO;
    private final String resetPasswordUri;

    public ResetPasswordResource(UserDAO userDAO, TokenDAO tokenDAO, String resetPasswordUri) {
        this.userDAO = userDAO;
        this.tokenDAO = tokenDAO;
        this.resetPasswordUri = resetPasswordUri;
    }

    @POST
    @Path("/recovery")
    public Response recoverPassword(@Valid EmailRepresentation email) {

        userDAO.getUserByEmail(email.getEmail());
        TokenService tokenService = new ResetPasswordService(resetPasswordUri);
        ResetPasswordToken token = tokenService.generate(email.getEmail());
        tokenDAO.createToken(token);
        tokenService.sendMessage(email.getEmail(), token.getToken());

        return Response.ok(token.getToken()).status(Response.Status.OK).build();
    }

    @POST
    @Path("/change")
    public Response changePassword(@Valid PasswordChangeRepresentation user) {

        tokenDAO.getTokenByEmail(user.getEmail());
        tokenDAO.deactivate(user.getToken());

        return Response.ok().status(Response.Status.OK).build();
    }
}
