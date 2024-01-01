package com.gwidgets.providers;

import com.gwidgets.resources.ApiKeyResource;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

public class ApiKeyResourceProvider implements RealmResourceProvider {

    private final KeycloakSession session;

    public ApiKeyResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    public Object getResource() {
        return new ApiKeyResource(session);
    }

    public void close() {}
}
