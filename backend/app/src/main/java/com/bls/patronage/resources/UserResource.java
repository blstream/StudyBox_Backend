package com.bls.patronage.resources;

import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.User;
import com.bls.patronage.db.model.UserWithoutPassword;
import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.UUIDParam;

import javax.annotation.security.PermitAll;
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

    @Path("/{userId}")
    @GET
    public UserWithoutPassword getUser(
            @Valid @PathParam("userId") UUIDParam userId) {
        User user = userDAO.getUserById(userId.get());
        return new UserWithoutPassword(user.getId(), user.getEmail(), user.getName());
    }

    @Path("/me")
    @GET
    public UserWithoutPassword logInUser(@Auth User userCredentials) {
        final User user = userDAO.getUserByEmail(userCredentials.getEmail());
        return new UserWithoutPassword(user.getId(), user.getEmail(), user.getName());
    }
}