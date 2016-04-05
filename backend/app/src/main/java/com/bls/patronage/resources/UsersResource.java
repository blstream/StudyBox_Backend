package com.bls.patronage.resources;

import com.bls.patronage.api.UserRepresentation;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.User;
import com.bls.patronage.db.model.UserWithoutPassword;

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
        User createdUser = new User(UUID.randomUUID(), user.getEmail(), user.getName(), generateSafeHash(user.getPassword()));
        userDAO.createUser(createdUser);
        return Response.ok(new UserWithoutPassword(createdUser.getId(), createdUser.getEmail(), createdUser.getName())).status(Response.Status.CREATED).build();
    }
}
