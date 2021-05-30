package com.gwidgets.resources;

import java.util.List;
import java.util.Objects;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;

public class ApiKeyResource {

    private KeycloakSession session;

    private final String realmName;

    public ApiKeyResource(KeycloakSession session) {
        this.session = session;
        String envRealmName = System.getenv("REALM_NAME");
        this.realmName = Objects.isNull(envRealmName) || Objects.equals(System.getenv(envRealmName), "")? "example": envRealmName;
    }

    @GET
    @Produces("application/json")
    public Response checkApiKey(@QueryParam("apiKey") String apiKey) {
        List<UserModel> result = session.userStorageManager().searchForUserByUserAttribute("api-key", apiKey, session.realms().getRealm(realmName));
        return result.isEmpty() ? Response.status(401).type(MediaType.APPLICATION_JSON).build(): Response.ok().type(MediaType.APPLICATION_JSON).build();
    }
}
