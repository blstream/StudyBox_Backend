package com.bls.patronage.resources;

import com.bls.patronage.api.UserRepresentation;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.User;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/users/{userId}")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserDAO userDAO;

    public UserResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GET
    public User getUser(
            @Valid @PathParam("userId") UUIDParam userId) {
        return userDAO.getUserById(userId.get());
    }

    @DELETE
    public void deleteUser(
            @Valid @PathParam("userId") UUIDParam userId) {
        userDAO.deleteUser(userDAO.getUserById(userId.get()).getId());
    }

    @PUT
    public void updateUser(
            @Valid @PathParam("userId") UUID userId,
            @Valid UserRepresentation user) {
        User updatedUser = new User(userId, user.getEmail(), user.getName(), user.getPassword());
        userDAO.updateUser(updatedUser);
    }
}
