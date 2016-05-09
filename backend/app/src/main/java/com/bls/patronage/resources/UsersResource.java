package com.bls.patronage.resources;

import com.bls.patronage.api.UserRepresentation;
import com.bls.patronage.db.dao.UserDAO;
import org.hibernate.validator.constraints.Email;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static com.bls.patronage.auth.BasicAuthenticator.generateSafeHash;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {
    private final UserDAO userDAO;

    public UsersResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @POST
    public Response createUser(@Valid UserRepresentation user) {
        userDAO.createUser(
                user
                        .setId(UUID.randomUUID())
                        .setPassword(generateSafeHash(user.getPassword()))
                        .map()
        );
        return Response.ok(new UserRepresentation(user.map())).status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/password/recovery")
    public Response recoverPassword(@Valid @Email String email) {

        return Response.ok().status(Response.Status.OK).build();
    }

    @POST
    @Path("/password/change")
    public Response changePassword() {

        return Response.ok().status(Response.Status.OK).build();
    }
}
