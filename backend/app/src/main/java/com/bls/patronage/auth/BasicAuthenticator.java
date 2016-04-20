package com.bls.patronage.auth;

import java.util.UUID;

import org.glassfish.jersey.internal.util.Base64;
import org.mindrot.jbcrypt.BCrypt;

import com.bls.patronage.StudyBoxConfiguration;
import com.bls.patronage.db.dao.UserDAO;
import com.bls.patronage.db.model.User;
import com.google.common.base.Optional;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import static com.google.common.base.Preconditions.checkState;

public class BasicAuthenticator implements Authenticator<BasicCredentials, User> {

    private final UserDAO userDao;

    public BasicAuthenticator(UserDAO userDao) {
        this.userDao = userDao;
    }

    public static String generateSafeHash(final String plaintextPassword) {
        return BCrypt.hashpw(plaintextPassword, BCrypt.gensalt(StudyBoxConfiguration.PW_HASH_SECURITY_LEVEL));
    }

    private static boolean isMatched(final String plaintextPassword, final String hashed) {
        return BCrypt.checkpw(plaintextPassword, hashed);
    }

    @Override
    public Optional<User> authenticate(final BasicCredentials basicCredentials) throws AuthenticationException {

        String email = basicCredentials.getUsername();
        String plaintextPassword = basicCredentials.getPassword();

        if (email.equals(PreAuthenticationFilter.defultUserEmail)) {
            return Optional.of(new User(UUID.randomUUID(), email, "", Base64.encodeAsString(plaintextPassword)));
        }

        final Optional<User> user = Optional.of(userDao.getUserByEmail(email));
        if (user.isPresent()) {
            final User existingUser = user.get();
            checkState(existingUser.getPassword() != null, "Cannot authenticate: user with id: %s (email: %s) without password",
                    existingUser.getId(), existingUser.getEmail());

            if (isMatched(plaintextPassword, existingUser.getPassword())) {
                return user;
            }
        }
        return Optional.absent();
    }
}
