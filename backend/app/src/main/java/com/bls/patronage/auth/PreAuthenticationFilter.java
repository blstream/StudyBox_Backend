package com.bls.patronage.auth;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.glassfish.jersey.internal.util.Base64;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Priority(Priorities.AUTHENTICATION - 100)
public class PreAuthenticationFilter implements ContainerRequestFilter {
    public static String defultUserEmail = "anon";
    public static String defultUserPassword = "password";

    /*
     * This class is responsible for getting the authHeaderRequired paramether from studybox-h2.yml file.
     * I know, it's very low level solution, but it works, and this whole class will be deleted
     * in the future, so i decided to do it this way. This method is also used in UserResourceTest class.
     */
    public static Boolean isAuthHeaderEnabledInConfig() throws IOException {
        JsonParser parser = new ObjectMapper(new YAMLFactory())
                .getFactory()
                .createParser(Files.newInputStream(Paths.get("studybox-h2.yml")));
        while (parser.nextToken() != null) {
            if (parser.getCurrentName() != null && parser.getCurrentName().equals("authHeaderRequired")) {
                parser.nextToken();
                return parser.getBooleanValue();
            }
        }
        return null;
    }

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        boolean hasValidHeader = false;
        if (request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            final String header = request.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (header.toLowerCase().startsWith("basic")) {
                hasValidHeader = true;
            }
        }
        if (!hasValidHeader) {
            if (isAuthHeaderEnabledInConfig())
                request.abortWith(
                        Response.noContent().status(Response.Status.FORBIDDEN).build()
                );
            final String base64 = Base64.encodeAsString(defultUserEmail + ":" + defultUserPassword);
            request.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, "Basic " + base64);
        }
    }
}