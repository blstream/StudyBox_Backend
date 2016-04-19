package com.bls.patronage.resources;

import com.bls.patronage.api.UserRepresentation;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserDAO userDAO;

    public UserResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Path("/{userId: [0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}}")
    @GET
    public UserRepresentation getUser(
            @Valid @PathParam("userId") UUIDParam userId) {

        return new UserRepresentation(userDAO.getUserById(userId.get()));
    }

    @Path("/me")
    @GET
    public UserRepresentation logInUser(@Auth User user) {
        return new UserRepresentation(user);
    }
}