package com.gwidgets.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.keycloak.models.KeycloakSession;

@Provider
public class ApiKeyResource {

    private final KeycloakSession session;

    public ApiKeyResource(KeycloakSession session) {
        this.session = session;
    }

    @GET
    @Produces("application/json")
    public Response checkApiKey(@QueryParam("apiKey") String apiKey) {
        return session.users().searchForUserByUserAttributeStream(session.getContext().getRealm(), "api-key", apiKey)
                .findFirst().isPresent() ? Response.ok().type(MediaType.APPLICATION_JSON).build():
                Response.status(401).type(MediaType.APPLICATION_JSON).build();
    }
}
