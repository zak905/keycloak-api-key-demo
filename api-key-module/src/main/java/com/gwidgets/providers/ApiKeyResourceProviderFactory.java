package com.gwidgets.providers;

import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

public class ApiKeyResourceProviderFactory implements RealmResourceProviderFactory {

    public RealmResourceProvider create(KeycloakSession session) {
        return new ApiKeyResourceProvider(session);
    }

    public void init(Config.Scope config) {}

    public void postInit(KeycloakSessionFactory factory) {}

    public void close() {}

    public String getId() {
        return "check";
    }
}
