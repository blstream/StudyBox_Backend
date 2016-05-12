package com.bls.patronage.resources;

import com.bls.patronage.api.EmailRepresentation;
import com.bls.patronage.api.PasswordChangeRepresentation;
import com.bls.patronage.db.dao.TokenDAO;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.ResetPasswordToken;
import com.bls.patronage.db.model.User;
import com.bls.patronage.service.ResetPasswordService;
import com.bls.patronage.service.TokenService;
import com.bls.patronage.service.configuration.ResetPasswordConfiguration;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static com.bls.patronage.auth.BasicAuthenticator.generateSafeHash;

@Path("/users/password")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResetPasswordResource {
    private final UserDAO userDAO;
    private final TokenDAO tokenDAO;
    private final ResetPasswordConfiguration resetPasswordConfig;

    public ResetPasswordResource(UserDAO userDAO, TokenDAO tokenDAO, ResetPasswordConfiguration resetPasswordConfig) {
        this.userDAO = userDAO;
        this.tokenDAO = tokenDAO;
        this.resetPasswordConfig = resetPasswordConfig;
    }

    @POST
    @Path("/recovery")
    public Response recoverPassword(@Valid EmailRepresentation email) {

        userDAO.getUserByEmail(email.getEmail());
        TokenService tokenService = new ResetPasswordService(resetPasswordConfig);
        ResetPasswordToken token = tokenService.generate(email.getEmail());
        tokenDAO.createToken(token);
        tokenService.sendMessage(email.getEmail(), token.getToken());

        return Response.ok(Collections.singletonMap("token",token.getToken())).status(Response.Status.OK).build();
    }

    @POST
    @Path("/change")
    public Response changePassword(@Valid PasswordChangeRepresentation userInfo) {

        tokenDAO.getToken(userInfo.getToken(), userInfo.getEmail());
        final User user = userDAO.getUserByEmail(userInfo.getEmail());
        userDAO.updatePassword(new User(user.getId(), user.getEmail(), user.getName(),
                generateSafeHash(userInfo.getPassword())));

        tokenDAO.deactivate(userInfo.getToken());

        return Response.ok().status(Response.Status.OK).build();
    }
}
