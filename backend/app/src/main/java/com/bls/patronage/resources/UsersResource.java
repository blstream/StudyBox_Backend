package com.bls.patronage.resources;

import com.bls.patronage.api.UserRepresentation;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.User;
import com.bls.patronage.db.model.UserWithoutPassword;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {
    private final UserDAO userDAO;

    public UsersResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @POST
    public User createUser(@Valid UserRepresentation user) {
        User createdUser = new User(UUID.randomUUID(), user.getEmail(), user.getName(), user.getPassword());
        userDAO.createUser(createdUser);
        return createdUser;
    }

}
