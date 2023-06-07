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
    private final String roleName;

    public ApiKeyResource(KeycloakSession session) {
        this.session = session;
        String envRealmName = System.getenv("REALM_NAME");
        String envRoleName = System.getenv("ROLE_NAME_ALLOWED");
        this.realmName = Objects.isNull(envRealmName) || Objects.equals(System.getenv(envRealmName), "")? "example": envRealmName;
        this.roleName =  Objects.isNull(envRoleName) || Objects.equals(System.getenv(envRoleName), "")? "example": envRoleName;
    }

    @GET
    @Produces("application/json")
    public Response getApiKey(@QueryParam("name") String userName) {
        UserModel user = session.userStorageManager().getUserByUsername(session.realms().getRealm(realmName), userName);

            if(user != null && user.isEnabled() && user.getRoleMappingsStream().anyMatch(r -> r.getName().equals(roleName)) && user.getAttributeStream("api-key").count() == 1)
                return user.getAttributeStream("api-key").findFirst()
                        .map(key -> Response.ok(key).type(MediaType.APPLICATION_JSON).build())
                        .orElseGet(() -> Response.status(401).type(MediaType.APPLICATION_JSON).build());

        return Response.status(401).type(MediaType.APPLICATION_JSON).build();
    }
}
