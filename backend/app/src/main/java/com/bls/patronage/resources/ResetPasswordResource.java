package com.bls.patronage.resources;

import com.bls.patronage.api.PasswordChangeRepresentation;
import com.bls.patronage.db.dao.TokenDAO;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.service.ResetPasswordService;
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

    public ResetPasswordResource(UserDAO userDAO, TokenDAO tokenDAO) {
        this.userDAO = userDAO;
        this.tokenDAO = tokenDAO;
    }

    @POST
    @Path("/recovery")
    public Response recoverPassword(@Valid @Email String email) {
        userDAO.getUserByEmail(email);
        ResetPasswordService service = new ResetPasswordService();
        tokenDAO.createToken(service.generate(email));
        service.sendMessage(email);

        return Response.ok().status(Response.Status.OK).build();
    }

    @POST
    @Path("/change")
    public Response changePassword(@Valid PasswordChangeRepresentation user) {

        return Response.ok().status(Response.Status.OK).build();
    }
}
